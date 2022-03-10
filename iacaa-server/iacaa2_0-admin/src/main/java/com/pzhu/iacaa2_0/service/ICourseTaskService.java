package com.pzhu.iacaa2_0.service;

import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.CourseTaskVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface ICourseTaskService extends IService<CourseTask> {

    List<CourseTaskVo> voList(CourseTaskVo vo);

    Boolean summaryCourseTask(Integer year);

    List<CourseTask> list(CourseTask courseTask);

    List<CourseTaskVo> randomlist(CourseTask courseTask, int randomSize);

    Boolean summaryCourseTaskById(CourseTask courseTask);
}
