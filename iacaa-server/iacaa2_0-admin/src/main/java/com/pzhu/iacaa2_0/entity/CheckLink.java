package com.pzhu.iacaa2_0.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.pzhu.iacaa2_0.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_check_link")
public class CheckLink extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 对应课程
     */
    @ExcelProperty(value = "course_id", index = 1)
    @TableField("course_id")
    private Long courseId;

    /**
     * 标题
     */
    @ExcelProperty(value = "name", index = 2)
    @TableField("name")
    private String name;

    /**
     * 标题
     */
    @ExcelProperty(value = "year", index = 3)
    @TableField("year")
    private Integer year;

    /**
     * 目标成绩
     */
    @ExcelProperty(value = "target_score", index = 4)
    @TableField("target_score")
    private Double targetScore;

    /**
     * 平均成绩
     */
    @ExcelProperty(value = "average_score", index = 5)
    @TableField("average_score")
    private Double averageScore;

}
