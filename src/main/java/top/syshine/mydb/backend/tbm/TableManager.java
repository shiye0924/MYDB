package top.syshine.mydb.backend.tbm;


import top.syshine.mydb.backend.dm.DataManager;
import top.syshine.mydb.backend.parser.statement.*;
import top.syshine.mydb.backend.vm.VersionManager;
import top.syshine.mydb.common.Parser;

public interface TableManager {
    top.syshine.mydb.backend.tbm.BeginRes begin(Begin begin);
    byte[] commit(long xid) throws Exception;
    byte[] abort(long xid);

    byte[] show(long xid);
    byte[] create(long xid, Create create) throws Exception;

    byte[] insert(long xid, Insert insert) throws Exception;
    byte[] read(long xid, Select select) throws Exception;
    byte[] update(long xid, Update update) throws Exception;
    byte[] delete(long xid, Delete delete) throws Exception;

    public static TableManager create(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.create(path);
        booter.update(Parser.long2Byte(0));
        return new top.syshine.mydb.backend.tbm.TableManagerImpl(vm, dm, booter);
    }

    public static TableManager open(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.open(path);
        return new top.syshine.mydb.backend.tbm.TableManagerImpl(vm, dm, booter);
    }
}
