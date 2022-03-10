package com.pzhu.iacaa2_0.base;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pzhu.iacaa2_0.easyexcel.LocalDateTimeConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    /**
     * 唯一标识
     */
    // 去除Myatisplus生成id
    @ExcelProperty(value = "id", index = 0)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "created_date", index = 6,converter = LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("created_date")
    private LocalDateTime createdDate;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "update_date", index = 7,converter = LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_date")
    private LocalDateTime updateDate;
}
