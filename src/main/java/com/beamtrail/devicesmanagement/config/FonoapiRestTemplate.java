package com.beamtrail.devicesmanagement.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration("fonoapiRestTemplate")
@EnableConfigurationProperties(FonoapiConfiguration.class)
public class FonoapiRestTemplate {

    @Autowired
    private FonoapiConfiguration config;

    @Bean("fonoapiUrl")
    public String fonoapiUrl() throws MalformedURLException {
        return createUrl(config::getPath);
    }

    @Bean("fonoapiRestTemplate")
    public RestTemplate getFonoapiRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {

        log.debug("Instantiating rest template...");

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        AbstractHttpMessageConverter<Object> converter = new MappingJackson2HttpMessageConverter();
        converter.setDefaultCharset(Charsets.UTF_8);
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        // restTemplate.getInterceptors().add(new RequestInterceptor());

        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory clientHttp() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        log.debug("Setting request factory timeout to {} milliseconds.", config.getTimeout());

        requestFactory.setReadTimeout(config.getTimeout());
        requestFactory.setConnectTimeout(config.getTimeout());

        return requestFactory;
    }

    private String createUrl(Supplier<String> path) throws MalformedURLException {
        return new URL(config.getProtocol(), config.getHost(), path.get()).toString();
    }

}
