package com.pzhu.iacaa2_0.service.impl;

import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entityVo.CourseVo;
import com.pzhu.iacaa2_0.mapper.CourseMapper;
import com.pzhu.iacaa2_0.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Override
    public List<CourseVo> voList(CourseVo vo) {
        return baseMapper.voList(vo);
    }

    @Override
    public List<Course> list(CourseVo vo) {
        return baseMapper.list(vo);
    }
}
