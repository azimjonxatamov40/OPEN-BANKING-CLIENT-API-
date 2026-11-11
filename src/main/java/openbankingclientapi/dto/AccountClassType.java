package openbankingclientapi.dto;

public enum AccountClassType {
    ASSETS, LIABILITIES, EQUITY, INCOME, EXPENSES, CONTINGENT;

    public static AccountClassType fromBalanceAccount(String balanceAccount) {
        if (balanceAccount == null || balanceAccount.isBlank()) {
            return null;
        }
        return switch (balanceAccount.trim().charAt(0)) {
            case '1' -> ASSETS;
            case '2' -> LIABILITIES;
            case '3' -> EQUITY;
            case '4' -> INCOME;
            case '5' -> EXPENSES;
            case '9' -> CONTINGENT;
            default -> null;
        };
    }
}
