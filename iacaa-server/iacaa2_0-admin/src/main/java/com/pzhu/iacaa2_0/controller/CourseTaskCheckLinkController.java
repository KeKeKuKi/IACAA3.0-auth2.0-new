package com.pzhu.iacaa2_0.controller;


import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.CourseTaskCheckLink;
import com.pzhu.iacaa2_0.entityVo.CourseTaskCheckLinkVo;
import com.pzhu.iacaa2_0.service.ICourseTaskCheckLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-21
 */
@RestController
@NeedAuth("CourseTaskCheckLink")
@RequestMapping("/courseTaskCheckLink")
public class CourseTaskCheckLinkController {
    @Autowired
    ICourseTaskCheckLinkService courseTaskCheckLinkService;

    @RequestMapping("voList")
    @AuthResource(scope = "voList", name = "CourseTaskCheckLinkVo列表")
    public ActionResult voList(@RequestBody CourseTaskCheckLinkVo vo){
        List<CourseTaskCheckLinkVo> list = courseTaskCheckLinkService.voList(vo);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("delete")
    @AuthResource(scope = "delete", name = "删除CourseTaskCheckLink")
    public ActionResult delete(@RequestBody CourseTaskCheckLink checkLink){
        return courseTaskCheckLinkService.removeById(checkLink.getId())
                ? ActionResult.ofSuccess() : ActionResult.ofFail("删除失败");
    }

    @RequestMapping("saveOrUpdate")
    @AuthResource(scope = "saveOrUpdate", name = "保存或更新CourseTaskCheckLink列表")
    public ActionResult saveOrUpdate(@RequestBody List<CourseTaskCheckLink> list){
        AtomicReference<Boolean> iflegal = new AtomicReference<>(true);
        AtomicReference<Double> totalMix = new AtomicReference<>(0d);
        Map<Integer,Double> checkMap = new HashMap<>(list.size());
        list.forEach(i -> {
            if(checkMap.get(i.getCheckLinkId())==null){
                checkMap.put(i.getCheckLinkId(),i.getMix());
            }else {
                 iflegal.set(false);
            }
            if(i.getMix() < 0.09999999999d){
                iflegal.set(false);
            }
            if (i.getCheckLinkId() == null){
                iflegal.set(false);
            }
            totalMix.set(totalMix.get() + i.getMix());
            if (i.getId() == null){
                i.setCreatedDate(LocalDateTime.now());
            }
            i.setUpdateDate(LocalDateTime.now());
        });
        if(!iflegal.get()){
            return ActionResult.ofFail("考核环节不可重复且不为空，权重至少为0.1");
        }
        if(totalMix.get() < 0.99999999d || totalMix.get() > 1.000000001d){
            return ActionResult.ofFail("权重总和必须为1");
        }
        boolean b = courseTaskCheckLinkService.saveOrUpdateBatch(list);
        return b ? ActionResult.ofSuccess(list) : ActionResult.ofFail("更新失败");
    }
}
