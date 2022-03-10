package com.pzhu.iacaa2_0.mapper;

import com.pzhu.iacaa2_0.entity.StuEvaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.iacaa2_0.entityVo.StuEvaluationStatisticsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-06
 */
public interface StuEvaluationMapper extends BaseMapper<StuEvaluation> {

    List<StuEvaluationStatisticsVo> statisticsByCourseTaskId(Long id);
}
