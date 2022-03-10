package com.pzhu.iacaa2_0.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

public class MyTools {

    //自定义MD5加密算法进行加密处理
    public static String toMd5(String str){
        //加密
        MessageDigest md5;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            try {
                md5 = MessageDigest.getInstance("MD5");
            }catch (Exception e2){
                e2.printStackTrace();
                return "";
            }
        }
        String slat = "23jke-ahsdj6568%@&&&&__&%5123***&&%%$$#@HSIH";

        String dataStr = str + slat;
        try{
            md5.update(dataStr.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            e.printStackTrace();
        }
        byte [] s = md5.digest();
        StringBuilder result = new StringBuilder();
        for (byte i : s) {
            result.append(Integer.toHexString((0x000000FF & i) | 0xFFFFFF00).substring(6));
        }
        return result.toString();
    }



    public static void printBehaviorLog(LocalDateTime time, String who, String doWhat){
        Logger logger =  LoggerFactory.getLogger(MyTools.class);
        logger.info("#" + time + "#"+ who +"#"+doWhat);
    }
}
