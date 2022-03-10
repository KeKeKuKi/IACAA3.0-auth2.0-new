package com.pzhu.iacaa2_0.service.impl;

import com.pzhu.iacaa2_0.entity.CourseTarget;
import com.pzhu.iacaa2_0.entityVo.CourseTargetVo;
import com.pzhu.iacaa2_0.mapper.CourseTargetMapper;
import com.pzhu.iacaa2_0.service.ICourseTargetService;
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
public class CourseTargetServiceImpl extends ServiceImpl<CourseTargetMapper, CourseTarget> implements ICourseTargetService {

    @Override
    public List<CourseTargetVo> volist(CourseTargetVo vo) {
        return baseMapper.volist(vo);
    }

    @Override
    public Boolean removeByTargetId(Long id) {
        return baseMapper.removeByTargetId(id) >= 0;
    }
}
