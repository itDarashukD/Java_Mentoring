package com.example.security.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;


@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPT = 3;
    private static final Duration EXPIRED_AFTER = Duration.ofMinutes(2);
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        CacheLoader<String, Integer> loader = new CacheLoader<>() {
	   public Integer load(String key) {
	       return 0;
	   }
        };
        attemptsCache = CacheBuilder.newBuilder().
	       expireAfterWrite(EXPIRED_AFTER)
	       .build(loader);
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts = 0;
        try {
	   attempts = attemptsCache.get(key);
        } catch (ExecutionException ignored) {//?
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
	   return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
	   return false;
        }
    }

    public LoadingCache<String, Integer> getAttemptsCache() {
        return attemptsCache;
    }

}
