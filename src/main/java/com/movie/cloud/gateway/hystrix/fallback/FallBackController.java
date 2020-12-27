package com.movie.cloud.gateway.hystrix.fallback;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RefreshScope
public class FallBackController
{
    @RequestMapping("/movieCatalogFallBack")
    public Mono<String> movieCatalogServiceFallBack()
    {
        return Mono.just( "movie catalog service is down and please try after some time" );
    }

    @RequestMapping("/movieRatingFallBack")
    public Mono<String> movieRatingServiceFallBack()
    {
        return Mono.just( "movie rating service is down and please try after some time" );
    }

    @RequestMapping("/movieInfoFallBack")
    public Mono<String> movieInfoServiceFallBack()
    {
        return Mono.just( "movie info service is down and please try after some time" );
    }
}
