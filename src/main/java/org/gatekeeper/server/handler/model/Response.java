package org.gatekeeper.server.handler.model;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class Response {
    private ResponseStatus status;
    private List<String> errors;

    public static Response ok() {
        return Response.of(ResponseStatus.SUCCESS, null);
    }

    public static Response fail(String error) {
        return Response.fail(List.of(error));
    }

    public static Response fail(List<String> errors) {
        return Response.of(ResponseStatus.FAILURE, errors);
    }
}
