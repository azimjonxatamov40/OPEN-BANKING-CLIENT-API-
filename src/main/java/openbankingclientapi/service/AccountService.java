package openbankingclientapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import openbankingclientapi.dto.AccountBaseResponseDTO;
import openbankingclientapi.dto.AccountClassType;
import openbankingclientapi.dto.AccountDetailResponseDTO;
import openbankingclientapi.dto.AccountKeyPayload;
import openbankingclientapi.dto.AccountSearchPayload;
import openbankingclientapi.dto.AccountState;
import openbankingclientapi.dto.AccountType;
import openbankingclientapi.dto.ActiveType;
import openbankingclientapi.dto.ApiRequest;
import openbankingclientapi.dto.BalancesPayload;
import openbankingclientapi.exception.ApiException;
import openbankingclientapi.exception.ErrorCode;
import openbankingclientapi.repository.AccountRepository;
import openbankingclientapi.repository.AccountRow;

import static openbankingclientapi.service.RequestValidator.trimToNull;

@Service
public class AccountService {

    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public List<AccountBaseResponseDTO> getBalances(ApiRequest<BalancesPayload> request) {
        RequestValidator v = RequestValidator.of(request);
        List<AccountKeyPayload> bans = request.payload().bans();
        if (bans == null || bans.isEmpty()) {
            throw new ApiException(ErrorCode.EMPTY_PARAM, List.of("payload.bans"));
        }
        for (int i = 0; i < bans.size(); i++) {
            AccountKeyPayload key = bans.get(i);
            v.require("payload.bans[" + i + "].ban", key == null ? null : key.ban());
            v.require("payload.bans[" + i + "].mfo", key == null ? null : key.mfo());
        }
        v.check();

        List<AccountBaseResponseDTO> result = new ArrayList<>();
        for (AccountKeyPayload key : bans) {
            AccountRow row = find(key.ban(), key.mfo());
            result.add(new AccountBaseResponseDTO(
                    request.requestId(), row.ban(), row.mfo(), row.currentBalance(), row.accountName()));
        }
        return result;
    }

    public List<AccountDetailResponseDTO> search(ApiRequest<AccountSearchPayload> request) {
        RequestValidator v = RequestValidator.of(request);
        AccountSearchPayload p = request.payload();
        v.require("payload.mfo", p.mfo())
                .require("payload.bankClientNumber", p.bankClientNumber())
                .requireAnyOf(List.of("payload.balanceType", "payload.ccy", "payload.order"),
                        p.balanceType(), p.ccy(), p.order())
                .check();

        List<AccountRow> rows = accounts.search(
                p.mfo().trim(), p.bankClientNumber().trim(),
                trimToNull(p.balanceType()), trimToNull(p.ccy()), trimToNull(p.order()));

        return rows.stream().map(row -> toDetail(request.requestId(), row)).toList();
    }

    public AccountDetailResponseDTO getDetails(ApiRequest<AccountKeyPayload> request) {
        AccountKeyPayload p = validateKey(request);
        return toDetail(request.requestId(), find(p.ban(), p.mfo()));
    }

    public boolean exists(ApiRequest<AccountKeyPayload> request) {
        AccountKeyPayload p = validateKey(request);
        return accounts.exists(p.ban().trim(), p.mfo().trim());
    }


    private AccountKeyPayload validateKey(ApiRequest<AccountKeyPayload> request) {
        RequestValidator v = RequestValidator.of(request);
        AccountKeyPayload p = request.payload();
        v.require("payload.ban", p.ban()).require("payload.mfo", p.mfo()).check();
        return p;
    }

    private AccountRow find(String ban, String mfo) {
        return accounts.findByBan(ban.trim(), mfo.trim())
                .orElseThrow(() -> new ApiException(ErrorCode.ACC_NOT_FOUND, List.of(ban)));
    }

    private AccountDetailResponseDTO toDetail(String requestId, AccountRow row) {
        return new AccountDetailResponseDTO(
                requestId,
                row.ban(),
                row.mfo(),
                row.currentBalance(),
                row.accountName(),
                ActiveType.fromAbs(row.activeSign()),
                AccountState.fromAbs(row.stateCode()),
                AccountClassType.fromBalanceAccount(row.balanceAccount()),
                AccountType.fromCode(null),
                toIsoCurrency(row.currency()),
                row.openDate(),
                row.closeDate(),
                null);
    }

    private static String toIsoCurrency(String currency) {
        return "000".equals(currency) ? "860" : currency;
    }
}
