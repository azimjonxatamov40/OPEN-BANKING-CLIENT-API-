package openbankingclientapi.exception;

public enum ErrorCode {
    INVALID_DSIGN(400, 1100, "Некорректная цифровая подпись.", false),
    SYSTEM_ERROR(500, 5000, "Внутренняя системная ошибка", false),
    EMPTY_PARAM(400, 5001, "Отсутствует обязательный параметр.", false),
    INVALID_PARAM(400, 5002, "Некорректное значение параметра.", false),
    ACC_TYPE_FORBIDDEN(403, 5003, "Доступ к счету запрещен.", false),
    ACC_NOT_FOUND(404, 5004, "Счет не найден.", true),
    MFO_NOT_FOUND(404, 5005, "МФО банка не найден.", true),
    CLIENT_NOT_FOUND(404, 5006, "Клиент не найден.", true),
    ABS_ERROR(500, 9000, "АБС вернула ошибку", false),
    ABS_BAD_GATEWAY(502, 9002, "Ошибка соединения с АБС.", false),
    ABS_TIMEOUT(504, 9004, "Истекло время ожидания ответа от АБС.", false),
    NOT_FOUND(404, 404, "Метод API не найден", false),
    METHOD_NOT_ALLOWED(405, 405, "HTTP метод не поддерживается", false);

    private final int httpStatus;
    private final int code;
    private final String cause;
    private final boolean business;

    ErrorCode(int httpStatus, int code, String cause, boolean business) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.cause = cause;
        this.business = business;
    }

    public int httpStatus() {
        return httpStatus;
    }

    public int code() {
        return code;
    }

    public String cause() {
        return cause;
    }

    public boolean business() {
        return business;
    }
}
