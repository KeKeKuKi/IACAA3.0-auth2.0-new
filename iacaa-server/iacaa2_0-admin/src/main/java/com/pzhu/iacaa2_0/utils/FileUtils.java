package com.pzhu.iacaa2_0.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: FileUtils
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/4/2610:28
 */
public class FileUtils {

    public static void download(String path, String fileName, HttpServletResponse response) throws IOException {
        File file;
        if(path.endsWith("/")){
            file = new File(path + fileName);
        }else {
            file = new File(path + "/" + fileName);
        }
        if (!file.exists()) {
            throw new IOException(file.getPath() + "文件不存在！");
        }
        InputStream fis = null;
        fis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        response.reset();
        response.setCharacterEncoding("utf-8");
        // Content-disposition 告诉浏览器以下载的形式打开
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        // application/ms-excel;charset=utf-8 告诉浏览器下载的文件是excel
        response.setContentType("application/ms-excel");

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        fis.close();
    }
}
