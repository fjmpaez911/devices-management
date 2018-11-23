package com.beamtrail.devicesmanagement.client.fonoapi;

import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.beamtrail.devicesmanagement.config.FonoapiConfiguration;
import com.beamtrail.devicesmanagement.exception.DefinedErrorException;
import com.beamtrail.devicesmanagement.exception.ErrorEnum;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FonoapiClientImpl implements FonoapiClient {

    private static Gson gson = new Gson();

    @Autowired
    private FonoapiConfiguration apiConfig;

    @Autowired
    @Qualifier("fonoapiRestTemplate")
    private RestTemplate template;

    @Autowired
    @Qualifier("fonoapiUrl")
    private String fonoapiUrl;

    @Override
    public FonoapiResponse getDevices(String brand, String device) {

        UriComponentsBuilder builder;
        builder = UriComponentsBuilder.fromHttpUrl(fonoapiUrl);

        HashMap<String, String> params = includeQueryParams(builder, brand, device);

        String response = null;
        String externalErrorCode = null;

        try {

            final String finalUrl = URLDecoder.decode(builder.build().encode().toUriString(),
                    Charsets.UTF_8.name());

            log.info("invoking fonoapi {} - {}", finalUrl, params);

            ResponseEntity<String> externalResponse = template.exchange(finalUrl, HttpMethod.POST,
                    getHttpEntity(), String.class, params);

            response = externalResponse.getBody();

        } catch (RestClientException e) {

            if (e instanceof RestClientResponseException) {
                RestClientResponseException restE = (RestClientResponseException) e;
                externalErrorCode = restE.getResponseBodyAsString();
                log.error("External error: {}", externalErrorCode, e);
            }

            checkTimeoutException(e);

            throw new DefinedErrorException(ErrorEnum.EXTERNAL_ERROR, externalErrorCode, e);

        } catch (Exception e) {

            log.error("error requesting: {}", fonoapiUrl, e);
            throw new DefinedErrorException(ErrorEnum.INTERNAL_SERVER_ERROR, e);
        }

        return parseExternalResponse(response);
    }

    private HashMap<String, String> includeQueryParams(UriComponentsBuilder builder, String brand,
            String device) {

        HashMap<String, String> params = new HashMap<>(3);

        builder.queryParam("token", "{token}");
        params.put("token", apiConfig.getToken());

        if (StringUtils.isNotBlank(brand)) {
            builder.queryParam("brand", "{brand}");
            params.put("brand", brand);
        }

        if (StringUtils.isNotBlank(device)) {
            builder.queryParam("device", "{device}");
            params.put("device", device);
        }

        return params;
    }

    private HttpEntity<?> getHttpEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(null, headers);
    }

    private void checkTimeoutException(Exception e) {

        if (e instanceof RestClientResponseException) {

            RestClientResponseException restE = (RestClientResponseException) e;

            if (HttpStatus.GATEWAY_TIMEOUT.value() == restE.getRawStatusCode()) {
                throw new DefinedErrorException(ErrorEnum.TIMEOUT, fonoapiUrl, e);
            }

        } else if (e instanceof SocketTimeoutException) {

            throw new DefinedErrorException(ErrorEnum.TIMEOUT, fonoapiUrl, e);

        } else {

            Throwable root = getRootCause(e);

            if (root instanceof SocketTimeoutException) {
                throw new DefinedErrorException(ErrorEnum.TIMEOUT, fonoapiUrl, root);
            }
        }
    }

    private Throwable getRootCause(Exception e) {

        Throwable cause = e;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }

    private FonoapiResponse parseExternalResponse(String externalResponse) {

        FonoapiResponse response = new FonoapiResponse();

        try {

            FonoapiDevice[] fonoapiDeviceArray = gson.fromJson(externalResponse,
                    FonoapiDevice[].class);

            response.setDevices(Arrays.asList(fonoapiDeviceArray));

        } catch (Exception e) {

            log.error("error parsing external response", e);

            try {

                FonoapiErrorResponse errorResponse = gson.fromJson(externalResponse,
                        FonoapiErrorResponse.class);

                throw new DefinedErrorException(ErrorEnum.EXTERNAL_ERROR, errorResponse.getStatus(),
                        errorResponse.getMessage(), e);

            } catch (DefinedErrorException e2) {
                throw e2;
            } catch (Exception e2) {
                throw new DefinedErrorException(ErrorEnum.EXTERNAL_ERROR, null, null, e);
            }
        }

        return response;
    }

}
