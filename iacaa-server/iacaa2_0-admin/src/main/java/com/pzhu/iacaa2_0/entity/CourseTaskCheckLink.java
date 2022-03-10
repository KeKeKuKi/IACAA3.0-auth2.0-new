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
 * @since 2021-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_course_task_check_link")
public class CourseTaskCheckLink extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 课程目标
     */
    @TableField("course_task_id")
    private Integer courseTaskId;

    /**
     * 考核环节
     */
    @TableField("check_link_id")
    private Integer checkLinkId;

    /**
     * 支撑权重
     */
    @TableField("mix")
    private Double mix;

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
