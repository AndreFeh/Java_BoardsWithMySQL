package br.com.dio.persistence.service;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity)throws SQLException{
        var dao= new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        try {
            dao.insert(entity);
            var columns = entity.getBoardColumns().stream().map(c->{
                c.setBoard(entity);
                return c;
            }).toList();
            for(var column : columns){
                boardColumnDao.insert(column);
            }
            connection.commit();
//   FARA INSERT DE TUDO, ASSIM QUE INSERIR TUDO DARA O COMMIT, SE DER ALGUM ERRO NA HORA DE INSERIR ELE FARA UM COMMIT
        } catch (SQLException e){
            connection.rollback();
                    throw e;
        }
        return entity;
    }

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try {
            if (!dao.exists(id)) {
                return false;
            }
            dao.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
