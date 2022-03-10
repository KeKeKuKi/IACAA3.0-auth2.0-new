package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.service.CompanyService;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.model.CompanyDTO;
import com.gapache.security.model.ElmUiTreeNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/26 11:13 上午
 */
@RestController
@RequestMapping("/api/company")
@NeedAuth("company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @AuthResource(scope = "scope", name = "创建公司")
    public JsonResult<Long> create(@RequestBody CompanyDTO dto) {
        return JsonResult.of(companyService.create(dto));
    }

    @PutMapping
    @AuthResource(scope = "update", name = "更新公司")
    public JsonResult<Boolean> update(@RequestBody CompanyDTO dto) {
        return JsonResult.of(companyService.update(dto));
    }

    @DeleteMapping("/{id}")
    @AuthResource(scope = "delete", name = "删除公司")
    public JsonResult<Boolean> delete(@PathVariable Long id) {
        return JsonResult.of(companyService.delete(id));
    }

    @GetMapping("/findTree")
    @AuthResource(scope = "findTree", name = "查询公司架构图")
    public JsonResult<List<ElmUiTreeNode>> findTree() {
        return JsonResult.of(companyService.findTree());
    }

    @GetMapping("/findAll")
    @AuthResource(scope = "findAll", name = "查询所有公司")
    public JsonResult<List<CompanyDTO>> findAll() {
        return JsonResult.of(companyService.findAll());
    }

    @PostMapping("/updateAll")
    @AuthResource(scope = "updateAll", name = "更新所有")
    public JsonResult<Boolean> updateAll(@RequestBody List<ElmUiTreeNode> nodes) {
        return JsonResult.of(companyService.updateAll(nodes));
    }
}
