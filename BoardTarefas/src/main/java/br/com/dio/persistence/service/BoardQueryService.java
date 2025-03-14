package br.com.dio.persistence.service;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {
    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var optional = dao.findById(id);
        var boardColumnsDAO = new BoardColumnDAO(connection);
        if (optional.isPresent()){
            var entity = optional.get();
            entity.setBoardColumns(boardColumnsDAO.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
