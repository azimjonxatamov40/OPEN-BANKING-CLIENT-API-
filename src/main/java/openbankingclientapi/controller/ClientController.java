package openbankingclientapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import openbankingclientapi.dto.ApiRequest;
import openbankingclientapi.dto.ApiResponse;
import openbankingclientapi.dto.ClientDetailResponseDTO;
import openbankingclientapi.dto.ClientInfoPayload;
import openbankingclientapi.service.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/info")
    public ApiResponse<ClientDetailResponseDTO> info(@RequestBody ApiRequest<ClientInfoPayload> request) {
        return ApiResponse.ok(service.getInfo(request));
    }
}
