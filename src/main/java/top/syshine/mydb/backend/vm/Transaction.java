package top.syshine.mydb.backend.vm;

import top.syshine.mydb.backend.tm.TransactionMangerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 石烨
 * @Date: 2023/02/14/20:38
 * @Description:
 */
//vm对事务的抽象
public class Transaction {
    public long xid;
    public int level;
    public Map<Long, Boolean> snapshot;
    public Exception err;
    public boolean autoAborted;

    public static Transaction newTransaction(long xid, int level, Map<Long, Transaction> active) {
        Transaction t = new Transaction();
        t.xid = xid;
        t.level = level;
        if(level != 0) {
            t.snapshot = new HashMap<>();
            for(Long x : active.keySet()) {
                t.snapshot.put(x, true);
            }
        }
        return t;
    }

    public boolean isInSnapshot(long xid) {
        if(xid == TransactionMangerImpl.SUPER_XID) {
            return false;
        }
        return snapshot.containsKey(xid);
    }
}
