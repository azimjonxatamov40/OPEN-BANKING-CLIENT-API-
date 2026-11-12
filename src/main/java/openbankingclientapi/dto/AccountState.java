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
        return switch (code.trim()) {
            case "0" -> NOT_DETERMINED;
            case "1" -> OPEN;
            case "2" -> OPEN;
            case "3" -> CLOSED;
            case "4" -> LOCKED;
            case "5" -> AWAITING_REGISTRATION_IN_THE_NIBBD;
            case "7" -> AWAITING_TRANSFER_TO_ANOTHER_BANK;
            case "8" -> TRANSFERRED_TO_ANOTHER_BANK;
            case "9" -> AWAITING_CLOSURE_AT_NIBBD;
            case "10" -> WAITING_FOR_LOCKS_IN_NIBBD;
            case "11" -> WAITING_FOR_RELEASE_OF_LOCK_IN_NIBBD;
            case "12" -> AWAITING_RE_REGISTRATION_OF_A_CLOSED_ACCOUNT_WITH_NIBBD;
            case "13" -> AWAITING_RE_REGISTRATION_OF_THE_TRANSFERRED_ACCOUNT_WITH_THE_NIBBD;
            default -> OTHER;
        };
    }
}
