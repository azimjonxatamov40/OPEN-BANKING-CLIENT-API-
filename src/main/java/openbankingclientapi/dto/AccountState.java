package openbankingclientapi.dto;

public enum AccountState {
    NOT_DETERMINED,
    OPEN,
    APPROVED,
    CLOSED,
    LOCKED,
    AWAITING_REGISTRATION_IN_THE_NIBBD,
    AWAITING_TRANSFER_TO_ANOTHER_BANK,
    TRANSFERRED_TO_ANOTHER_BANK,
    AWAITING_CLOSURE_AT_NIBBD,
    WAITING_FOR_LOCKS_IN_NIBBD,
    WAITING_FOR_RELEASE_OF_LOCK_IN_NIBBD,
    AWAITING_RE_REGISTRATION_OF_A_CLOSED_ACCOUNT_WITH_NIBBD,
    AWAITING_RE_REGISTRATION_OF_THE_TRANSFERRED_ACCOUNT_WITH_THE_NIBBD,
    OTHER;

    public static AccountState fromAbs(String code) {
        if (code == null || code.isBlank()) {
            return NOT_DETERMINED;
        }
        try {
            int i = Integer.parseInt(code.trim());
            if (i >= 0 && i < OTHER.ordinal()) {
                return values()[i];
            }
        } catch (NumberFormatException ignored) {
        }
        return OTHER;
    }
}
