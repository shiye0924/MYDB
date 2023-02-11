package top.syshine.mydb.backend.common;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/15:52
 * @Description:
 */
public class SubArray {
    public byte[] raw;
    public int start;
    public int end;

    public SubArray(byte[] raw, int start, int end) {
        this.raw = raw;
        this.start = start;
        this.end = end;
    }
}
