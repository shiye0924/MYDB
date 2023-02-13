package top.syshine.mydb.backend.dm.pageIndex;

/**
 * @Author: 石烨
 * @Date: 2023/02/13/19:41
 * @Description:页面信息
 */
public class PageInfo {

    public int pgno;
    public int freeSpace;

    public PageInfo(int pgno, int freeSpace) {
        this.pgno = pgno;
        this.freeSpace = freeSpace;
    }
}
