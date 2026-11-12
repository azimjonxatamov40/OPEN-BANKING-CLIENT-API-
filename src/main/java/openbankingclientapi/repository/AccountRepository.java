package openbankingclientapi.repository;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public AccountRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<AccountRow> MAPPER = (rs, rowNum) -> {
        BigDecimal balance = rs.getBigDecimal("current_balance");
        return new AccountRow(
                rs.getString("ban"),
                rs.getString("mfo"),
                rs.getString("account_name"),
                rs.getString("active_sign"),
                rs.getString("state_code"),
                rs.getString("balance_account"),
                rs.getString("currency"),
                balance == null ? null : balance.longValue());
    };

    public Optional<AccountRow> findByBan(String ban, String mfo) {
        List<AccountRow> rows = jdbc.query(AbsQueries.ACCOUNT_BY_BAN, key(ban, mfo), MAPPER);
        return rows.stream().findFirst();
    }

    public List<AccountRow> search(String mfo, String client, String balanceType, String ccy, String order) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("mfo", mfo, Types.VARCHAR)
                .addValue("client", client, Types.VARCHAR)
                .addValue("balanceType", balanceType, Types.VARCHAR)
                .addValue("ccy", ccy, Types.VARCHAR)
                .addValue("ord", order, Types.VARCHAR);
        return jdbc.query(AbsQueries.ACCOUNT_SEARCH, params, MAPPER);
    }

    public boolean exists(String ban, String mfo) {
        Integer count = jdbc.queryForObject(AbsQueries.ACCOUNT_EXISTS, key(ban, mfo), Integer.class);
        return count != null && count > 0;
    }

    private static MapSqlParameterSource key(String ban, String mfo) {
        return new MapSqlParameterSource()
                .addValue("ban", ban, Types.VARCHAR)
                .addValue("mfo", mfo, Types.VARCHAR);
    }
}
