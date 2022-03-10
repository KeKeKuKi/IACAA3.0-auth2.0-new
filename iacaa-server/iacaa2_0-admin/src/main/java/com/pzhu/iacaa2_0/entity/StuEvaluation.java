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
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_stu_evaluation")
public class StuEvaluation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 课程目标
     */
    @TableField("course_task")
    private Integer courseTask;

    /**
     * id地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 学号
     */
    @TableField("stu_no")
    private String stuNo;

    /**
     * 成绩
     */
    @TableField("score")
    private Double score;

    /**
     * 创建时间
     */
    @TableField("created_date")
    private LocalDateTime createdDate;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;


}
