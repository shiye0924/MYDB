package top.syshine.mydb.backend.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @Author: 石烨
 * @Date: 2023/02/11/20:36
 * @Description:
 */
public class RandomUtil {
    public static byte[] randomBytes(int length){
        Random r = new SecureRandom();
        byte[] buf = new byte[length];
        r.nextBytes(buf);
        return buf;
    }
}
