package openbankingclientapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import openbankingclientapi.dto.AccountBaseResponseDTO;
import openbankingclientapi.dto.AccountDetailResponseDTO;
import openbankingclientapi.dto.AccountKeyPayload;
import openbankingclientapi.dto.AccountSearchPayload;
import openbankingclientapi.dto.ApiRequest;
import openbankingclientapi.dto.ApiResponse;
import openbankingclientapi.dto.BalancesPayload;
import openbankingclientapi.service.AccountService;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/balances")
    public ApiResponse<List<AccountBaseResponseDTO>> balances(@RequestBody ApiRequest<BalancesPayload> request) {
        return ApiResponse.ok(service.getBalances(request));
    }

    @PostMapping("/search")
    public ApiResponse<List<AccountDetailResponseDTO>> search(@RequestBody ApiRequest<AccountSearchPayload> request) {
        return ApiResponse.ok(service.search(request));
    }

    @PostMapping("/details")
    public ApiResponse<AccountDetailResponseDTO> details(@RequestBody ApiRequest<AccountKeyPayload> request) {
        return ApiResponse.ok(service.getDetails(request));
    }

    @PostMapping("/exists")
    public ApiResponse<Boolean> exists(@RequestBody ApiRequest<AccountKeyPayload> request) {
        return ApiResponse.ok(service.exists(request));
    }
}
