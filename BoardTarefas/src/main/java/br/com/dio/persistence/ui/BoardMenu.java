package br.com.dio.persistence.ui;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {
    private final Scanner scan = new Scanner(System.in);
    private final BoardEntity boardEntity;

    public void execute(){
        System.out.printf("Bem vindo ao Board %s, selecione a operação desejada: ", boardEntity.getId());
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

}
