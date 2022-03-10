package com.pzhu.iacaa2_0.service.impl;

import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.mapper.CheckLinkMapper;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.iacaa2_0.service.IStuScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CheckLinkServiceImpl extends ServiceImpl<CheckLinkMapper, CheckLink> implements ICheckLinkService {
    @Autowired
    IStuScoreService stuScoreService;

    @Override
    public List<CheckLink> list(CheckLinkVo vo) {
        return baseMapper.list(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean summaryByCourseTaskID(Long id,Integer year) {
//        stuScoreService.summaryAllCheckLinksScore(year);
        return baseMapper.summaryByCourseTaskId(id) >= 0;
    }

    @Override
    public List<CheckLink> listBySourseTask(Long id) {
        return baseMapper.listBySourseTask(id);
    }

    @Override
    public void coverNullToZero() {
        baseMapper.coverNullToZero();
    }
}
