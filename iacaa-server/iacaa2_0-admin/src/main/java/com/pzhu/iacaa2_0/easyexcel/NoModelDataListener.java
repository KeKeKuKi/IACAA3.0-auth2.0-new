package com.pzhu.iacaa2_0.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.StuScore;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import com.pzhu.iacaa2_0.service.IStuScoreService;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: NoModelDataListener
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/5/149:14
 */
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoModelDataListener.class);

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<Map<Integer, String>> list = new ArrayList<>();
    private List<Map<Integer, Map<Integer, String>>> list1 = new ArrayList<>();
    private Long saveCourseId;
    private Integer year;
    private ICheckLinkService checkLinkService;
    private IStuScoreService stuScoreService;

    public NoModelDataListener(ICheckLinkService checkLinkService,IStuScoreService stuScoreService, Long id, Integer year) {
        this.saveCourseId = id;
        this.year = year;
        this.checkLinkService = checkLinkService;
        this.stuScoreService = stuScoreService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        Map<Integer, Map<Integer, String>> map = new HashMap<>();
        map.put(context.readRowHolder().getRowIndex(), headMap);
        list1.add(map);
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    @Transactional
    public void saveData() {
        Map<Integer, String> headMap = list1.get(0).get(0);
        Set<Map.Entry<Integer, String>> entries = headMap.entrySet();
        CheckLinkVo checkLink = new CheckLinkVo();
        checkLink.setCourseId(this.saveCourseId);
        checkLink.setYear(this.year);
        List<CheckLink> checkLinks = checkLinkService.list(checkLink);

        list.forEach(map -> {
            int size = map.size();
            for (int i = 1; i < size; i++) {
                StuScore stuScore = new StuScore();
                stuScore.setScore(Double.parseDouble(map.get(i)));
                stuScore.setCheckLinkId(checkLinks.get(i-1).getId().intValue());
                stuScore.setStuno(map.get(0));
                stuScore.setMixScore(stuScore.getScore()/checkLinks.get(i-1).getTargetScore());
                stuScore.setCreatedDate(LocalDateTime.now());
                stuScore.setUpdateDate(LocalDateTime.now());
                stuScoreService.save(stuScore);
            }
        });

        checkLinks.forEach(i -> {
            stuScoreService.summaryCheckLinkScoreById(i.getId());
        });
    }
}
