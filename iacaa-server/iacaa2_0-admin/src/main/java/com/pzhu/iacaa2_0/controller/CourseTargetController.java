package com.pzhu.iacaa2_0.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTarget;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entityVo.CourseTargetVo;
import com.pzhu.iacaa2_0.service.ICourseTargetService;
import com.pzhu.iacaa2_0.service.ICourseTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/courseTarget")
@NeedAuth("CourseTarget")
public class CourseTargetController {
    @Autowired
    ICourseTargetService courseTargetService;

    @Autowired
    ICourseTaskService courseTaskService;

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "课程-指标点列表")
    public ActionResult list(@RequestBody CourseTarget courseTarget){
        QueryWrapper<CourseTarget> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseTarget.getTargetId())){
            wrapper.like("target_id",courseTarget.getTargetId());
        }
        if(!StringUtils.isEmpty(courseTarget.getCourseId())){
            wrapper.like("course_id",courseTarget.getCourseId());
        }
        List<CourseTarget> list = courseTargetService.list(wrapper);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/voList")
    @AuthResource(scope = "voList", name = "课程-指标点Vo列表")
    public ActionResult voList(@RequestBody CourseTargetVo vo){
        List<CourseTargetVo> volist = courseTargetService.volist(vo);
        return ActionResult.ofSuccess(volist);
    }

    @RequestMapping("/thisYearvoList")
    @AuthResource(scope = "thisYearvoList", name = "本年度课程-指标点Vo列表")
    public ActionResult thisYearvoList(@RequestBody CourseTargetVo vo){
        List<CourseTargetVo> volist = courseTargetService.volist(vo);
        volist.removeIf(next -> !next.getTarget().getYear().equals(vo.getYear()));
        return ActionResult.ofSuccess(volist);
    }

    @RequestMapping("/saveOrUpdate")
    @AuthResource(scope = "saveOrUpdate", name = "保存或更新课程-指标点")
    public ActionResult saveOrUpdate(@RequestBody List<CourseTargetVo> vos){
        if(vos.isEmpty()){
            return ActionResult.ofFail("数据不得为空");
        }
        List<CourseTarget> courseTargets = new ArrayList<>();
        AtomicReference<Float> totalMix = new AtomicReference<>((float) 0);
        Map<String,String> checkName = new HashMap<>();
        AtomicBoolean nameOk = new AtomicBoolean(true);
        vos.forEach(i -> {
            if(StringUtils.isEmpty(i.getCourse().getId()) || checkName.get(i.getCourse().getId().toString()) != null){
                nameOk.set(false);
            }else {
                checkName.put(i.getCourse().getId().toString(),"have");
            }
            totalMix.updateAndGet(v -> (float) (v + i.getMix()));
            CourseTarget courseTarget = new CourseTarget();
            courseTarget.setUpdateDate(LocalDateTime.now());
            if(null != i.getId()){
                courseTarget.setId(i.getId());
            }else {
                courseTarget.setCreatedDate(LocalDateTime.now());
            }
            courseTarget.setCourseId(i.getCourse().getId());
            courseTarget.setTargetId(i.getTarget().getId());
            courseTarget.setMix(i.getMix());
            courseTargets.add(courseTarget);
        });
        if(!nameOk.get()){
            return ActionResult.ofFail(200,"课程信息不得为空或重复");
        }
        if(totalMix.get() > 1.000001 || totalMix.get() < 0.999999999){
            return ActionResult.ofFail(200,"权重总必须为1");
        }
        boolean b = courseTargetService.saveOrUpdateBatch(courseTargets);
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail(200,"后台异常，更新失败");
    }

    @RequestMapping("/deleteOne")
    @AuthResource(scope = "deleteOne", name = "删除单个课程-指标点")
    public ActionResult deleteOne(@RequestBody CourseTargetVo courseTargetVo){
        CourseTarget target = courseTargetService.getById(courseTargetVo.getId());
        QueryWrapper<CourseTask> courseTaskQueryWrapper = new QueryWrapper<>();
        courseTaskQueryWrapper.eq("course_id",target.getCourseId());
        courseTaskQueryWrapper.eq("target_id",target.getTargetId());
        boolean remove = courseTaskService.remove(courseTaskQueryWrapper);
        boolean b = courseTargetService.removeById(courseTargetVo.getId());
        return b && remove ? ActionResult.ofSuccess() : ActionResult.ofFail("删除失败");
    }
}
