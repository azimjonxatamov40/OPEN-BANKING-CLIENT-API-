package openbankingclientapi.dto;

public record AccountDetailResponseDTO(
        String requestId,
        String ban,
        String mfo,
        Long currentBalance,
        String accountName,
        ActiveType activeType,
        AccountState state,
        AccountClassType accountClassType,
        AccountType accountType,
        String currency) {
}
