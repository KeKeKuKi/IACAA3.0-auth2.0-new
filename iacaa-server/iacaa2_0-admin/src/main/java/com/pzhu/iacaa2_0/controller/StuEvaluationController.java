package com.pzhu.iacaa2_0.controller;


import com.gapache.security.annotation.AuthResource;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entity.StuEvaluation;
import com.pzhu.iacaa2_0.entityVo.*;
import com.pzhu.iacaa2_0.service.ICourseTaskService;
import com.pzhu.iacaa2_0.service.IStuEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/stuEvaluation")
public class StuEvaluationController {
    @Autowired
    ICourseTaskService courseTaskService;

    @Autowired
    IStuEvaluationService stuEvaluationService;

    @RequestMapping("getQuestions")
    public ActionResult getQuestions (@RequestBody CourseVo courseVo){
        CourseTask courseTask = new CourseTask();
//        courseTask.setYear(LocalDateTime.now().getYear());
        int randomSize = 8;
        List<CourseTaskVo> courseTasks = courseTaskService.randomlist(courseTask,randomSize);
        List<StuEvaluationVo> stuEvaluationVos = new ArrayList<>();
        courseTasks.forEach(i -> {
            StuEvaluationVo vo = new StuEvaluationVo();
            vo.setCourseTaskVo(i);
            vo.setScore(0D);
            stuEvaluationVos.add(vo);
        });
        return ActionResult.ofSuccess(stuEvaluationVos);
    }

    @RequestMapping("saveAll")
    public ActionResult saveAll (@RequestBody EvaluationsList evaluationsList, HttpServletRequest request) throws InterruptedException {
        // 为了让用户觉得系统很牛，加载一会儿
//        Thread.sleep(300);

        List<StuEvaluation> stuEvaluations = evaluationsList.getStuEvaluations();
        stuEvaluations.forEach(i -> {
            i.setIp(request.getLocalAddr());
            i.setCreatedDate(LocalDateTime.now());
            i.setUpdateDate(LocalDateTime.now());
        });
        boolean b = stuEvaluationService.saveBatch(stuEvaluations);
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail("保存失败");
    }

    @RequestMapping("statisticsByCourseTaskId")
    public ActionResult statisticsByCourseTaskId (@RequestBody CourseTask courseTask){
        List<StuEvaluationStatisticsVo> stuEvaluationStatisticsVos = stuEvaluationService.statisticsByCourseTaskId(courseTask.getId());
        return ActionResult.ofSuccess(stuEvaluationStatisticsVos);
    }

}
