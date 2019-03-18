package com.jie.space.zipkinclient2.utils;

import lombok.Cleanup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by huangmingjie on 2017/10/17.
 */
@Component
public class YklyRestTemplate {
    private static final Log logger = LogFactory.getLog(YklyRestTemplate.class);
    private static final Logger ogLog = LoggerFactory.getLogger("bis_outgoing_log");
    private final String DEF_ENCODING_UTF_8 = "UTF-8";
    @Autowired
    @Qualifier("lRestTemplate")
    private RestTemplate lRestTemplate;
    @Autowired
    @Qualifier("sRestTemplate")
    private RestTemplate sRestTemplate;

    public String lGet(String url, Map<String, String> headerMap, Map<String, String> reqMap) {
        ResponseEntity<byte[]> response =
                httpGet(url, headerMap, reqMap, byte[].class, lRestTemplate);
        return getHttpResponseContent(response);
    }

    public String lGet(HttpServletRequest request, String url, Map<String, String> headerMap, Map<String, String> reqMap) {
        ResponseEntity<byte[]> response =
                httpGet(request, url, headerMap, reqMap, byte[].class, lRestTemplate);
        return getHttpResponseContent(response);
    }

    public ResponseEntity<byte[]> lGet(String url, Map<String, String> headerMap,
                                       Map<String, String> reqMap, Class<byte[]> responseType) {
        return httpGet(url, headerMap, reqMap, responseType, lRestTemplate);

    }

    public String lPost(String url, Map<String, String> headerMap, Map<String, String> reqMap) {
        ResponseEntity<byte[]> response =
                httpPost(url, headerMap, reqMap, byte[].class, lRestTemplate);
        return getHttpResponseContent(response);
    }

    public ResponseEntity<byte[]> lPost(String url, Map<String, String> headerMap,
                                        Map<String, String> reqMap, Class<byte[]> responseType) {
        return httpPost(url, headerMap, reqMap, responseType, lRestTemplate);
    }

    public String sGet(String url, Map<String, String> headerMap, Map<String, String> reqMap) {
        ResponseEntity<byte[]> response =
                httpGet(url, headerMap, reqMap, byte[].class, sRestTemplate);
        return getHttpResponseContent(response);
    }

    public ResponseEntity<byte[]> sGet(String url, Map<String, String> headerMap,
                                       Map<String, String> reqMap, Class<byte[]> responseType) {
        return httpGet(url, headerMap, reqMap, responseType, sRestTemplate);
    }

    public String sPost(String url, Map<String, String> headerMap, Map<String, String> reqMap) {
        ResponseEntity<byte[]> response =
                httpPost(url, headerMap, reqMap, byte[].class, sRestTemplate);
        return getHttpResponseContent(response);

    }

    public ResponseEntity<byte[]> sPost(String url, Map<String, String> headerMap,
                                        Map<String, String> reqMap, Class<byte[]> responseType) {
        return httpPost(url, headerMap, reqMap, responseType, sRestTemplate);
    }

    public String sPostBody(String url, Map<String, String> headerMap, String body) {
        ResponseEntity<byte[]> response =
                httpPostBody(url, headerMap, body, byte[].class, sRestTemplate);
        return getHttpResponseContent(response);
    }

    public String lPostBody(String url, Map<String, String> headerMap, String body) {
        ResponseEntity<byte[]> response =
                httpPostBody(url, headerMap, body, byte[].class, lRestTemplate);
        return getHttpResponseContent(response);
    }

    public String lPostBody(HttpServletRequest request, String url, Map<String, String> headerMap, String body) {
        ResponseEntity<byte[]> response =
                httpPostBody(request, url, headerMap, body, byte[].class, lRestTemplate);
        return getHttpResponseContent(response);
    }

    public ResponseEntity<byte[]> sPost(String url, Map<String, String> headerMap, String body,
                                        Class<byte[]> responseType) {
        return httpPostBody(url, headerMap, body, responseType, sRestTemplate);
    }

    public ResponseEntity<byte[]> lPost(String url, Map<String, String> headerMap, String body,
                                        Class<byte[]> responseType) {
        return httpPostBody(url, headerMap, body, responseType, lRestTemplate);
    }

