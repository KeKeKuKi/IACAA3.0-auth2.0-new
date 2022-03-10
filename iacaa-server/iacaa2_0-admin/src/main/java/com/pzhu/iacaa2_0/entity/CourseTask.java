package com.pzhu.iacaa2_0.entity;

import com.pzhu.iacaa2_0.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
@TableName("t_course_task")
public class CourseTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 描述
     */
    @TableField("describes")
    private String describes;

    /**
     * 关联课程
     */
    @TableField("course_id")
    private Integer courseId;

    /**
     * 关联指标点
     */
    @TableField("target_id")
    private Integer targetId;

    /**
     * 占比
     */
    @TableField("mix")
    private Double mix;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 系统录入成绩
     */
    @TableField("sys_grade")
    private Double sysGrade;

    /**
     * 学生评价成绩
     */
    @TableField("stu_grade")
    private Double stuGrade;



}
