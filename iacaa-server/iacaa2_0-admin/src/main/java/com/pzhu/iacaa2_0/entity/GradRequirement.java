package com.pzhu.iacaa2_0.entity;

import com.pzhu.iacaa2_0.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@TableName("t_grad_requirement")
public class GradRequirement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 毕业要求
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("discrible")
    private String discrible;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 系统计算成绩
     */
    @TableField("sys_grade")
    private Double sysGrade;

    /**
     * 学生评价成绩
     */
    @TableField("stu_grade")
    private Double stuGrade;

    @TableField(exist = false)
    private List<Target> targets;

}
