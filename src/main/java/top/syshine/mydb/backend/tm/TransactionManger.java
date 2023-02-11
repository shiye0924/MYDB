package top.syshine.mydb.backend.tm;

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


}
