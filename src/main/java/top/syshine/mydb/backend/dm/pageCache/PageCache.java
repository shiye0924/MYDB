package top.syshine.mydb.backend.dm.pageCache;

import top.syshine.mydb.backend.dm.page.Page;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/17:55
 * @Description:
 */
public interface PageCache {
    public static final int PAGE_SIZE = 1 << 13;

    int newPage(byte[] initData);
    Page getPage(int pgno) throws Exception;
    void close();
    void release(Page page);

    void truncateByBgno(int maxPgno);
    int getPageNumber();
    void flushPage(Page pg);
}
