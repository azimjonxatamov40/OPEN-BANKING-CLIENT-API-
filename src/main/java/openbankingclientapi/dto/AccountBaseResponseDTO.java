package openbankingclientapi.dto;

public record AccountBaseResponseDTO(
        String requestId,
        String ban,
        String mfo,
        Long currentBalance,
        String accountName) {
}
