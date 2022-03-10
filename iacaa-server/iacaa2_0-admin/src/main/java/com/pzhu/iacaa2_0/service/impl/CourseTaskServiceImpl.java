package com.pzhu.iacaa2_0.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entityVo.CourseTaskVo;
import com.pzhu.iacaa2_0.mapper.CourseTaskMapper;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import com.pzhu.iacaa2_0.service.ICourseTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@Service
public class CourseTaskServiceImpl extends ServiceImpl<CourseTaskMapper, CourseTask> implements ICourseTaskService {
    @Autowired
    ICheckLinkService checkLinkService;

    @Override
    public List<CourseTaskVo> voList(CourseTaskVo vo) {
        return baseMapper.voList(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean summaryCourseTask(Integer year){
        QueryWrapper<CourseTask> courseTaskQueryWrapper = new QueryWrapper<>();
        courseTaskQueryWrapper.eq("year",year);
        List<CourseTask> courseTasks = baseMapper.selectList(courseTaskQueryWrapper);
        courseTasks.forEach(i -> {
            checkLinkService.summaryByCourseTaskID(i.getId(),year);
            baseMapper.summaryStuScore(i.getId());
        });
        baseMapper.coverNullToZero();
        return true;
    }

    @Override
    public List<CourseTask> list(CourseTask courseTask) {
        return baseMapper.list(courseTask);
    }

    @Override
    public List<CourseTaskVo> randomlist(CourseTask courseTask, int randomSize) {
        return baseMapper.randomlist(courseTask,randomSize);
    }

    @Override
    public Boolean summaryCourseTaskById(CourseTask courseTask) {
        QueryWrapper<CourseTask> courseTaskQueryWrapper = new QueryWrapper<>();
        courseTaskQueryWrapper.eq("year",courseTask.getYear());
        courseTaskQueryWrapper.eq("course_id",courseTask.getCourseId());
        List<CourseTask> courseTasks = baseMapper.selectList(courseTaskQueryWrapper);
        courseTasks.forEach(i -> {
            checkLinkService.summaryByCourseTaskID(i.getId(),courseTask.getYear());
            baseMapper.summaryStuScore(i.getId());
        });
        baseMapper.coverNullToZero();
        return true;
    }

}
