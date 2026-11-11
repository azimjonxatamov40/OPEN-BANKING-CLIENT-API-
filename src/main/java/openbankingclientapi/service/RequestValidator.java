package openbankingclientapi.service;

import java.util.ArrayList;
import java.util.List;

import openbankingclientapi.dto.ApiRequest;
import openbankingclientapi.exception.ApiException;
import openbankingclientapi.exception.ErrorCode;

public final class RequestValidator {

    private final List<String> missing = new ArrayList<>();

    private RequestValidator() {
    }

    public static RequestValidator of(ApiRequest<?> request) {
        RequestValidator v = new RequestValidator();
        if (request == null) {
            throw new ApiException(ErrorCode.EMPTY_PARAM, List.of("requestId", "dsign", "payload"));
        }
        v.require("requestId", request.requestId());
        v.require("dsign", request.dsign());
        if (request.payload() == null) {
            v.missing.add("payload");
            v.check();
        }
        return v;
    }

    public RequestValidator require(String field, String value) {
        if (isBlank(value)) {
            missing.add(field);
        }
        return this;
    }

    public RequestValidator requireAnyOf(List<String> fields, String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return this;
            }
        }
        missing.addAll(fields);
        return this;
    }

    public void check() {
        if (!missing.isEmpty()) {
            throw new ApiException(ErrorCode.EMPTY_PARAM, List.copyOf(missing));
        }
    }

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static String trimToNull(String s) {
        return isBlank(s) ? null : s.trim();
    }
}
