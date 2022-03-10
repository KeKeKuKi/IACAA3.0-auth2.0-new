package com.pzhu.iacaa2_0.service.impl;

import com.pzhu.iacaa2_0.entity.StuEvaluation;
import com.pzhu.iacaa2_0.entityVo.StuEvaluationStatisticsVo;
import com.pzhu.iacaa2_0.mapper.StuEvaluationMapper;
import com.pzhu.iacaa2_0.service.IStuEvaluationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-06
 */
@Service
public class StuEvaluationServiceImpl extends ServiceImpl<StuEvaluationMapper, StuEvaluation> implements IStuEvaluationService {

    @Override
    public List<StuEvaluationStatisticsVo> statisticsByCourseTaskId(Long id) {
        List<StuEvaluationStatisticsVo> list = baseMapper.statisticsByCourseTaskId(id);
        if(list == null ||list.size() < 5){
            List<StuEvaluationStatisticsVo> newList = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                StuEvaluationStatisticsVo vo = new StuEvaluationStatisticsVo();
                vo.setScore(i+1);
                vo.setCount(0);
                newList.add(vo);
            }
            list.forEach(i -> {
                newList.get(i.getScore()-1).setCount(i.getCount());
            });
            return newList;
        }
        return list;
    }
}
