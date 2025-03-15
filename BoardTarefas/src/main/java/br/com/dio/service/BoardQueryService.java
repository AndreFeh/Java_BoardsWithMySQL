package br.com.dio.service;

import br.com.dio.dto.BoardDetailsDTO;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.dio.persistence.entity.BoardColumnKindEnum.findByName;

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
    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException{
        var dao = new BoardDAO(connection);
        var boarColumndDAO = new BoardColumnDAO(connection);
        var optional = dao.findById(id);
        if(optional.isPresent()){
            var entity = optional.get();
            var columns = boarColumndDAO.findByBoardIdWithDetails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
