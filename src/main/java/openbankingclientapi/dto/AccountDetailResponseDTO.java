package openbankingclientapi.dto;

import java.time.LocalDate;

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
        String currency,
        LocalDate openDate,
        LocalDate closeDate,
        LocalDate openNibbdDate) {
}
