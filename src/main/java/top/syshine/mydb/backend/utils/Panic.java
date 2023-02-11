package top.syshine.mydb.backend.utils;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/11:49
 * @Description:
 */
public class Panic {
    public static void panic(Exception err){
        err.printStackTrace();
        System.exit(1);
    }
}