    /**
     * httpPost body
     *
     * @param url
     * @param headerMap
     * @param body
     * @param responseType
     * @param restTemplate
     * @return
     */
    private ResponseEntity<byte[]> httpPostBody(String url, Map<String, String> headerMap,
                                                String body, Class<byte[]> responseType, RestTemplate restTemplate) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put("Content-Type", "application/json");
            HttpHeaders headers = new HttpHeaders();
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }
            body = body == null ? "" : body;
            HttpEntity<byte[]> entity = new HttpEntity<>(body.getBytes(DEF_ENCODING_UTF_8), headers);
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        } catch (Exception e) {
            logger.error("post error", e);
        } finally {
        }
        return responseEntity;
    }

    /**
     * ttpPost body带request
     *
     * @param request
     * @param url
     * @param headerMap
     * @param body
     * @param responseType
     * @param restTemplate
     * @return
     */
    private ResponseEntity<byte[]> httpPostBody(HttpServletRequest request, String url, Map<String, String> headerMap,
                                                String body, Class<byte[]> responseType, RestTemplate restTemplate) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put("Content-Type", "application/json");
            HttpHeaders headers = new HttpHeaders();
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }

            List<String> cookieList = new ArrayList<String>();
            for (Cookie cookie : request.getCookies()) {
                //System.out.println("当前cookies为:" +  cookie.getDomain() + " " + cookie.getName() + ":" + cookie.getValue());
                cookieList.add(cookie.getName() + "=" + cookie.getValue());
            }
            //System.out.println("cookie为：" + cookieList.toString());
            headers.put(HttpHeaders.COOKIE, cookieList); //将cookie放入header

            body = body == null ? "" : body;
            HttpEntity<byte[]> entity = new HttpEntity<>(body.getBytes(DEF_ENCODING_UTF_8), headers);
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        } catch (Exception e) {
            logger.error("post error", e);
        } finally {
        }
        return responseEntity;
    }

    /**
     * @param url
     * @param headerMap
     * @param reqMap
     * @param responseType
     * @param restTemplate
     * @return
     */
    private ResponseEntity<byte[]> httpPost(String url, Map<String, String> headerMap,
                                            Map<String, String> reqMap, Class<byte[]> responseType, RestTemplate restTemplate) {
        ResponseEntity<byte[]> responseEntity = null;
        try {
            MultiValueMap<String, String> mvMap = new LinkedMultiValueMap<>();
            if (reqMap != null) {
                for (Map.Entry<String, String> e : reqMap.entrySet()) {
                    mvMap.set(e.getKey(), e.getValue());
                }
            }
            HttpHeaders headers = new HttpHeaders();
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(mvMap, headers);
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        } catch (Exception e) {
            logger.error("post error", e);
            throw e;
        } finally {

        }
        return responseEntity;
    }

    /**
     * @param url
     * @param headerMap
     * @param reqMap
     * @param responseType
     * @param restTemplate
     * @return
     */
    private ResponseEntity<byte[]> httpGet(String url, Map<String, String> headerMap,
                                           Map<String, String> reqMap, Class<byte[]> responseType, RestTemplate restTemplate) {
        ResponseEntity<byte[]> responseEntity = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            if (reqMap != null) {
                for (Map.Entry<String, String> rmap : reqMap.entrySet()) {
                    builder.queryParam(rmap.getKey(), rmap.getValue());
                }
            }
            HttpHeaders headers = new HttpHeaders();
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }
            HttpEntity entity = new HttpEntity<>(headers);
            responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, responseType);
        } catch (Exception e) {
            logger.error("get error", e);
            throw e;
        } finally {

        }
        return responseEntity;
    }

    /**
     * 带request的get请求
     *
     * @param request
     * @param url
     * @param headerMap
     * @param reqMap
     * @param responseType
     * @param restTemplate
     * @return
     */
    private ResponseEntity<byte[]> httpGet(HttpServletRequest request, String url, Map<String, String> headerMap,
                                           Map<String, String> reqMap, Class<byte[]> responseType, RestTemplate restTemplate) {
        ResponseEntity<byte[]> responseEntity = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            if (reqMap != null) {
                for (Map.Entry<String, String> rmap : reqMap.entrySet()) {
                    builder.queryParam(rmap.getKey(), rmap.getValue());
                }
            }
            HttpHeaders headers = new HttpHeaders();
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }

            List<String> cookieList = new ArrayList<String>();
            for (Cookie cookie : request.getCookies()) {
                cookieList.add(cookie.getName() + "=" + cookie.getValue());
            }
            headers.put(HttpHeaders.COOKIE, cookieList); //将cookie放入header

            HttpEntity entity = new HttpEntity<>(headers);
            responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, responseType);
        } catch (Exception e) {
            logger.error("get error", e);
            throw e;
        } finally {

        }
        return responseEntity;
    }

    /**
     * @param httpResponse
     * @return
     */
    public String getHttpResponseContent(ResponseEntity<byte[]> httpResponse) {
        if (httpResponse != null) {
            List<String> encodings = httpResponse.getHeaders().get("Content-Encoding");
            List<String> contentTypes = httpResponse.getHeaders().get("Content-Type");
            String charset = DEF_ENCODING_UTF_8;
            // application/json;odata=minimalmetadata;streaming=true;charset=utf-8
            if (contentTypes != null) {
                for (String contentType : contentTypes) {
                    if (contentType.contains("charset")) {
                        String[] types = contentType.split(";");
                        for (String type : types) {
                            String[] kv = type.split("=");
                            if ("charset".equals(kv[0])) {
                                charset = kv[1];
                                break;
                            }
                        }
                    }
                }
            }
            try {
                if (encodings == null) {
                    return httpResponse.getBody() != null ? new String(httpResponse.getBody(), charset) : "";
                }
                boolean flag = false;
                for (String string : encodings) {
                    if (string.equalsIgnoreCase("gzip")) {
                        flag = true;
                    }
                }
                if (flag) {
                    @Cleanup
                    GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(httpResponse.getBody()));
                    try {
                        return StreamUtils.copyToString(gis, Charset.forName(charset));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    return httpResponse.getBody() != null ? new String(httpResponse.getBody(), charset) : "";
                }
            } catch (Exception e) {
                logger.error("getHttpResponseContent error", e);
            }
        }
        return "";
    }

    /**
     * @param e
     * @return
     */
    private String getError(Exception e) {
        if (e instanceof HttpClientErrorException) {
            HttpClientErrorException hceException = (HttpClientErrorException) e;
            return hceException.getResponseBodyAsString();
        } else if (e instanceof HttpServerErrorException) {
            HttpServerErrorException hseException = (HttpServerErrorException) e;
            return hseException.getResponseBodyAsString();
        } else {
            return e.toString();
        }
    }

}
