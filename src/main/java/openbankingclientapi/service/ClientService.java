package openbankingclientapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import openbankingclientapi.dto.ApiRequest;
import openbankingclientapi.dto.ClientDetailResponseDTO;
import openbankingclientapi.dto.ClientInfoPayload;
import openbankingclientapi.exception.ApiException;
import openbankingclientapi.exception.ErrorCode;
import openbankingclientapi.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository clients;

    public ClientService(ClientRepository clients) {
        this.clients = clients;
    }

    public ClientDetailResponseDTO getInfo(ApiRequest<ClientInfoPayload> request) {
        RequestValidator v = RequestValidator.of(request);
        String pinfl = request.payload().pinfl();
        v.require("payload.pinfl", pinfl).check();

        pinfl = pinfl.trim();
        if (!pinfl.matches("\\d{14}")) {
            throw new ApiException(ErrorCode.INVALID_PARAM, List.of("payload.pinfl"));
        }

        List<ClientDetailResponseDTO> found = clients.findByPinfl(pinfl);
        if (found.isEmpty()) {
            throw new ApiException(ErrorCode.CLIENT_NOT_FOUND);
        }

        ClientDetailResponseDTO c = found.get(0);
        return new ClientDetailResponseDTO(
                request.requestId(), c.mfo(), c.pinfl(), c.documentId(), c.phoneNumber(),
                c.bankClientNumber(), c.firstName(), c.lastName(), c.patronymic(), c.birthDate());
    }
}
