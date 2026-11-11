package openbankingclientapi.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import openbankingclientapi.dto.ClientDetailResponseDTO;

@Repository
public class ClientRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ClientRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<ClientDetailResponseDTO> MAPPER = (rs, rowNum) -> {
        Date birth = rs.getDate("birth_date");
        return new ClientDetailResponseDTO(
                null,
                rs.getString("mfo"),
                rs.getString("pinfl"),
                rs.getString("document_id"),
                rs.getString("phone_number"),
                rs.getString("bank_client_number"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("patronymic"),
                birth == null ? null : birth.toLocalDate());
    };

    public List<ClientDetailResponseDTO> findByPinfl(String pinfl) {
        return jdbc.query(AbsQueries.CLIENT_BY_PINFL, Map.of("pinfl", pinfl), MAPPER);
    }
}
