package top.syshine.mydb.backend.utils;

/**
 * @Author: 石烨
 * @Date: 2023/02/13/19:51
 * @Description:
 */
public class Types {
    public static long addressToUid(int pgno, short offset) {
        long u0 = (long)pgno;
        long u1 = (long)offset;
        return u0 << 32 | u1;
    }
}
