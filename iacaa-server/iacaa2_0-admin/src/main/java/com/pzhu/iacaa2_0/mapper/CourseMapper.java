package com.pzhu.iacaa2_0.mapper;

import com.pzhu.iacaa2_0.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.iacaa2_0.entityVo.CourseVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 根据条件获取List
     *
     * @Author ZhaoZezhong
     * @Param: Course
     * @Return: List<Course>
     * @Create: 2021/1/20 11:16
     */
    List<Course> fuzzyQuery(CourseVo vo);

    List<CourseVo> voList(CourseVo vo);

    List<Course> list(CourseVo vo);
}
