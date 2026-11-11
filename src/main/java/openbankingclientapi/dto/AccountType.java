package openbankingclientapi.dto;

public enum AccountType {
    CORRESPONDENCE_MAIN_ACCOUNT,
    CORRESPONDENCE_OVERDRAFT_ACCOUNT,
    MAIN_MFO,
    OVERDRAFT_MFO,
    ACCOUNT_WITHOUT_OVERDRAFT,
    ACCOUNT_SECTION_TYPE;

    public static AccountType fromCode(String code) {
        if (code != null) {
            try {
                int i = Integer.parseInt(code.trim());
                if (i >= 0 && i < values().length) {
                    return values()[i];
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return ACCOUNT_WITHOUT_OVERDRAFT;
    }
}
