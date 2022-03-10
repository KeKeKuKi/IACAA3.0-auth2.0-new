package com.pzhu.iacaa2_0.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.entity.*;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.entityVo.GradRequirementVo;
import com.pzhu.iacaa2_0.entityVo.IdsVo;
import com.pzhu.iacaa2_0.entityVo.TargetVo;
import com.pzhu.iacaa2_0.mapper.StuScoreMapper;
import com.pzhu.iacaa2_0.service.*;
import com.pzhu.iacaa2_0.utils.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/gradRequirement")
@NeedAuth("GradRequirement")
public class GradRequirementController{

    @Autowired
    IGradRequirementService gradRequirementService;
    @Autowired
    ITargetService targetService;

    @Autowired
    ICourseService courseService;

    @Autowired
    ICourseTargetService courseTargetService;

    @Autowired
    ICourseTaskService courseTaskService;

    @Autowired
    ICheckLinkService checkLinkService;

    @Autowired
    ICourseTaskCheckLinkService courseTaskCheckLinkService;

    @Autowired
    IStuScoreService stuScoreService;

    @Autowired
    IStuEvaluationService stuEvaluationService;

    @RequestMapping("/list")
    @AuthResource(scope = "list", name = "毕业要求列表")
    public ActionResult list(@RequestBody GradRequirementVo vo) {
        List<GradRequirement> list = gradRequirementService.list(vo);
        return ActionResult.ofSuccess(list);
    }

//    @RequestMapping("/pageList")
//    @AuthResource(scope = "pageList", name = "毕业要求列表分页")
//    public ActionResult pageList(@RequestBody GradRequirementVo vo) {
//        QueryWrapper<GradRequirement> wrapper = new QueryWrapper<>();
//        if (!StringUtils.isEmpty(vo.getWord())) {
//            wrapper.like("name", vo.getWord()).or()
//                    .like("discrible", vo.getWord());
//        }
//        if (vo.getYear() != null) {
//            wrapper.eq("year", vo.getYear());
//        }
////        wrapper.orderByDesc("i");
////        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
//        List<GradRequirement> list = gradRequirementService.list(wrapper);
//        return ActionResult.ofSuccess(list);
//    }


    @RequestMapping("/voList")
    @AuthResource(scope = "voList", name = "毕业要求Vo列表")
    public ActionResult voList(@RequestBody GradRequirementVo vo) {
        List<GradRequirementVo> list = gradRequirementService.voList(vo);
        return ActionResult.ofSuccess(list);
    }

    @RequestMapping("/getOne")
    @AuthResource(scope = "getOne", name = "获取单个毕业要求")
    public ActionResult getOne(@RequestBody GradRequirementVo vo) {
        List<GradRequirementVo> list = gradRequirementService.voList(vo);
        return list == null ? ActionResult.ofFail(500,"没有该数据") : ActionResult.ofSuccess(list.get(0));
    }

