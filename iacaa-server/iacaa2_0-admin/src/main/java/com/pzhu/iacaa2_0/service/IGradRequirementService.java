package com.pzhu.iacaa2_0.service;

import com.pzhu.iacaa2_0.entity.GradRequirement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.iacaa2_0.entityVo.GradRequirementVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZhaoZezhong
 * @since 2021-01-15
 */
public interface IGradRequirementService extends IService<GradRequirement> {
    /**
     * 使用实体进行匹配查询
     *
     * @Author ZhaoZezhong
     * @Param: GradRequirement
     * @Return: List<GradRequirement>
     * @Create: 2021/1/22 16:50
     */
    List<GradRequirement> list(GradRequirementVo vo);

    List<GradRequirementVo> voList(GradRequirementVo vo);

    Boolean summaryThisYearReqGrade(Integer year);

    Boolean removeList(List<Long> ids);
}
