package com.pzhu.iacaa2_0.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pzhu.iacaa2_0.entity.CourseTarget;
import com.pzhu.iacaa2_0.entity.Target;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.TargetVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface ITargetService extends IService<Target> {
    Boolean summaryThisYearTargetsGrade(Integer year);

    List<Target> list(TargetVo vo);

    Boolean removeByReqId(Long id);

}
