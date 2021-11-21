package com.beercenter.shop.core.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import static javax.management.timer.Timer.ONE_HOUR;

@Slf4j
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("products");
    }

    @Scheduled(fixedRate = ONE_HOUR * 4)
    @CacheEvict(value = { "products" })
    public void clearCache() {
        log.info("products cache clear");
    }

}
