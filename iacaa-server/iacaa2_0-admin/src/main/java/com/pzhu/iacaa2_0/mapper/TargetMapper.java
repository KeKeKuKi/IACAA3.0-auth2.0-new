package com.pzhu.iacaa2_0.mapper;

import com.pzhu.iacaa2_0.entity.Target;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.iacaa2_0.entityVo.TargetVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface TargetMapper extends BaseMapper<Target> {
    List<Target> getByReqId(Long id);

    Integer summaryByTargetId(Long id);

    List<Target> list(TargetVo vo);

    Integer removeByReqId(Long id);
}
