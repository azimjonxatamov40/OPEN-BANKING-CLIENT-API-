package openbankingclientapi.dto;

import java.util.List;

public record ApiError(
        int code,
        String message,
        String cause,
        List<String> fields) {
}
