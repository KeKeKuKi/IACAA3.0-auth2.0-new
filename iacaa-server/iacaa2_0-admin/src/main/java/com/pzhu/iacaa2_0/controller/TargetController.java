package com.pzhu.iacaa2_0.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.github.pagehelper.PageInfo;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.Target;
import com.pzhu.iacaa2_0.entityVo.IdsVo;
import com.pzhu.iacaa2_0.entityVo.TargetVo;
import com.pzhu.iacaa2_0.service.ICourseTargetService;
import com.pzhu.iacaa2_0.service.ICourseTaskService;
import com.pzhu.iacaa2_0.service.ITargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/target")
@NeedAuth("Target")
public class TargetController {
    @Autowired
    ITargetService targetService;

    @Autowired
    ICourseTargetService courseTargetService;

    @RequestMapping("/pageList")
    @AuthResource(scope = "pageList", name = "指标点分页列表")
    public ActionResult list(@RequestBody TargetVo vo){
        QueryWrapper<Target> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(vo.getWord())){
            wrapper.like("discribe",vo.getWord());
        }
        if(!StringUtils.isEmpty(vo.getYear())){
            wrapper.eq("year",vo.getYear());
        }
        if(!StringUtils.isEmpty(vo.getReqId())){
            wrapper.eq("req_id",vo.getReqId());
        }
//        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<Target> list = targetService.list(wrapper);
        PageInfo page = new PageInfo(list);
        return ActionResult.ofSuccess(page);
    }

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "指标点列表")
    public ActionResult pageList(@RequestBody TargetVo vo){
        List<Target> list = targetService.list(vo);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/getOne")
    @AuthResource(scope = "getOne", name = "获取单个指标点")
    public ActionResult getOne(@RequestBody Target target){
        return ActionResult.ofSuccess(targetService.getById(target.getId()));
    }

    @RequestMapping("/update")
    @AuthResource(scope = "update", name = "更新单个指标点")
    public ActionResult update(@RequestBody Target target){
        target.setUpdateDate(LocalDateTime.now());
        UpdateWrapper<Target> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",target.getId());
        boolean update = targetService.update(target, updateWrapper);
        return update ? ActionResult.ofSuccess() : ActionResult.ofFail(200,"更新失败");
    }

    @RequestMapping("/save")
    @AuthResource(scope = "save", name = "保存单个指标点")
    public ActionResult save(@RequestBody Target target){
        target.setCreatedDate(LocalDateTime.now());
        target.setUpdateDate(LocalDateTime.now());
        boolean update = targetService.save(target);
        return update ? ActionResult.ofSuccess() : ActionResult.ofFail(200,"添加失败");
    }

    @RequestMapping("/del")
    @AuthResource(scope = "del", name = "删除多个指标点")
    public ActionResult del(@RequestBody IdsVo ids){
        for (Long id : ids.getIds()) {
            targetService.removeById(id);
        }
        return ActionResult.ofSuccess();
    }

    @RequestMapping("/deleteOne")
    @AuthResource(scope = "deleteOne", name = "删除单个指标点")
    public ActionResult deleteOne(@RequestBody Target target){
        return courseTargetService.removeByTargetId(target.getId())
                && targetService.removeById(target.getId())
                ? ActionResult.ofSuccess()
                : ActionResult.ofFail("删除失败");
    }

    @RequestMapping("/summaryAll")
    @AuthResource(scope = "summaryAll", name = "同步指标点年度成绩数据")
    public ActionResult summaryAll(@RequestBody Target target){
        Boolean aBoolean = targetService.summaryThisYearTargetsGrade(target.getYear());
        return aBoolean ? ActionResult.ofSuccess() : ActionResult.ofFail("统计失败");
    }
}
