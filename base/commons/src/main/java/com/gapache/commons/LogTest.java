package com.gapache.commons;

import com.gapache.commons.utils.IStringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author HuSen
 * @since 2020/5/15 6:50 下午
 */
public class LogTest {

    public static void main(String[] args) throws IOException {
        // 用jdk的
        Logger logger = Logger.getLogger("神奇的log------------test");
        logger.info("some");

        FileInputStream inputStream = new FileInputStream("/Users/husen/develop/work/customize-1.dic");
        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        String s = IStringUtils.newString(bytes);
        Set<String> set = new HashSet<>();
        String[] split = s.split(System.lineSeparator());
        for (String s1 : split) {
            System.out.println(s1);
            set.add(s1.trim());
        }
        FileWriter writer = new FileWriter("/Users/husen/develop/work/customize-2.dic", true);
        for (String s1 : set) {
            writer.write(s1.concat(System.lineSeparator()));
        }
        writer.flush();
        writer.close();
    }
}
