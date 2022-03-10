package com.pzhu.iacaa2_0.entityVo;

import com.pzhu.iacaa2_0.entity.StuEvaluation;
import lombok.Data;

/**
 * @author ZhaoZezhong
 * @version V1.0
 * @Title: StuEvaluationVo
 * @Description: Company:成都平凡谷科技有限责任公司
 * @date 2021/4/6 15:09
 */
@Data
public class StuEvaluationVo extends StuEvaluation {
    CourseTaskVo courseTaskVo;
}
