package com.pzhu.iacaa2_0.service;

import com.pzhu.iacaa2_0.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.CourseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface ICourseService extends IService<Course> {

    /**
     * 方法用于获取CourseVo列表
     *
     * @Author ZhaoZezhong
     * @Param: Course
     * @Return: List<CourseVo>
     * @Create: 2021/1/20 11:09
     */
    List<CourseVo> voList(CourseVo vo);

    List<Course> list(CourseVo vo);

}
