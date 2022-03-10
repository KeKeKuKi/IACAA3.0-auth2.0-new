package com.pzhu.iacaa2_0.mapper;

import com.pzhu.iacaa2_0.entity.StuScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-21
 */
public interface StuScoreMapper extends BaseMapper<StuScore> {

    List<StuScore> list(StuScore stuScore);

    int summaryByCheckLinkId(Long id);

    Boolean summaryCheckLinkScoreById(Long id);
}
