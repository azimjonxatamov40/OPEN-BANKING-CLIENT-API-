package openbankingclientapi.dto;

import java.util.List;

public record BalancesPayload(
        List<AccountKeyPayload> bans) {
}
