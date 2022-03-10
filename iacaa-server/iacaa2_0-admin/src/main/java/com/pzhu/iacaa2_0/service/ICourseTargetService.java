package com.pzhu.iacaa2_0.service;

import com.pzhu.iacaa2_0.entity.CourseTarget;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.CourseTargetVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface ICourseTargetService extends IService<CourseTarget> {
    List<CourseTargetVo> volist(CourseTargetVo vo);

    Boolean removeByTargetId(Long id);
}
