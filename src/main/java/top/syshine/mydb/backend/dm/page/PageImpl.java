package top.syshine.mydb.backend.dm.page;

import top.syshine.mydb.backend.dm.pageCache.PageCacheImpl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/17:45
 * @Description:
 */
public class PageImpl implements Page{

    private int pageNumber;
    private byte[] data;
    private boolean dirty;
    private Lock lock;
    // 用来方便在拿到Page的引用时可以快速对这个页面的缓存进行释放操作
    private PageCacheImpl pc;


    public PageImpl(int pageNumber, byte[] data, PageCacheImpl pc){
        this.pageNumber = pageNumber;
        this.data = data;
        this.pc = pc;
        lock = new ReentrantLock();
    }
    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public void release() {
        pc.release(this);
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public byte[] getData() {
        return data;
    }
}
