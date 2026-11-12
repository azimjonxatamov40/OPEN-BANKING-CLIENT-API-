package openbankingclientapi.repository;

public record AccountRow(
        String ban,
        String mfo,
        String accountName,
        String activeSign,
        String stateCode,
        String balanceAccount,
        String currency,
        Long currentBalance) {
}
