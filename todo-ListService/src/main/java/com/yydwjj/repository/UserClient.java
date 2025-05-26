package com.yydwjj.repository;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("userserver")
public interface UserClient {
}
