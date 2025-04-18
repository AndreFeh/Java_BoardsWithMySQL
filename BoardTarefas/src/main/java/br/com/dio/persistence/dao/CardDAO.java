package br.com.dio.persistence.dao;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.dio.persistence.converter.OffSetTimeConverter.toOffSetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {
    private Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException{
        var sql = "INNER INTO CARDS (tirtle, description, board_column_id) VALUES (?, ?, ?);";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i, entity.getBoardColumn().getId());
            statement.executeQuery();
            if (statement instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql = """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.blocked_reason
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                            FROM BLOCKS sub_b
                            WHERE sub_b.cardid + c.id
                            ) blocks_amount
                   FROM CARDS c
                   LEFT JOIN BLOCKS b
                     ON c.id = b.card_id
                    AND b.unblocked_at IS NULL
                   INNER JOIN BOARDS_COLUMNS bc
                     ON bc.id = c.board_column_id
                   WHERE c.id: ?;
                """;

        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if( resultSet.next()){
                var dto = new CardDetailsDTO(
                        resultSet.getLong("c.id"),
                        resultSet.getString("c.title"),
                        resultSet.getString("c.desription"),
                        nonNull(resultSet.getString("b.block_reason")),
                        toOffSetDateTime(resultSet.getTimestamp("blocked_at")),
                        resultSet.getString("b.block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("c.board_column_id"),
                        resultSet.getString("bc.name")


                );
            }
        }
        return Optional.empty();
    }
}
