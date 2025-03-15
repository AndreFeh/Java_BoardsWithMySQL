package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.entity.CardEntity;
import br.com.dio.service.BoardColumnQueryService;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.CardQueryService;
import br.com.dio.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {
    private final Scanner scan = new Scanner(System.in);
    private final BoardEntity boardEntity;

    public void execute() {
        try {

            System.out.printf("Bem vindo ao Board %s, selecione a operação desejada: %n", boardEntity.getId());
            System.out.println("Bem Vindo ao gerenciador de Boards, escolha a opção desejada\n");
            while (true) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Visualizar board");
                System.out.println("7 - Visualizar coluna com card");
                System.out.println("8 - Visualizar card");
                System.out.println("9 - Voltar para o menu anterior");
                System.out.println("10 - Sair");

                var option = scan.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> CancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para menu anterior\n");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção invalida, informe uma opção ao menu\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }


    }

    public void createCard() throws SQLException{
        var card = new CardEntity();
        System.out.println("Informe o titulo do card: \n");
        card.setTitle(scan.next());

        System.out.println("Informe a descrição do card: \n");
        card.setDescription(scan.next());
        card.setBoardColumn(boardEntity.getInitialColumns ());
        try(var connection = getConnection()){
            new CardService(connection).insert(card);
        }

    }

    public void moveCardNextColumn() {

    }

    public void blockCard() {

    }

    public void unblockCard() {

    }

    public void CancelCard() {

    }

    public void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]%n", b.id(), b.name());
                b.columns().forEach(c -> {
                    System.out.printf("Coluna [%s], tipo: [%s], tem %s cards %n", c.name(), c.kind(), c.cardsAmount());
                });
            });
        }
    }

    public void showColumn() throws SQLException {
        var columnsId = boardEntity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumns = -1L;
        while (!columnsId.contains(selectedColumns)) {
            System.out.printf("Escolha uma coluna do Board %s%n", boardEntity.getName()); /*Ficara no loop ate escolher a opcao certa*/
            boardEntity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]%n", c.getId(), c.getName(), c.getKind()));
            selectedColumns = scan.nextLong();
        }
        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumns);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s,   tipo %s%n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card: %s - %s %n Descrição: %s%n", ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }

    }

    public void showCard() throws SQLException {
        System.out.println("Informe o Id do Card que deseja visualizar");
        var selectedCardId = scan.nextLong();
        try (var connecion = getConnection()) {
            new CardQueryService(connecion).findById(selectedCardId)
                    .ifPresentOrElse(c -> {
                        System.out.printf("Card: %s - %s%n", c.id(), c.title());
                        System.out.printf(", Descrição: %s%n", c.description());
                        System.out.println(c.blocked()? "Esta bloqueado. Motivo %s%n"+c.blockReason() :"Nao esta bloqueado" );
                        System.out.printf("Ja foi bloqueado %s vezes%n", c.blockedAmount());
                        System.out.printf("Está no momento na coluna %s - %s%n", c.columnId(), c.columnName());
                    }, () -> System.out.printf("Nao existe card com o ID %s%n", selectedCardId));
        }

    }

}
