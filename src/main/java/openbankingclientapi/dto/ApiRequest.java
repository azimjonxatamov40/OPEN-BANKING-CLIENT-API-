package openbankingclientapi.dto;

public record ApiRequest<T>(
        String requestId,
        String dsign,
        T payload) {
}
