package openbankingclientapi.exception;

import java.util.List;

public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final List<String> fields;

    public ApiException(ErrorCode errorCode) {
        this(errorCode, List.of());
    }

    public ApiException(ErrorCode errorCode, List<String> fields) {
        super(errorCode.cause());
        this.errorCode = errorCode;
        this.fields = fields;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public List<String> fields() {
        return fields;
    }
}
