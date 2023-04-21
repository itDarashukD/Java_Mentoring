package org.example.lru;

import ch.qos.logback.classic.Logger;
import com.google.common.cache.*;
import org.example.lfu.entity.Model;
import org.example.lfu.lfuCache.LfuCache;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class LruCache {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LfuCache.class);

    public LoadingCache<String, Model> createCache(Long size,Long expireAfter) {
        return CacheBuilder.newBuilder()
                .recordStats()
                .maximumSize(size)
                .concurrencyLevel(2)
                .removalListener(new RemovalListener<String, Model>()
                {
                    @Override
                    public void onRemoval(RemovalNotification<String, Model> removalNotification) {
                        log.info("Removed entry: {}  --> {}", removalNotification.getKey(), removalNotification.getValue());
                        log.info("Cause: {} ", removalNotification.getCause());
                    }
                })
                .expireAfterAccess(expireAfter, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Model>() {

                    @Override
                    public Model load(String name) {
                        return new Model(name);
                        //might return beforehand uploaded values e.g. from 'DB',
                        //also in case of Model not present cache will try to load from 'DB'
                    }
                });
    }
}
