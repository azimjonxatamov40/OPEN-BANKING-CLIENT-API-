package openbankingclientapi.dto;

public record AccountSearchPayload(
        String mfo,
        String balanceType,
        String ccy,
        String order,
        String bankClientNumber) {
}
