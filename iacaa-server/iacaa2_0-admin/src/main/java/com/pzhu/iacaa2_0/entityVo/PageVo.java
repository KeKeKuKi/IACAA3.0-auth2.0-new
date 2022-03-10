package com.pzhu.iacaa2_0.entityVo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author MrZhao
 */
@Data
public class PageVo {
    Integer pageNum = 1;
    Integer pageSize = 20;
}
