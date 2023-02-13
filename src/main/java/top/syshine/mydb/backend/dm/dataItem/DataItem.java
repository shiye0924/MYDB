package top.syshine.mydb.backend.dm.dataItem;

import com.google.common.primitives.Bytes;
import top.syshine.mydb.backend.common.SubArray;
import top.syshine.mydb.backend.dm.DataManagerImpl;
import top.syshine.mydb.backend.dm.page.Page;
import top.syshine.mydb.backend.utils.Types;
import top.syshine.mydb.common.Parser;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: 石烨
 * @Date: 2023/02/13/19:45
 * @Description:
 */
public interface DataItem {
    SubArray data();

    void before();
    void unBefore();
    void after(long xid) throws IOException;
    void release();

    void lock();
    void unlock();
    void rLock();
    void rUnLock();

    Page page();
    long getUid();
    byte[] getOldRaw();
    SubArray getRaw();

    public static byte[] wrapDataItemRaw(byte[] raw) {
        byte[] valid = new byte[1];
        byte[] size = Parser.short2Byte((short)raw.length);
        return Bytes.concat(valid, size, raw);
    }

    // 从页面的offset处解析处dataitem
    public static DataItem parseDataItem(Page pg, short offset, DataManagerImpl dm) {
        byte[] raw = pg.getData();
        short size = Parser.parseShort(Arrays.copyOfRange(raw, offset+DataItemImpl.OF_SIZE, offset+DataItemImpl.OF_DATA));
        short length = (short)(size + DataItemImpl.OF_DATA);
        long uid = Types.addressToUid(pg.getPageNumber(), offset);
        return new DataItemImpl(new SubArray(raw, offset, offset+length), new byte[length], pg, uid, dm);
    }

    public static void setDataItemRawInvalid(byte[] raw) {
        raw[DataItemImpl.OF_VALID] = (byte)1;
    }
}
