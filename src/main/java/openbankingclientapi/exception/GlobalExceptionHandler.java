package openbankingclientapi.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import openbankingclientapi.dto.ApiError;
import openbankingclientapi.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String JSON = "application/json";

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Отсутствует обязательный параметр, некорректное значение или некорректный JSON",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class), examples = {
                            @ExampleObject(name = "EMPTY_PARAM", value = """
                                    {"code":5001,"message":"EMPTY_PARAM","cause":"Отсутствует обязательный параметр.",
                                    "fields":["requestId","dsign"]}"""),
                            @ExampleObject(name = "INVALID_PARAM", value = """
                                    {"code":5002,"message":"INVALID_PARAM","cause":"Некорректное значение параметра.",
                                    "fields":["payload.pinfl"]}""")})),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                    description = "Доступ к счету запрещен",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "ACC_TYPE_FORBIDDEN", value = """
                                    {"code":5003,"message":"ACC_TYPE_FORBIDDEN","cause":"Доступ к счету запрещен.",
                                    "fields":[]}"""))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Метод API не найден (неверный адрес)",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "NOT_FOUND", value = """
                                    {"code":404,"message":"NOT_FOUND","cause":"Метод API не найден: /api/v1/accounts/balancs",
                                    "fields":null}"""))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "405",
                    description = "HTTP метод не поддерживается (требуется POST)",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "METHOD_NOT_ALLOWED", value = """
                                    {"code":405,"message":"METHOD_NOT_ALLOWED","cause":"HTTP метод не поддерживается: GET, требуется: POST",
                                    "fields":null}""")))
    })
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApi(ApiException ex) {
        ErrorCode ec = ex.errorCode();
        ApiError error = new ApiError(ec.code(), ec.name(), ec.cause(), ex.fields());
        if (ec.business()) {
            return ResponseEntity.ok(ApiResponse.fail(error));
        }
        return ResponseEntity.status(ec.httpStatus()).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleBadJson(HttpMessageNotReadableException ex) {
        return http(ErrorCode.INVALID_PARAM, ErrorCode.INVALID_PARAM.cause(), List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNotFound(NoResourceFoundException ex) {
        return http(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.cause() + ": /" + ex.getResourcePath(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return http(ErrorCode.METHOD_NOT_ALLOWED,
                ErrorCode.METHOD_NOT_ALLOWED.cause() + ": " + ex.getMethod() + ", требуется: POST", null);
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Ошибка АБС (Oracle) или внутренняя системная ошибка",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class), examples = {
                            @ExampleObject(name = "ABS_ERROR", value = """
                                    {"code":9000,"message":"ABS_ERROR",
                                    "cause":"АБС вернула ошибку: ORA-00942: table or view does not exist","fields":null}"""),
                            @ExampleObject(name = "SYSTEM_ERROR", value = """
                                    {"code":5000,"message":"SYSTEM_ERROR","cause":"Внутренняя системная ошибка: ...",
                                    "fields":null}""")})),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502",
                    description = "Ошибка соединения с АБС (Oracle)",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "ABS_BAD_GATEWAY", value = """
                                    {"code":9002,"message":"ABS_BAD_GATEWAY","cause":"Ошибка соединения с АБС.",
                                    "fields":null}"""))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "504",
                    description = "Истекло время ожидания ответа от АБС (Oracle)",
                    content = @Content(mediaType = JSON, schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "ABS_TIMEOUT", value = """
                                    {"code":9004,"message":"ABS_TIMEOUT","cause":"Истекло время ожидания ответа от АБС.",
                                    "fields":null}""")))
    })
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDb(DataAccessException ex) {
        log.error("Ошибка АБС (Oracle)", ex);
        if (ex instanceof CannotGetJdbcConnectionException) {
            return http(ErrorCode.ABS_BAD_GATEWAY, ErrorCode.ABS_BAD_GATEWAY.cause(), null);
        }
        if (ex instanceof QueryTimeoutException) {
            return http(ErrorCode.ABS_TIMEOUT, ErrorCode.ABS_TIMEOUT.cause(), null);
        }
        String text = ex.getMostSpecificCause().getMessage();
        return http(ErrorCode.ABS_ERROR, ErrorCode.ABS_ERROR.cause() + ": " + text, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOther(Exception ex) {
        log.error("Непредвиденная ошибка", ex);
        return http(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.cause() + ": " + ex.getMessage(), null);
    }

    private ResponseEntity<Object> http(ErrorCode ec, String cause, List<String> fields) {
        return ResponseEntity.status(ec.httpStatus()).body(new ApiError(ec.code(), ec.name(), cause, fields));
    }
}
