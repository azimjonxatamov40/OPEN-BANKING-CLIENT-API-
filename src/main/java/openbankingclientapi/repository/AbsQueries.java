package openbankingclientapi.repository;

final class AbsQueries {

    private AbsQueries() {
    }

    static final String CLIENT_BY_PINFL = """
            select c.branch                                 as mfo,
                   c.pinfl                                  as pinfl,
                   c.passport_serial || c.passport_number   as document_id,
                   c.phone_mobile                           as phone_number,
                   c.id                                     as bank_client_number,
                   c.first_name                             as first_name,
                   c.family                                 as last_name,
                   c.patronymic                             as patronymic,
                   c.birthday                               as birth_date
              from client_p c
             where c.pinfl = :pinfl
             order by c.branch
            """;

    private static final String ACCOUNT_SELECT = """
            select a.id                                               as ban,
                   a.branch                                           as mfo,
                   a.name                                             as account_name,
                   a.sgn                                              as active_sign,
                   a.state                                            as state_code,
                   a.acc_bal                                          as balance_account,
                   a.currency                                         as currency,
                   kernel.GetSOut(a.id, info.GetDay, null, a.branch)  as current_balance
              from account a
            """;

    static final String ACCOUNT_BY_BAN = ACCOUNT_SELECT + """
             where a.id = :ban
               and a.branch = :mfo
            """;

    static final String ACCOUNT_SEARCH = ACCOUNT_SELECT + """
             where a.branch = :mfo
               and a.client = :client
               and (:balanceType is null or a.acc_bal = :balanceType)
               and (:ccy is null or a.currency = :ccy)
               and (:ord is null or a.id_order = :ord)
             order by a.id
            """;

    static final String ACCOUNT_EXISTS = """
            select count(*)
              from account a
             where a.id = :ban
               and a.branch = :mfo
            """;
}
