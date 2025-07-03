package org.springframework.ai.mcp.sample.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class HttpTests {

    static {
        // Logback 설정
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 인코더 설정
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.start();

        // ConsoleAppender 설정
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        // Root 로거 설정
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.OFF);
        rootLogger.addAppender(consoleAppender);

        // org.apache.http.wire
        Logger wire = loggerContext.getLogger("org.apache.http.wire");
        rootLogger.addAppender(consoleAppender);
        wire.setLevel(Level.DEBUG);
    }
    static Logger logger = (Logger) LoggerFactory.getLogger(HttpTests.class);

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://api.fireworks.ai/inference/v1/chat/completions");
        post.setHeader("accept-encoding", "gzip, deflate");
        post.setHeader("connection", "keep-alive");
        post.setHeader("accept", "application/json");
        post.setHeader("content-Type", "application/json");
        post.setHeader("user-agent", "OpenAI/Python 1.91.0");
        post.setHeader("authorization", "Bearer fw_3ZXMucVdUFeSQiKyBYhcA47i");
        post.setHeader("x-stainless-lang", "python");
        post.setHeader("x-stainless-package-version", "1.91.0");
        post.setHeader("x-stainless-os", "MacOS");
        post.setHeader("x-stainless-arch", "arm64");
        post.setHeader("x-stainless-runtime", "CPython");
        post.setHeader("x-stainless-runtime-version", "3.12.11");
        post.setHeader("x-stainless-async", "false");
        post.setHeader("x-stainless-retry-count", "0");
        post.setHeader("x-stainless-read-timeout", "600");
        post.setEntity(new StringEntity("{\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"한국의 수도는 어디야?\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"model\": \"accounts/fireworks/models/llama4-maverick-instruct-basic\"\n" +
                "}\n"));
        HttpResponse response = httpClient.execute(post);
        String res = EntityUtils.toString(response.getEntity());
        logger.info(">>> {}", res);
        httpClient.close();
    }
}
