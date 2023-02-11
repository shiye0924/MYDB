package top.syshine.mydb.backend.dm.page;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/17:43
 * @Description:
 */
public interface Page {
    void lock();
    void unlock();
    void release();
    void setDirty(boolean dirty);
    boolean isDirty();
    int getPageNumber();
    byte[] getData();
}
