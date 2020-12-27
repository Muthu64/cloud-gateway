package com.movie.cloud.gateway.configuration;

import java.util.function.Function;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration
{
    private static final String MOVIE_CATALOG_SERVICE_ID = "movie-catalog-service";
    private static final String MOVIE_INFO_SERVICE_ID = "movie-info-service";
    private static final String MOVIE_RATING_SERVICE_ID = "movie-rating-service";

    private static final String MOVIE_CATALOG_SERVICE_URL_PATTERN = "/catalog/**";
    private static final String MOVIE_INFO_SERVICE_URL_PATTERN = "/info/**";
    private static final String MOVIE_RATING_SERVICE_URL_PATTERN = "/rating/**";

    private static final String LOAD_BALANCE_TEXT = "lb://";

    private static final String MOVIE_CATALOG_SERVICE_URI = LOAD_BALANCE_TEXT + MOVIE_CATALOG_SERVICE_ID;
    private static final String MOVIE_INFO_SERVICE_URI = LOAD_BALANCE_TEXT + MOVIE_INFO_SERVICE_ID;
    private static final String MOVIE_RATING_SERVICE_URI = LOAD_BALANCE_TEXT + MOVIE_RATING_SERVICE_ID;
    private static final String HYSTRIX_CIRCUIT_BREAKER_TEXT = "CircuitBreaker";

    @Bean
    public RouteLocator customRouteLocator( RouteLocatorBuilder builder)
    {
        Function<GatewayFilterSpec, UriSpec> movieCatalogFilterSpecUriSpecFunction = ( gatewayFilterSpec ) -> gatewayFilterSpec.hystrix( config -> config.setName( HYSTRIX_CIRCUIT_BREAKER_TEXT ).setFallbackUri( "forward:/movieCatalogFallBack" ) );
        Function<GatewayFilterSpec, UriSpec> movieInfoFilterSpecUriSpecFunction = ( gatewayFilterSpec ) -> gatewayFilterSpec.hystrix( config -> config.setName( HYSTRIX_CIRCUIT_BREAKER_TEXT ).setFallbackUri( "forward:/movieInfoFallBack" ) );
        Function<GatewayFilterSpec, UriSpec> movieRatingFilterSpecUriSpecFunction = ( gatewayFilterSpec ) -> gatewayFilterSpec.hystrix( config -> config.setName( HYSTRIX_CIRCUIT_BREAKER_TEXT ).setFallbackUri( "forward:/movieRatingFallBack" ) );

        return builder.routes()
                .route( MOVIE_CATALOG_SERVICE_ID, predicateSpec -> predicateSpec.path( MOVIE_CATALOG_SERVICE_URL_PATTERN ).filters( movieCatalogFilterSpecUriSpecFunction ).uri( MOVIE_CATALOG_SERVICE_URI ) )
                .route( MOVIE_INFO_SERVICE_ID, predicateSpec -> predicateSpec.path( MOVIE_INFO_SERVICE_URL_PATTERN ).filters( movieInfoFilterSpecUriSpecFunction ).uri( MOVIE_INFO_SERVICE_URI ) )
                .route( MOVIE_RATING_SERVICE_ID, predicateSpec -> predicateSpec.path( MOVIE_RATING_SERVICE_URL_PATTERN ).filters( movieRatingFilterSpecUriSpecFunction ).uri( MOVIE_RATING_SERVICE_URI ) )
                .build();
    }

}
