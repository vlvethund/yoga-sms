package com.mindeulle.yoga.calendar.configure;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
                .build();

        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient
//                                        .create()
//                                        .secure(
//                                                ThrowingConsumer.unchecked(
//                                                        sslContextSpec -> sslContextSpec.sslContext(
//                                                                SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
//                                                        )
//                                                )
//                                        )
//                                        .tcpConfiguration(
//                                                client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
//                                                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
//                                                                .addHandlerLast(new WriteTimeoutHandler(180))
//                                                        )
//                                        )
//                        )
//                )
//                .exchangeStrategies(exchangeStrategies)
                .baseUrl("https://sens.apigw.ntruss.com/sms")
                .build();

    }

}
