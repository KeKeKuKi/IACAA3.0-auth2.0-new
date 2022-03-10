package com.pzhu.iacaa2_0.service.impl;

import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.StuScore;
import com.pzhu.iacaa2_0.entityVo.CheckLinkVo;
import com.pzhu.iacaa2_0.mapper.StuScoreMapper;
import com.pzhu.iacaa2_0.service.ICheckLinkService;
import com.pzhu.iacaa2_0.service.IStuScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2021-04-21
 */
@Service
public class StuScoreServiceImpl extends ServiceImpl<StuScoreMapper, StuScore> implements IStuScoreService {
    @Autowired
    ICheckLinkService checkLinkService;

    @Override
    public List<StuScore> list(StuScore stuScore) {
        return baseMapper.list(stuScore);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean summaryAllCheckLinksScore(Integer year) {
        CheckLinkVo vo = new CheckLinkVo();
        vo.setYear(year);
        List<CheckLink> list = checkLinkService.list(vo);
        list.forEach(checkLink -> {
            baseMapper.summaryByCheckLinkId(checkLink.getId());
        });
        checkLinkService.coverNullToZero();
        return null;
    }

    @Override
    public Boolean summaryCheckLinkScoreById(Long id) {
        return baseMapper.summaryCheckLinkScoreById(id);
    }
}
