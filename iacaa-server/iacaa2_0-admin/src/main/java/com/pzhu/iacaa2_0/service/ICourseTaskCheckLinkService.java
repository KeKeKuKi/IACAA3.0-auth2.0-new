package com.pzhu.iacaa2_0.service;

import com.pzhu.iacaa2_0.entity.CourseTaskCheckLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.CourseTaskCheckLinkVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-04-21
 */
public interface ICourseTaskCheckLinkService extends IService<CourseTaskCheckLink> {

    List<CourseTaskCheckLinkVo> voList(CourseTaskCheckLinkVo vo);
}
