package io.everyonecodes.project.movie_recommendations.communication.client;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

@SuppressWarnings("UnstableApiUsage")
@Component
public class RequestController {
    private static final int REQUESTS_PER_SECOND = 40;
    private RateLimiter rateLimiter;

    public RequestController() {
        rateLimiter = RateLimiter.create(REQUESTS_PER_SECOND);
    }

    public void makeRequest() {
        rateLimiter.acquire();
    }
}
