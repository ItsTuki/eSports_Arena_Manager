package com.example.authservice.config;

import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class HateoasLinkAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (!path.startsWith("/api/v1/")) {
            return body;
        }

        String self = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUriString();
        response.getHeaders().add(HttpHeaders.LINK, Link.of(self).withSelfRel().toString());

        String collectionPath = path.replaceFirst("/\\d+($|/.*)", "");
        if (!collectionPath.equals(path)) {
            String collection = ServletUriComponentsBuilder.fromCurrentContextPath().path(collectionPath).build().toUriString();
            response.getHeaders().add(HttpHeaders.LINK, Link.of(collection).withRel("collection").toString());
        }
        return body;
    }
}

