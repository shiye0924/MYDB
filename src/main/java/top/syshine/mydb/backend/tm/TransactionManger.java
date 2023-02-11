package top.syshine.mydb.backend.tm;

import top.syshine.mydb.backend.utils.Panic;
import top.syshine.mydb.common.Error;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/11:21
 * @Description:
 */
public interface TransactionManger {
    long begin();   //开启一个新事务
    void commit(long xid); //提交一个新事务
    void abort(long xid); //取消一个事务
    boolean isActive(long xid);  //查询一个事务的状态是否正在进行的状态
    boolean isCommitted(long xid);  //查询一个事务的状态是否已经提交
    boolean isAborted(long xid); //查询一个事务的状态是否已经取消
    void close();  //关闭TM

    public static TransactionMangerImpl create(String path){
        File f = new File(path+TransactionMangerImpl.XID_SUFFIX);
        try {
            if (!f.createNewFile()){
                Panic.panic(Error.FileExistsException);
            }
        }catch (Exception e){
            Panic.panic(e);
        }
        if (!f.canRead() || !f.canWrite()){
            Panic.panic(Error.FileCannotRWException);
        }
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(f,"rw");
            fc = raf.getChannel();
        }catch (FileNotFoundException e){
            Panic.panic(e);
        }

        // 写空XID文件头
        ByteBuffer buf = ByteBuffer.wrap(new byte[TransactionMangerImpl.LEN_XID_HEADER_LENGTH]);
        try{
            fc.position(0);
            fc.write(buf);
        }catch (IOException e){
            Panic.panic(e);
        }
        return new TransactionMangerImpl(raf,fc);
    }

    public static TransactionMangerImpl open(String path){
        File f = new File(path+TransactionMangerImpl.XID_SUFFIX);
        if (!f.exists()){
            Panic.panic(Error.FileNotExistsException);
        }
        if (!f.canRead() || !f.canWrite()){
            Panic.panic(Error.FileCannotRWException);
        }
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(f,"rw");
            fc = raf.getChannel();
        }catch (FileNotFoundException e){
            Panic.panic(e);
        }
        return new TransactionMangerImpl(raf,fc);
    }


}
