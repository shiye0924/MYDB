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

    // dm
    public static final Exception BadLogFileException = new RuntimeException("Bad log file!");
    public static final Exception MemTooSmallException = new RuntimeException("Memory too small!");
    public static final Exception DataTooLargeException = new RuntimeException("Data too large!");
    public static final Exception DatabaseBusyException = new RuntimeException("Database is busy!");

    // vm
    public static final Exception DeadlockException = new RuntimeException("Deadlock!");
    public static final Exception ConcurrentUpdateException = new RuntimeException("Concurrent update issue!");
    public static final Exception NullEntryException = new RuntimeException("Null entry!");
}
