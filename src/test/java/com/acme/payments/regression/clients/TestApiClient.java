package com.acme.payments.regression.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RequiredArgsConstructor
@Component
public class TestApiClient {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;


    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------

    public MvcResult get(String path) {
        try {
            return mockMvc.perform(
                    MockMvcRequestBuilders.get(path)
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();
        } catch (Exception e) {
            throw requestFailed("GET", path, e);
        }
    }

    public <T> T get(String path, Class<T> responseType) {
        return readResponse(
                get(path),
                responseType
        );
    }

    public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
        return readResponse(
                get(path),
                responseType
        );
    }

    // -------------------------------------------------------------------------
    // POST JSON
    // -------------------------------------------------------------------------

    public MvcResult post(String path, Object body) {
        try {
            return mockMvc.perform(
                    MockMvcRequestBuilders.post(path)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
            ).andReturn();
        } catch (Exception e) {
            throw requestFailed("POST", path, e);
        }
    }

    public <T> T post(
            String path,
            Object body,
            Class<T> responseType) {

        return readResponse(
                post(path, body),
                responseType
        );
    }

    // -------------------------------------------------------------------------
    // POST XML
    // -------------------------------------------------------------------------

    public MvcResult postXml(String path, String xml) {
        try {
            return mockMvc.perform(
                    MockMvcRequestBuilders.post(path)
                            .contentType(MediaType.APPLICATION_XML)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(xml)
            ).andReturn();
        } catch (Exception e) {
            throw requestFailed("POST", path, e);
        }
    }

    public <T> T postXml(
            String path,
            String xml,
            Class<T> responseType) {

        return readResponse(
                postXml(path, xml),
                responseType
        );
    }

    // -------------------------------------------------------------------------
    // PUT
    // -------------------------------------------------------------------------

    public MvcResult put(String path, Object body) {
        try {
            return mockMvc.perform(
                    MockMvcRequestBuilders.put(path)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body))
            ).andReturn();
        } catch (Exception e) {
            throw requestFailed("PUT", path, e);
        }
    }

    public <T> T put(
            String path,
            Object body,
            Class<T> responseType) {

        return readResponse(
                put(path, body),
                responseType
        );
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    public MvcResult delete(String path) {
        try {
            return mockMvc.perform(
                    MockMvcRequestBuilders.delete(path)
                            .accept(MediaType.APPLICATION_JSON)
            ).andReturn();
        } catch (Exception e) {
            throw requestFailed("DELETE", path, e);
        }
    }

    // -------------------------------------------------------------------------
    // Response handling
    // -------------------------------------------------------------------------

    public int status(MvcResult result) {
        return result.getResponse().getStatus();
    }

    public String body(MvcResult result) {
        try {
            return result.getResponse().getContentAsString();
        } catch (Exception e) {
            throw new TestApiException(
                    "Failed to read response body",
                    e
            );
        }
    }

    public <T> T body(
            MvcResult result,
            Class<T> responseType) {

        return readResponse(result, responseType);
    }

    private <T> T readResponse(
            MvcResult result,
            Class<T> responseType) {

        try {
            return objectMapper.readValue(
                    result.getResponse().getContentAsByteArray(),
                    responseType
            );
        } catch (Exception e) {
            throw new TestApiException(
                    "Failed to deserialize API response. " +
                            "Status: " + result.getResponse().getStatus() +
                            ", body: " + safeBody(result),
                    e
            );
        }
    }

    private <T> T readResponse(
            MvcResult result,
            ParameterizedTypeReference<T> responseType) {

        try {
            var javaType = objectMapper.getTypeFactory()
                    .constructType(responseType.getType());

            return objectMapper.readValue(
                    result.getResponse().getContentAsByteArray(),
                    javaType
            );
        } catch (Exception e) {
            throw new TestApiException(
                    "Failed to deserialize API response. " +
                            "Status: " + result.getResponse().getStatus() +
                            ", body: " + safeBody(result),
                    e
            );
        }
    }

    private TestApiException requestFailed(
            String method,
            String path,
            Exception cause) {

        return new TestApiException(
                method + " request failed: " + path,
                cause
        );
    }

    private String safeBody(MvcResult result) {
        try {
            return result.getResponse().getContentAsString();
        } catch (Exception e) {
            return "<unable to read response>";
        }
    }

    public static class TestApiException extends RuntimeException {

        public TestApiException(
                String message,
                Throwable cause) {

            super(message, cause);
        }
    }
}