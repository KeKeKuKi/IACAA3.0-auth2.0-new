package com.pzhu.iacaa2_0.controller;

import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: MenuController
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/4/914:12
 */
@RestController
@RequestMapping("/Menue")
@NeedAuth("Menue")
public class MenuController {

    @RequestMapping("yearData")
    @AuthResource(scope = "yearData", name = "年度数据分析")
    public void yearData(){

    }

    @RequestMapping("GradRequirement")
    @AuthResource(scope = "GradRequirement", name = "毕业要求管理")
    public void GradRequirement(){

    }

    @RequestMapping("Target")
    @AuthResource(scope = "Target", name = "指标点管理")
    public void Target(){

    }

    @RequestMapping("CourseTask")
    @AuthResource(scope = "CourseTask", name = "课程目标管理")
    public void CourseTask(){

    }

    @RequestMapping("Score")
    @AuthResource(scope = "Score", name = "课程成绩管理")
    public void Score(){

    }

    @RequestMapping("AdminCourse")
    @AuthResource(scope = "AdminCourse", name = "课程管理")
    public void AdminCourse(){

    }


}
