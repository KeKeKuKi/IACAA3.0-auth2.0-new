package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.service.PositionService;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.model.ElmUiTreeNode;
import com.gapache.security.model.PositionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/25 5:03 下午
 */
@RestController
@RequestMapping("/api/position")
@NeedAuth("position")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    @AuthResource(scope = "create", name = "创建职位")
    public JsonResult<Long> create(@RequestBody PositionDTO dto) {
        return JsonResult.of(positionService.create(dto));
    }

    @PutMapping
    @AuthResource(scope = "update", name = "更新职位")
    public JsonResult<Boolean> update(@RequestBody PositionDTO dto) {
        return JsonResult.of(positionService.update(dto));
    }

    @PostMapping("/updateAll")
    @AuthResource(scope = "updateAll", name = "更新所有")
    public JsonResult<Boolean> updateAll(@RequestBody List<ElmUiTreeNode> nodes) {
        return JsonResult.of(positionService.updateAll(nodes));
    }

    @DeleteMapping("/{id}")
    @AuthResource(scope = "delete", name = "删除职位")
    public JsonResult<Boolean> delete(@PathVariable Long id) {
        return JsonResult.of(positionService.delete(id));
    }

    @GetMapping("/findTree/{companyId}")
    @AuthResource(scope = "findTree", name = "查询出组织架构图")
    public JsonResult<List<ElmUiTreeNode>> findTree(@PathVariable Long companyId) {
        return JsonResult.of(positionService.findTree(companyId));
    }

    @GetMapping("/findAll")
    @AuthResource(scope = "findAll", name = "查询所有的职位")
    public JsonResult<List<PositionDTO>> findAll() {
        return JsonResult.of(positionService.findAll(false,null));
    }
}
