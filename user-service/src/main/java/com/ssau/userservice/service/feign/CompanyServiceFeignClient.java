package com.ssau.userservice.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "company-service",
        path = "/api/companies",
        url = "http://localhost:8080"
)
public interface CompanyServiceFeignClient {
    @GetMapping("/exist-by-id/{companyId}")
    Boolean existById(@PathVariable("companyId") Long companyId);

    @GetMapping("/{companyId}/name")
    String getCompanyNameById(@PathVariable("companyId") Long companyId);
}
