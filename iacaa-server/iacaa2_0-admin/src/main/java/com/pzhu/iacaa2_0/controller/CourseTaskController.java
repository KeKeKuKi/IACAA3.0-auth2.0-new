package com.pzhu.iacaa2_0.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entityVo.CourseTaskVo;
import com.pzhu.iacaa2_0.service.ICourseTaskService;
import com.pzhu.iacaa2_0.service.IStuEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/courseTask")
@NeedAuth("CourseTask")
public class CourseTaskController {
    @Autowired
    ICourseTaskService courseTaskService;

    @Autowired
    IStuEvaluationService stuEvaluationService;

    @RequestMapping("/voList")
    @AuthResource(scope = "voList", name = "课程目标Vo列表")
    public ActionResult voList(@RequestBody CourseTaskVo vo){
        List<CourseTaskVo> courseTaskVos = courseTaskService.voList(vo);
        return ActionResult.ofSuccess(courseTaskVos);
    }

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "课程目标列表")
    public ActionResult list(@RequestBody CourseTask courseTask){
        List<CourseTask> courseTasks = courseTaskService.list(courseTask);
        return ActionResult.ofSuccess(courseTasks);
    }

    @RequestMapping("/getOne")
    @AuthResource(scope = "getOne", name = "获取单个课程目标")
    public ActionResult getOne(@RequestBody CourseTask courseTask){
        QueryWrapper<CourseTask> queryWrapper = new QueryWrapper<CourseTask>();
        if(courseTask.getId() != null){
            queryWrapper.eq("id",courseTask.getId());
        }
        if(courseTask.getCourseId() != null){
            queryWrapper.eq("course_id",courseTask.getCourseId());
        }
        if(courseTask.getTargetId() != null){
            queryWrapper.eq("target_id",courseTask.getTargetId());
        }
        if(courseTask.getYear() != null){
            queryWrapper.eq("year",courseTask.getYear());
        }
        CourseTask courseTasks = courseTaskService.getOne(queryWrapper);

        return ActionResult.ofSuccess(courseTasks);
    }

    @RequestMapping("/randomlist")
    public ActionResult randomlist(){
        CourseTask courseTask = new CourseTask();
        int randomSize = 4;
        List<CourseTaskVo> courseTasks = courseTaskService.randomlist(courseTask,randomSize);
        return ActionResult.ofSuccess(courseTasks);
    }

    @RequestMapping("/delete")
    @AuthResource(scope = "delete", name = "删除单个课程目标")
    public ActionResult delete(@RequestBody CourseTask courseTask){
        boolean b = courseTaskService.removeById(courseTask.getId());
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail("删除失败");
    }


    @RequestMapping("/saveOrUpdate")
    @AuthResource(scope = "saveOrUpdate", name = "保存或更新课程目标列表")
    public ActionResult saveOrUpdate(@RequestBody List<CourseTaskVo> courseTasks){
        List<CourseTask> tasks = new ArrayList<>();
        Map<String,Double> checkMap = new HashMap<>();
        AtomicBoolean able = new AtomicBoolean(true);
        courseTasks.forEach(i -> {
            String key = String.format("%S%S",i.getCourse().getId(),i.getTarget().getId());
            checkMap.merge(key, i.getMix(), Double::sum);
            CourseTask courseTask = new CourseTask();
            courseTask.setUpdateDate(LocalDateTime.now());
            if(i.getCourseId() != null){
                courseTask.setId(i.getId());
            }else {
                courseTask.setCreatedDate(LocalDateTime.now());
            }
            courseTask.setCourseId(i.getCourse().getId().intValue());
            courseTask.setMix(i.getMix());
            courseTask.setTargetId(i.getTarget().getId().intValue());
            courseTask.setDescribes(i.getDescribes());
            courseTask.setYear(i.getYear());
            tasks.add(courseTask);
        });
        Set<Map.Entry<String, Double>> entries = checkMap.entrySet();
        entries.forEach(entrie -> {
            if(entrie.getValue() > 1.0000001 || entrie.getValue() < 0.9999999d){
                able.set(false);
            }
        });

        if(able.get()){
            boolean b = courseTaskService.saveOrUpdateBatch(tasks);
            return b ? ActionResult.ofSuccess() : ActionResult.ofFail(200,"更新失败");
        }else {
            return ActionResult.ofFail(200,"所支撑单个指标点权重总和需等于1");
        }
    }

    @RequestMapping("/summaryCourseTask")
    @AuthResource(scope = "summaryCourseTask", name = "同步课程目标成绩数据")
    public ActionResult summaryCourseTask(@RequestBody CourseTask courseTask){
        return ActionResult.ofSuccess(courseTaskService.summaryCourseTask(courseTask.getYear()));
    }

    @RequestMapping("/summaryCourseTaskByCourseId")
    @AuthResource(scope = "summaryCourseTaskByCourseId", name = "同步课程目标成绩数据ById")
    public ActionResult summaryCourseTaskByCourseId(@RequestBody CourseTask courseTask){
        return ActionResult.ofSuccess(courseTaskService.summaryCourseTaskById(courseTask));
    }
}
