package com.pzhu.iacaa2_0.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

public class ExportFileUtils {
    /**
     * 根据模板导出Excel
     * @param map
     * @param templatePath
     * @param fileName
     * @return
     */
    public static void export(Map<String, Object> map,String templatePath,String fileName,HttpServletResponse response) throws IOException {
        TemplateExportParams params = new TemplateExportParams(templatePath+"/"+fileName);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 根据模板多sheet页导出
     * @param map
     * @param templatePath
     * @param fileName
     * @param sheetNamesArray
     * @return
     */
    public static void export(Map<String, Object> map, String templatePath, String fileName, String[] sheetNamesArray, HttpServletResponse response) throws IOException {
        //默认值为false，设置为true即表示会输出模版中的全部sheet，否则只会输出第一个sheet。
        TemplateExportParams params = new TemplateExportParams(templatePath,true);
        // 设置sheetName，若不设置该参数，则使用得原本得sheet名称
        params.setSheetName(sheetNamesArray);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"), XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