    @RequestMapping("/voListAll")
    @AuthResource(scope = "voListAll", name = "毕业要求Vo全部列表")
    public ActionResult voListAll(@RequestBody GradRequirementVo vo) {
        List<GradRequirementVo> list = gradRequirementService.voList(vo);
        return ActionResult.ofSuccess(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/update")
    @AuthResource(scope = "update", name = "更新毕业要求")
    public ActionResult update(@RequestBody GradRequirementVo vo) {
        List<Target> targets = vo.getTargets();
        targets.forEach(i -> {
            if (i.getId() == null) {
                i.setCreatedDate(LocalDateTime.now());
            }
            i.setUpdateDate(LocalDateTime.now());
        });
        targetService.saveOrUpdateBatch(targets);
        vo.setUpdateDate(LocalDateTime.now());
        UpdateWrapper<GradRequirement> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", vo.getId());
        boolean update = gradRequirementService.update(vo, updateWrapper);
        return update ? ActionResult.ofSuccess() : ActionResult.ofFail(200, "更新失败");
    }

    @RequestMapping("/save")
    @AuthResource(scope = "save", name = "保存毕业要求")
    public ActionResult save(@RequestBody GradRequirement gradRequirement) {
        gradRequirement.setCreatedDate(LocalDateTime.now());
        gradRequirement.setUpdateDate(LocalDateTime.now());
        boolean update = gradRequirementService.save(gradRequirement);
        return update ? ActionResult.ofSuccess() : ActionResult.ofFail(200, "添加失败");
    }

    @RequestMapping("/deleteList")
    @AuthResource(scope = "deleteList", name = "删除毕业要求列表")
    public ActionResult deleteList(@RequestBody IdsVo ids) {
        boolean b = gradRequirementService.removeList(ids.getIds());
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail("删除失败");
    }

    @RequestMapping("/deleteOne")
    @AuthResource(scope = "deleteOne", name = "删除单个毕业要求")
    public ActionResult deleteOne(@RequestBody GradRequirement gradRequirement) {
        boolean b = gradRequirementService.removeById(gradRequirement.getId());
        return b ? ActionResult.ofSuccess() : ActionResult.ofFail("删除失败");
    }

    @RequestMapping("/exportTemplate")
    @AuthResource(scope = "exportTemplate", name = "导出模板")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        FileUtils.download("D:/doc/","import.xlsx",response);
    }

    @RequestMapping("/summaryAll")
    @AuthResource(scope = "summaryAll", name = "同步本年度成绩数据")
    public ActionResult summaryAll(@RequestBody GradRequirement gradRequirement) {
        Boolean aBoolean = gradRequirementService.summaryThisYearReqGrade(gradRequirement.getYear());
        return aBoolean ? ActionResult.ofSuccess() : ActionResult.ofFail("统计失败");
    }

    @Transactional
    @RequestMapping("/randData")
//    @AuthResource(scope = "randData", name = "生成三年随机数据（慎用）")
    public void randData() {
        Wrapper<Target> wrapper = new QueryWrapper<>();
        targetService.remove(wrapper);

        Wrapper<CourseTarget> wrapper1 = new QueryWrapper<>();
        courseTargetService.remove(wrapper1);

        Wrapper<CourseTask> wrapper2 = new QueryWrapper<>();
        courseTaskService.remove(wrapper2);

        Wrapper<CheckLink> wrapper3 = new QueryWrapper<>();
        checkLinkService.remove(wrapper3);

        Wrapper<CourseTaskCheckLink> wrapper4 = new QueryWrapper<>();
        courseTaskCheckLinkService.remove(wrapper4);

        Wrapper<StuEvaluation> wrapper5 = new QueryWrapper<>();
        stuEvaluationService.remove(wrapper5);

        Wrapper<StuScore> wrapper6 = new QueryWrapper<>();
        stuScoreService.remove(wrapper6);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        for (int year = 2018; year < 2022; year++) {
            GradRequirementVo vo = new GradRequirementVo();
            vo.setYear(year);
            List<GradRequirement> list = gradRequirementService.list(vo);
            int finalYear = year;
            list.forEach(req -> {
                int count = (int)((Math.random()) * 3 + 2);
                List<Target> targets = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Target target = new Target();
                    target.setYear(finalYear);
                    target.setReqId(req.getId().intValue());
                    target.setDiscribe(req.getName()+"的指标点"+i);
                    target.setCreatedDate(LocalDateTime.now());
                    target.setUpdateDate(LocalDateTime.now());
                    targets.add(target);
                }
                targets.forEach(target -> {
                    targetService.save(target);
                });
            });

            TargetVo query = new TargetVo();
            query.setYear(year);
            List<Target> list1 = targetService.list(query);
            int finalYear1 = year;
            list1.forEach(target -> {
                List<CourseTarget> courseTargets = new ArrayList<>();
                Double allMix = 1D;
                long courseId = (long)(Math.random() * 49 + 1);
                while (allMix > 0.1){
                    double mix = Double.parseDouble(nf.format(Math.random()/2 + 0.1));
                    CourseTarget courseTarget = new CourseTarget();
                    courseTarget.setTargetId(target.getId());
                    courseTarget.setCourseId(courseId%50+1);
                    courseTarget.setCreatedDate(LocalDateTime.now());
                    courseTarget.setUpdateDate(LocalDateTime.now());
                    if(allMix - mix > 0.1){
                        allMix -= mix;
                        courseTarget.setMix(mix);
                    }else {
                        courseTarget.setMix(Double.parseDouble(nf.format(allMix)));
                        allMix -= mix;
                    }
                    courseTargets.add(courseTarget);
                    List<CourseTask> courseTasks = new ArrayList<>();
                    Double allMix2 = 1D;
                    int j = 1;
                    while (allMix2 > 0.1){
                        double mix2 = Double.parseDouble(nf.format(Math.random()/2 + 0.3));
                        CourseTask courseTask = new CourseTask();
                        courseTask.setYear(finalYear1);
                        courseTask.setCourseId((int)courseId%50+1);
                        courseTask.setDescribes("支撑"+target.getId()+"的课程目标"+j++);
                        courseTask.setCreatedDate(LocalDateTime.now());
                        courseTask.setUpdateDate(LocalDateTime.now());
                        courseTask.setTargetId(target.getId().intValue());
                        if(allMix2 - mix2 > 0.1){
                            courseTask.setMix(mix2);
                            allMix2 -= mix2;
                        }else {
                            courseTask.setMix(Double.parseDouble(nf.format(allMix2)));
                            allMix2 -= mix2;
                        }
                        courseTasks.add(courseTask);
                    }
                    courseTaskService.saveBatch(courseTasks);
                    courseId += (Math.random() * 6 + 1);
                }
                courseTargetService.saveBatch(courseTargets);
            });
            List<Course> courses = courseService.list();
            int finalYear2 = year;
            courses.forEach(course -> {
                List<CheckLink> checkLinks = new ArrayList<>();
                double allMix = 1D;
                int j = 1;
                while (allMix > 0.1){
                    double mix = Double.parseDouble(nf.format(Math.random()/3 + 0.1));
                    CheckLink checkLink = new CheckLink();
                    checkLink.setYear(finalYear2);
                    checkLink.setCreatedDate(LocalDateTime.now());
                    checkLink.setUpdateDate(LocalDateTime.now());
                    checkLink.setTargetScore(100D);
                    checkLink.setAverageScore(Math.random()*80 + 20);
                    checkLink.setName(""+ finalYear2 + "" + "考核环节" + j++);
                    checkLink.setAverageScore((int)(Math.random()*80) + 20d);
                    checkLink.setCourseId(course.getId());
                    allMix -= mix;
                    checkLinks.add(checkLink);
                }
                checkLinkService.saveBatch(checkLinks);
            });

            CourseTask courseTask = new CourseTask();
            courseTask.setYear(year);
            List<CourseTask> courseTasks = courseTaskService.list(courseTask);
            int finalYear3 = year;
            courseTasks.forEach(i -> {
                CheckLinkVo checkLinkVo = new CheckLinkVo();
                checkLinkVo.setCourseId(i.getCourseId().longValue());
                checkLinkVo.setYear(finalYear3);
                List<CheckLink> checkLinks1 = checkLinkService.list(checkLinkVo);
                List<CourseTaskCheckLink> checkLinks = new ArrayList<>();
                double allMix = 1D;
                int j = 0;
                while (allMix > 0.1){
                    double mix = Double.parseDouble(nf.format(Math.random()/2 + 0.2));
                    CourseTaskCheckLink checkLink = new CourseTaskCheckLink();
                    checkLink.setCreatedDate(LocalDateTime.now());
                    checkLink.setUpdateDate(LocalDateTime.now());
                    checkLink.setCourseTaskId(i.getId().intValue());
                    checkLink.setCheckLinkId(checkLinks1.get(j++%checkLinks1.size()).getId().intValue());
                    if(allMix - mix > 0.1){
                        checkLink.setMix(mix);
                        allMix -= mix;
                    }else {
                        checkLink.setMix(Double.parseDouble(nf.format(allMix)));
                        allMix -= mix;
                    }
                    checkLinks.add(checkLink);
                }
                courseTaskCheckLinkService.saveBatch(checkLinks);
            });


            GradRequirement gradRequirement = new GradRequirement();
            gradRequirement.setYear(year);

            this.summaryAll(gradRequirement);
        }

        List<CourseTask> list11 = courseTaskService.list();
        List<StuEvaluation> stuEvaluations = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            StuEvaluation stuEvaluation = new StuEvaluation();
            String s = UUID.randomUUID().toString();
            stuEvaluation.setStuNo(s.substring(0,13));
            stuEvaluation.setScore((double)((int)(Math.random()*5))+1);
            stuEvaluation.setCourseTask(list11.get((int)(Math.random()*list11.size()-2 + 1)).getId().intValue());
            stuEvaluation.setCreatedDate(LocalDateTime.now());
            stuEvaluation.setUpdateDate(LocalDateTime.now());
            stuEvaluations.add(stuEvaluation);
        }
        stuEvaluationService.saveBatch(stuEvaluations);

        List<CheckLink> list = checkLinkService.list();
        List<StuScore> stuScoreList = new ArrayList<>(50);
        list.forEach(i -> {
            for (int j = 0; j < 50; j++) {
                StuScore stuScore = new StuScore();
                stuScore.setScore((double)((int)(Math.random()*50+50)));
                String s = UUID.randomUUID().toString();
                stuScore.setStuno(s.substring(0,13));
                stuScore.setCheckLinkId(i.getId().intValue());
                stuScore.setCreatedDate(LocalDateTime.now());
                stuScore.setUpdateDate(LocalDateTime.now());
                stuScore.setMixScore(stuScore.getScore()/100d);
                stuScoreList.add(stuScore);
            }
        });
        stuScoreService.saveBatch(stuScoreList);
    }


//    private String getString(){
//        int count = (int)((Math.random()) * 30 + 12);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < count; i++) {
//            stringBuilder.append((char)((int)((Math.random()) * (40869 - 19968) + 19968)));
//        }
//        return stringBuilder.toString();
//    }
}
