package com.gapache.security.reporter;

import com.gapache.commons.model.JsonResult;
import com.gapache.security.interfaces.ResourceReporter;
import com.gapache.security.model.ResourceReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * 远程上报资源
 *
 * @author HuSen
 * @since 2020/8/6 5:46 下午
 */
@Slf4j
public class RemoteResourceReporter implements ResourceReporter {

    private final String serverName;

    private final RestTemplate restTemplate;

    private static final int REPEAT = 3;

    public RemoteResourceReporter(String serverName, RestTemplate restTemplate) {
        this.serverName = serverName;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean reporting(ResourceReportDTO resourceReportDTO) {
        @SuppressWarnings("rawtypes")
        JsonResult result;
        try {
            result = getResult(resourceReportDTO);
            if (result != null && result.getData() != null) {
                return (boolean) result.getData();
            }
        } catch (Exception e) {
            for (int i = 0; i < REPEAT; i++) {
                log.info("repeat {} times reporting resource", i + 1);
                result = getResult(resourceReportDTO);
                if (result != null && result.getData() != null) {
                    return (boolean) result.getData();
                }
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    private JsonResult getResult(ResourceReportDTO resourceReportDTO) {
        return restTemplate.postForObject("http://" + serverName + "/api/resource/receiveReportData", resourceReportDTO, JsonResult.class);
    }
}
