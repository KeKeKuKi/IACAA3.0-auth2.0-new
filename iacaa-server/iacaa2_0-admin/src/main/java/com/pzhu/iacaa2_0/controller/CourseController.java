package com.pzhu.iacaa2_0.controller;


import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.holder.AccessCardHolder;
import com.github.pagehelper.PageInfo;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entityVo.CourseVo;
import com.pzhu.iacaa2_0.entityVo.IdsVo;
import com.pzhu.iacaa2_0.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/course")
@NeedAuth("Course")
public class CourseController{

    @Autowired
    ICourseService courseService;

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "课程列表")
    public ActionResult list(@RequestBody Course course) throws Exception{
        List<Course> list = courseService.list();
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/voList")
    @AuthResource(scope = "voList", name = "课程Vo列表")
    public ActionResult list(@RequestBody CourseVo vo) throws Exception{
        Long userId = AccessCardHolder.getContext().getUserId();
        vo.setEditUserId(userId.intValue());
        List<CourseVo> list = courseService.voList(vo);
        PageInfo page = new PageInfo(list);
        return ActionResult.ofSuccess(page);
    }

    @RequestMapping("/authList")
    @AuthResource(scope = "authList", name = "权限课程列表")
    public ActionResult authList(@RequestBody CourseVo vo){
        Long userId = AccessCardHolder.getContext().getUserId();
        vo.setEditUserId(userId.intValue());
        List<Course> list = courseService.list(vo);
        PageInfo page = new PageInfo(list);
        return ActionResult.ofSuccess(page);
    }

    @RequestMapping("/adminList")
    @AuthResource(scope = "adminList", name = "admin课程列表")
    public ActionResult adminList(@RequestBody CourseVo vo) throws Exception{
        List<Course> list = courseService.list(vo);
        PageInfo page = new PageInfo(list);
        return ActionResult.ofSuccess(page);
    }

    @RequestMapping("/voListAll")
    @AuthResource(scope = "voListAll", name = "课程Vo列表所有")
    public ActionResult voListAll(@RequestBody CourseVo vo) {
        List<CourseVo> list = courseService.voList(vo);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/save")
    @AuthResource(scope = "save", name = "保存单个课程")
    public ActionResult save(@RequestBody Course course){
        course.setCreatedDate(LocalDateTime.now());
        course.setUpdateDate(LocalDateTime.now());
        boolean update = courseService.save(course);
        return ActionResult.ofSuccess();
    }

    @RequestMapping("/update")
    @AuthResource(scope = "update", name = "更新课程ByID")
    public ActionResult update(@RequestBody Course course){
        course.setUpdateDate(LocalDateTime.now());
        boolean update = courseService.updateById(course);
        return ActionResult.ofSuccess();
    }

    @RequestMapping("/changeEditStatus")
    @AuthResource(scope = "changeEditStatus", name = "改变课程编辑状态")
    public ActionResult changeEditStatus(@RequestBody Course course){
        Course byId = courseService.getById(course.getId());
        byId.setUpdateDate(LocalDateTime.now());
        byId.setEditStatus(Math.abs(byId.getEditStatus()-1));
        courseService.saveOrUpdate(byId);
        return ActionResult.ofSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/del")
    @AuthResource(scope = "del", name = "删除单个课程")
    public ActionResult del(@RequestBody IdsVo ids){
        for (Long id : ids.getIds()) {
            courseService.removeById(id);
        }
        return ActionResult.ofSuccess();
    }
}
