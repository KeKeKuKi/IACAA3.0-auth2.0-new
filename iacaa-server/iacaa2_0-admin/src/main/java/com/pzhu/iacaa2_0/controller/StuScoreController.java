package com.pzhu.iacaa2_0.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.pzhu.iacaa2_0.common.ActionResult;
import com.pzhu.iacaa2_0.easyexcel.NoModelDataListener;
import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entity.StuScore;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.entityVo.FileUploadVo;
import com.pzhu.iacaa2_0.entityVo.StuScoreVo;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import com.pzhu.iacaa2_0.service.IStuScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-21
 */
@RestController
@RequestMapping("/stuScore")
@NeedAuth("StuScore")
public class StuScoreController {
    @Autowired
    IStuScoreService stuScoreService;

    @Autowired
    ICheckLinkService checkLinkService;

    @RequestMapping("list")
    @AuthResource(scope = "list", name = "学生成绩列表")
    public ActionResult list(@RequestBody StuScore stuScore){
        List<StuScore> stuScoreList = stuScoreService.list(stuScore);
        return ActionResult.ofSuccess(stuScoreList);
    }

    @RequestMapping("delete")
    @AuthResource(scope = "delete", name = "删除学生成绩")
    public ActionResult delete(@RequestBody StuScore stuScore){
        stuScoreService.removeById(stuScore.getId());
        return ActionResult.ofSuccess();
    }

    @RequestMapping("saveOrUpdate")
    @AuthResource(scope = "saveOrUpdate", name = "保存或更新学生成绩列表")
    public ActionResult saveOrUpdate(@RequestBody List<StuScore> stuScoreList){
        if(stuScoreList.isEmpty()){
            return ActionResult.ofFail("不能为空数据");
        }
        CheckLink checkLink = checkLinkService.getById(stuScoreList.get(0).getCheckLinkId());
        Map<String,String> check = new HashMap<>();
        for (StuScore stuScore : stuScoreList) {
            if(check.get(stuScore.getStuno()) == null){
                check.put(stuScore.getStuno(),"exsist");
            }else {
                return ActionResult.ofFail(String.format("已存在学生%S此考核环节分数",stuScore.getStuno()));
            }
            if(stuScore.getScore() == null || stuScore.getScore() < 0){
                return ActionResult.ofFail(String.format("学生%S成绩不合法数",stuScore.getStuno()));
            }
            if(stuScore.getStuno() == null || stuScore.getStuno().length() != 12){
                return ActionResult.ofFail(String.format("学号%S不合法",stuScore.getStuno()));
            }
            if (stuScore.getScore() > checkLink.getTargetScore()+0.00000001){
                return ActionResult.ofFail(String.format("学生%S成绩不得大于考核环节目标成绩:%S",stuScore.getStuno(),checkLink.getTargetScore()));
            }

            stuScore.setMixScore(stuScore.getScore()/checkLink.getTargetScore());
            if(stuScore.getId() == null){
                stuScore.setCreatedDate(LocalDateTime.now());
            }
            stuScore.setUpdateDate(LocalDateTime.now());
        }
        boolean b = stuScoreService.saveOrUpdateBatch(stuScoreList);
        Boolean aBoolean = stuScoreService.summaryCheckLinkScoreById(checkLink.getId());
        return b && aBoolean ? ActionResult.ofSuccess() : ActionResult.ofFail("保存失败");
    }

    @RequestMapping("/exportTemplate")
    @AuthResource(scope = "exportTemplate", name = "成绩导入模板")
    public void exportTemplate(HttpServletResponse response, @RequestBody StuScoreVo stuScoreVo) throws IOException {
        CheckLinkVo vo = new CheckLinkVo();
        vo.setYear(stuScoreVo.getYear());
        vo.setCourseId(stuScoreVo.getCourseId());
        List<CheckLink> list1 = checkLinkService.list(vo);
        try {
            // 所有行的集合
            List<List<Object>> list = new ArrayList<>();
            ExcelWriter excelWriter = EasyExcelFactory.getWriter(response.getOutputStream());
            // 表单
            Sheet sheet = new Sheet(1,0);
            sheet.setSheetName("Sheet1");
            // 创建一个表格
            Table table = new Table(1);
            // 动态添加 表头 headList --> 所有表头行集合
            List<List<String>> headList = new ArrayList<>();
            List<String> headTitle = new ArrayList<>();
            headTitle.add("学生学号");
            headList.add(headTitle);
            list1.forEach(i -> {
                // 第 n 行 的表头
                List<String> headTitle0 = new ArrayList<>();
                headTitle0.add(i.getName() + "成绩");
                headList.add(headTitle0);
            });
            table.setHead(headList);
            excelWriter.write1(list,sheet,table);
            // 记得 释放资源
            excelWriter.finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/importScore")
    @AuthResource(scope = "importScore", name = "导入课程成绩")
    public ActionResult importScore(FileUploadVo fileUploadVo) throws IOException {
        // 1判断文件是否为空
        if (fileUploadVo.getFile().isEmpty()) {
            return ActionResult.ofFail("空文件");
        }
        // 2获取文件名
        String fileName = fileUploadVo.getFile().getOriginalFilename();
        // 3获取后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 4路径
        String filePath = "C://" + fileName;
        File dest = new File(filePath);
        // 5判断目录是否存在
        if (!dest.getParentFile().exists()) {
            // 不存在，创建
            dest.getParentFile().mkdirs();
        }
        try {
            fileUploadVo.getFile().transferTo(dest);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(filePath, new NoModelDataListener(
                checkLinkService,
                stuScoreService,
                fileUploadVo.getCourse().getId(),
                fileUploadVo.getYear())).sheet().doRead();
        return ActionResult.ofSuccess();
    }
}
