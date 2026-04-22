package com.uditgoel.groomify.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StreamUtils;

public class RestClientInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		traceRequest(request, body);
		long start = System.currentTimeMillis();
		ClientHttpResponse response = execution.execute(request, body);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("Call Duration %s", System.currentTimeMillis() - start));
		}
		traceResponse(response);
		return response;
	}

	@Async
	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("<<======request begin=========");
			LOGGER.info("<< URI         : {}", request.getURI());
			LOGGER.info("<< Method      : {}", request.getMethod());
			LOGGER.info("<< Headers     : {}", request.getHeaders());
			LOGGER.info("<< Request body: {}", new String(body, "UTF-8"));
			LOGGER.info("<<=======request end==========");
		}
	}

	@Async
	private void traceResponse(ClientHttpResponse response) throws IOException {
		String bodyText = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(">>======response begin==========");
			LOGGER.info(">> Status code  : {}", response.getStatusCode());
			LOGGER.info(">> Status text  : {}", response.getStatusText());
			LOGGER.info(">> Headers      : {}", response.getHeaders());
			LOGGER.info(">> Response body: {}", bodyText);
			LOGGER.info(">>=====response end=============");
		}
	}
}
