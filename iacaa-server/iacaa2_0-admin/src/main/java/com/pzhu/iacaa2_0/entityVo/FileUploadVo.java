package com.pzhu.iacaa2_0.entityVo;

import com.pzhu.iacaa2_0.entity.Course;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: fileUploadVo
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/5/1410:02
 */
@Data
public class FileUploadVo {
    private MultipartFile file;
    private Course course;
    private Integer year;
}
