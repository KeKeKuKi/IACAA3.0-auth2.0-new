package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.service.ResourceService;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.interfaces.ResourceReceiver;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.model.ResourceDTO;
import com.gapache.security.model.ResourceReportDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/6 6:02 下午
 */
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceReceiver resourceReceiver;
    private final ResourceService resourceService;

    public ResourceController(ResourceReceiver resourceReceiver, ResourceService resourceService) {
        this.resourceReceiver = resourceReceiver;
        this.resourceService = resourceService;
    }

    @PostMapping("/receiveReportData")
    public JsonResult<Boolean> receiveReportData(@RequestBody ResourceReportDTO reportData) {
        return JsonResult.of(resourceReceiver.receiveReportData(reportData));
    }

    @GetMapping("/findAll")
    @AuthResource(scope = "findAll", name = "查询所有资源")
    public JsonResult<List<ResourceDTO>> findAll() {
        List<ResourceDTO> list = resourceService.findAll();
        return JsonResult.of(list);
    }
}
