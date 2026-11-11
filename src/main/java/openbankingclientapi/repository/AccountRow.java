package openbankingclientapi.repository;

import java.time.LocalDate;

public record AccountRow(
        String ban,
        String mfo,
        String accountName,
        String activeSign,
        String stateCode,
        String balanceAccount,
        String currency,
        LocalDate openDate,
        LocalDate closeDate,
        Long currentBalance) {
}
