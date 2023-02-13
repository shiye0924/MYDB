package top.syshine.mydb.backend.dm.logger;

/**
 * @Author: 石烨
 * @Date: 2023/02/13/12:27
 * @Description:日志读写
 */

import com.google.common.primitives.Bytes;
import top.syshine.mydb.backend.utils.Panic;
import top.syshine.mydb.common.Error;
import top.syshine.mydb.common.Parser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 日志文件读写
 *
 * 日志文件标准格式为：
 * [XChecksum] [Log1] [Log2] ... [LogN] [BadTail]
 * XChecksum 为后续所有日志计算的Checksum，int类型
 *
 * 每条正确日志的格式为：
 * [Size] [Checksum] [Data]
 * Size 4字节int 标识Data长度
 * Checksum 4字节int
 */
public class LoggerImpl implements Logger{

    public static final int SEED = 13331;

    public static final int OF_SIZE = 0;
    public static final int OF_CHECKSUM = OF_SIZE + 4;
    public static final int OF_DATA = OF_CHECKSUM + 4;

    public static final String LOG_SUFFIX = ".log";

    private RandomAccessFile file;
    private FileChannel fc;
    private Lock lock;

    private long position;  // 当前日志指针的位置
    private long fileSize;  // 初始化时记录，log操作不更新
    private int xChecksum;

    LoggerImpl(RandomAccessFile file, FileChannel fc) {
        this.file = file;
        this.fc = fc;
        lock = new ReentrantLock();
    }

    public LoggerImpl(RandomAccessFile file, FileChannel fc, int xChecksum) {
        this.file = file;
        this.fc = fc;
        this.xChecksum = xChecksum;
        lock = new ReentrantLock();
    }

    void init() throws Exception {
        long size = 0;
        try {
            size = file.length();
        } catch (IOException e) {
            Panic.panic(e);
        }
        if(size < 4) {
            Panic.panic(Error.BadLogFileException);
        }

        ByteBuffer raw = ByteBuffer.allocate(4);
        try {
            fc.position(0);
            fc.read(raw);
        } catch (IOException e) {
            Panic.panic(e);
        }
        int xChecksum = Parser.parseInt(raw.array());
        this.fileSize = size;
        this.xChecksum = xChecksum;

        checkAndRemoveTail();
    }

    private void updateXChecksum(byte[] log) throws IOException {
        this.xChecksum = calChecksum(this.xChecksum,log);
        fc.position(0);
        fc.write(ByteBuffer.wrap(Parser.int2Byte(xChecksum)));
        fc.force(false);
    }
    private byte[] wrapLog(byte[] data){
        byte[] checksum = Parser.int2Byte(calChecksum(0,data));
        byte[] size = Parser.int2Byte(data.length);
        return Bytes.concat(size,checksum,data);
    }
    /**
     *
     * @param xCheck 初始校验和数
     * @param log  一条日志记录字节数组的长度
     * @return   单条日志的校验和
     */
    private int calChecksum(int xCheck,byte[] log){
        for (byte b : log){
            xCheck = xCheck * SEED + b;
        }
        return xCheck;
    }

    /**
     * 通过next方法不断地从文件中读取下一条日志，并将其中的Data解析出来并返回
     * 其中position是当前日志文件读到的位置偏移
     * @return
     */
    private byte[] interNext() throws IOException {
        if (position + OF_DATA >= fileSize){
            return null;
        }
        //读取size
        ByteBuffer tmp = ByteBuffer.allocate(4);
        fc.position(position);
        fc.read(tmp);
        int size = Parser.parseInt(tmp.array());
        if (position + size + OF_DATA > fileSize){
            return null;
        }

        //读取checksum+data
        ByteBuffer buf = ByteBuffer.allocate(OF_SIZE + size);
        fc.position(position);
        fc.read(buf);
        byte[] log = buf.array();

        //校验checksum
        int checkSum1 = calChecksum(0, Arrays.copyOfRange(log,OF_DATA,log.length));
        int checkSum2 = Parser.parseInt(Arrays.copyOfRange(log,OF_CHECKSUM,OF_DATA));
        if (checkSum1 != checkSum2){
            return null;
        }
        position += log.length;
        return log;
    }

    /**
     *
     * 在打开一个日志文件时，需要首先校验日志文件的 XChecksum，
     * 并移除文件尾部可能存在的 BadTail，由于 BadTail 该条日志尚未写入完成，
     * 文件的校验和也就不会包含该日志的校验和，去掉 BadTail 即可保证日志文件的一致性。
     */
    private void checkAndRemoveTail() throws Exception {
        rewind();
        int xCheck = 0;
        while (true){
            byte[] log = interNext();
            if (log == null){
                break;
            }
            xCheck = calChecksum(xCheck,log);
        }
        if (xCheck != xChecksum){
            Panic.panic(Error.BadLogFileException);
        }

        //截断文件到正常日志的末尾
        truncate(position);
        rewind();
    }

    /**
     * 向日志文件写入日志时，也是首先将数据包裹成日志格式，
     * 写入文件后，再更新文件的校验和，更新校验和时，会刷新缓冲区，保证内容写入磁盘。
     */
    @Override
    public void log(byte[] data) throws IOException {
        byte[] log = wrapLog(data);
        ByteBuffer buf = ByteBuffer.wrap(log);
        lock.lock();
        try{
            fc.position(fc.size());
            fc.write(buf);
        }catch (IOException e){
            Panic.panic(e);
        }finally {
            lock.unlock();
        }
        updateXChecksum(log);
    }

    @Override
    public void truncate(long x) throws Exception {

    }

    @Override
    public byte[] next() {
        return new byte[0];
    }

    @Override
    public void rewind() {

    }

    @Override
    public void close() {

    }
}
