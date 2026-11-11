package openbankingclientapi.dto;

public record ApiResponse<T>(
        T data,
        boolean success,
        ApiError error) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, true, null);
    }

    public static <T> ApiResponse<T> fail(ApiError error) {
        return new ApiResponse<>(null, false, error);
    }
}
