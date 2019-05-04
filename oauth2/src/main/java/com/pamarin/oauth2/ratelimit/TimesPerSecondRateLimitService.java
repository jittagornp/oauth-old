/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.oauth2.ratelimit;

import com.pamarin.oauth2.exception.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import java.time.Duration;
import org.springframework.util.Assert;

/**
 *
 * @author jitta
 */
public class TimesPerSecondRateLimitService implements RateLimitService {

    private final int times;
    private final TokenBucketRepository tokenBucketRepository;

    public TimesPerSecondRateLimitService(int times, TokenBucketRepository tokenBucketRepository) {
        Assert.notNull(tokenBucketRepository, "require tokenBucketRepository.");
        this.times = times;
        this.tokenBucketRepository = tokenBucketRepository;
    }

    public TimesPerSecondRateLimitService(int times) {
        this(times, new InMemoryTokenBucketRepository());
    }

    @Override
    public TokenBucketRepository getTokenBucketRepository() {
        return tokenBucketRepository;
    }

    @Override
    public Bucket createTokenBucket() {
        // define the limit x times per 1 second
        Bandwidth limit = Bandwidth.simple(times, Duration.ofSeconds(1));
        // construct the bucket
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void limit(String key) {
        Bucket bucket = tokenBucketRepository.find(key);
        if (bucket == null) {
            bucket = createTokenBucket();
            tokenBucketRepository.save(key, bucket);
        }

        if (!bucket.tryConsume(1)) {
            throw new RateLimitException("\"" + key + "\" is over rate limit");
        }
    }

}
