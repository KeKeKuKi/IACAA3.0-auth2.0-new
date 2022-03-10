package com.pzhu.iacaa2_0.entity;

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
@TableName("t_course_task_evaluate")
public class CourseTaskEvaluate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ip
     */
    @TableField("ip_adress")
    private String ipAdress;

    /**
     * 评价课程目标
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 分数（1~5分）
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 年份
     */
    @TableField("year")
    private LocalDateTime year;



}
