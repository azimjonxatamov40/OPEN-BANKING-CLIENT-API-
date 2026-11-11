package openbankingclientapi.dto;

public enum ActiveType {
    ACTIVE, PASSIVE, OTHER;

    public static ActiveType fromAbs(String sign) {
        if (sign == null) {
            return OTHER;
        }
        return switch (sign.trim().toUpperCase()) {
            case "A", "А" -> ACTIVE;
            case "P", "П" -> PASSIVE;
            default -> OTHER;
        };
    }
}
