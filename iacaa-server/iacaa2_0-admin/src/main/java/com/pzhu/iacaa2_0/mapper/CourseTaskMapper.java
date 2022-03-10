package com.pzhu.iacaa2_0.mapper;

import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.iacaa2_0.entityVo.CourseTaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface CourseTaskMapper extends BaseMapper<CourseTask> {
    List<CourseTask> getByCourseId(Long id);

    List<CourseTaskVo> voList(CourseTaskVo vo);

    List<CourseTask> list(CourseTask courseTask);

    List<CourseTaskVo> randomlist(@Param("courseTask") CourseTask courseTask,@Param("randomSize") int randomSize);

    Boolean summaryStuScore(Long courseTaskId);

    void coverNullToZero();
}
