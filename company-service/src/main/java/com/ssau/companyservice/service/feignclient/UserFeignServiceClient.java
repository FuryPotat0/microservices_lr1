package com.ssau.companyservice.service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        path = "/api/users",
        url = "http://localhost:8081"
)
public interface UserFeignServiceClient {
    @GetMapping("/{userId}/username")
    String getUserNameById(@PathVariable("userId") Long userId);

    @GetMapping("/exists-by-id/{userId}")
    Boolean existUserById(@PathVariable("userId") Long userId);
}
