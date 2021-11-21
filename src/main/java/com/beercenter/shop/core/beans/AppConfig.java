package com.beercenter.shop.core.beans;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
@AllArgsConstructor
public class AppConfig {

    private final ConfigProperties configProperties;

    @Bean
    public WatchService watchService() {
        log.debug("MONITORING_FOLDER: {}", configProperties.getMonitoringfolder());
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(configProperties.getMonitoringfolder());

            if (!Files.isDirectory(path)) {
                throw new RuntimeException("incorrect monitoring folder: " + path);
            }

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;

    }

    @Bean
    public StringBuilder stringBuilder(){

        return new StringBuilder();
    }
}
