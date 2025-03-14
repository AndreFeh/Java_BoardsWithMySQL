package br.com.dio.persistence.ui;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.service.BoardQueryService;
import br.com.dio.persistence.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {
    private final Scanner scan = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem Vindo ao gerenciador de Boards, escolha a opção desejada");
        while (true) {
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");

            var option = scan.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção invalida, informe uma opção ao menu");
            }
        }
    }

    public void createBoard() throws SQLException{
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu board");
        entity.setName(scan.next());

        System.out.println("Seu board tera quantas colunas alem das 3? Se sim, informe quantas, senao digite '0'");
        var aditionalColumns = scan.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board: ");
        var initialColumnName = scan.next();
        var initialColumn = createColumn(initialColumnName, BoardColumnKindEnum.INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < aditionalColumns; i++){
            System.out.println("Informe o nome da coluna tarefa pendente do board: ");

            var pendingColumnName = scan.next();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnKindEnum.PENDING, i+1);
            columns.add(pendingColumn);
        }
        System.out.println("Informe o nome da coluna final do board: ");
        var finalColumnName = scan.next();
        var finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, aditionalColumns+1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do board: ");
        var cancelColumnName = scan.next();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, aditionalColumns+2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);
        try(var connection = getConnection()){
            var service = new BoardService(connection);
            service.insert(entity);
        }

    }

    public void selectBoard() throws SQLException {
        System.out.println("Informe Id do board que deseja selecionar");
        var id = scan.nextLong();
        try(var connection = getConnection()){
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);
            optional.ifPresentOrElse(b -> new BoardMenu(optional.get()).execute(), ()->System.out.printf("Nao foi encontrado o board com id %s%n", id));
//            if(optional.isPresent()){
//                var boardMenu = new BoardMenu(optional.get());
//                boardMenu.execute();
//
//            } else System.out.printf("Nao foi encontrado o board com id %s", id);
        }
    }

    public void deleteBoard() throws SQLException {
        System.out.println("Infortme o Id a ser excluido");
        var id = scan.nextLong();
        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board %s foi excluido\n", id);
            } else System.out.printf("Nao foi encontrado o board com id %s", id);
        }
    }

    public BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum columnKindEnum, final int order){
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName((name));
        boardColumn.setKind(columnKindEnum);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}
