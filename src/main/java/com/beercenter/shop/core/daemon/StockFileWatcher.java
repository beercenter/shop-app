package com.beercenter.shop.core.daemon;

import com.beercenter.shop.core.services.StockFileProcessorServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;

@Slf4j
@Component
@AllArgsConstructor
public class StockFileWatcher {

    private final WatchService watchService;
    private final StockFileProcessorServiceImpl stockFileProcessorService;

    @Async
    @PostConstruct
    public void startStockFileWatcher() {
        log.info("START_MONITORING");
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Thread.sleep(2000);
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
                    final Path folder = Path.of(key.watchable().toString());
                    final Path filePath = folder.resolve(event.context().toString());
                    if (StringUtils.isNotEmpty(FilenameUtils.getExtension(filePath.toString())))
                        stockFileProcessorService.processFile(filePath);
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("interrupted exception for monitoring service");
        }
    }

    @PreDestroy
    public void stopStockFileWatcher() {
        log.info("STOP_MONITORING");

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("exception while closing the monitoring service");
            }
        }

    }
}