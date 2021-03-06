package com.netflix.subscription.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.netflix.subscription.core.CacheManager;

@Component
public class CacheRefreshTask {

    private static final Logger log = LoggerFactory.getLogger(CacheRefreshTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        cacheManager.activateScheduledPrices();
        cacheManager.refreshUpdatedPrices();
    }
}