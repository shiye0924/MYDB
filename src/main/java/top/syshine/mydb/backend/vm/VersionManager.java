package top.syshine.mydb.backend.vm;

import top.syshine.mydb.backend.dm.DataManager;
import top.syshine.mydb.backend.tm.TransactionManger;

/**
 * @Author: 石烨
 * @Date: 2023/02/14/20:29
 * @Description:
 */
public interface VersionManager {
    byte[] read(long xid,long uid) throws Exception;
    long insert(long xid,byte[] data) throws Exception;
    boolean delete(long xid,long uid) throws Exception;

    long begin(int level);
    void commit(long xid) throws Exception;
    void abort(long xid);

    public static VersionManager newVersionManager(TransactionManger tm, DataManager dm){
        return new VersionManagerImpl(tm,dm);
    }
}
