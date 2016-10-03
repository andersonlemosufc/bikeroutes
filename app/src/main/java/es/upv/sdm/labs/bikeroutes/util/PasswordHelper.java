package es.upv.sdm.labs.bikeroutes.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by anderson on 25/04/2016.
 */
public class PasswordHelper {

    public static String md5(String senha) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        sen = hash.toString(16);
        return sen;
    }

    public static String generatePassword(){
        String s = "ABCDEFGHYJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", res="";
        for(int k=0;k<10;k++) res += s.charAt((int) (Math.random() * 62));
        return res;
    }
}
