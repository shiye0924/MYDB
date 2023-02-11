package top.syshine.mydb.common;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/11:51
 * @Description:
 */
public class Error {

    // common
    public static final Exception CacheFullException = new RuntimeException("Cache is full!");
    public static final Exception FileExistsException = new RuntimeException("File already exists!");
    public static final Exception FileNotExistsException = new RuntimeException("File does not exists!");
    public static final Exception FileCannotRWException = new RuntimeException("File cannot read or write!");

    // tm
    public static final Exception BadXIDFileException = new RuntimeException("Bad XID file!");
}
