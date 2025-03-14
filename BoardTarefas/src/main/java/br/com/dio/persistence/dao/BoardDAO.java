package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        String sql = "INSERT INTO BOARDS (name) VALUES (?);";
        try(var statement = connection.prepareStatement(sql)){
            statement.setString(1, entity.getName());
            statement.executeUpdate();
            if(statement instanceof StatementImpl impl)/*funcao do MySQL*/{
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }

    }

    public void delete(final Long id) throws SQLException {
        String sql = "DELETE FROM BOARDS WHERE id=?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();/*Toda vez que deletar, vai atualizar o banco de dados*/
        }
    }


    public boolean exists(final Long id) throws SQLException {
        String sql = "SELECT 1 FROM BOARDS WHERE id=?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();/*Se o resultado existe, automaticamente é true*/
        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        String sql = "SELECT id, name FROM BOARDS WHERE id=?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();/*Se o resultado existe, automaticamente é true*/
            if (resultSet.next()) {
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }


}
