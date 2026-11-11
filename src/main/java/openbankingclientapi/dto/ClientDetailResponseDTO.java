package openbankingclientapi.dto;

import java.time.LocalDate;

public record ClientDetailResponseDTO(
        String requestId,
        String mfo,
        String pinfl,
        String documentId,
        String phoneNumber,
        String bankClientNumber,
        String firstName,
        String lastName,
        String patronymic,
        LocalDate birthDate) {
}
