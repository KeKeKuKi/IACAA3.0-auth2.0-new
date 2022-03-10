package com.pzhu.iacaa2_0.controller;


import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.github.pagehelper.PageInfo;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
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
@RequestMapping("/checkLink")
@NeedAuth("CheckLink")
public class CheckLinkController {
    @Autowired
    ICheckLinkService checkLinkService;

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "考核环节列表")
    public ActionResult list(@RequestBody CheckLinkVo vo) throws Exception{
        List<CheckLink> list = checkLinkService.list(vo);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/listBySourseTask")
    @AuthResource(scope = "listBySourseTask", name = "单个课程目标的考核环节列表")
    public ActionResult listBySourseTask(@RequestBody CourseTask courseTask) throws Exception{
        List<CheckLink> list = checkLinkService.listBySourseTask(courseTask.getId());
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/delete")
    @AuthResource(scope = "delete", name = "删除单个考核环节")
    public ActionResult delete(@RequestBody CheckLinkVo vo){
        boolean b = checkLinkService.removeById(vo.getId());
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail(200,"删除失败");
    }

    @RequestMapping("/pageList")
    @AuthResource(scope = "pageList", name = "考核环节分页列表")
    public ActionResult pageList(@RequestBody CheckLinkVo vo) {
//        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<CheckLink> list = checkLinkService.list(vo);
        PageInfo page = new PageInfo(list);
        return ActionResult.ofSuccess(page);
    }

    @RequestMapping("/saveOrUpdate")
    @AuthResource(scope = "saveOrUpdate", name = "更新或保存考核环节列表")
    public ActionResult saveOrUpdate(@RequestBody List<CheckLink> checkLinks){
        AtomicReference<Boolean> check = new AtomicReference<>(true);
        checkLinks.forEach(i -> {
            if(i.getTargetScore() <= 0){
                check.set(false);
            }
            if(i.getId() == null){
                i.setCreatedDate(LocalDateTime.now());
            }
            i.setUpdateDate(LocalDateTime.now());
        });
        if(check.get()){
            checkLinkService.saveOrUpdateBatch(checkLinks);
            return ActionResult.ofSuccess();
        }else {
            return ActionResult.ofFail("成绩不能小于或等于0");
        }
    }
}
