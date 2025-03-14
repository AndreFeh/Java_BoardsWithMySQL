package br.com.dio;

import br.com.dio.persistence.migration.MigrationStrategy;
import br.com.dio.persistence.ui.MainMenu;

import java.sql.SQLException;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;


public class Main {
    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            if (connection != null) {
                // Exibe no console antes de iniciar o Liquibase
                System.out.println("Iniciando Liquibase...");

                // Executa a migração
                new MigrationStrategy(connection).executeMigration();
            } else {
                System.out.println("Falha ao conectar ao banco.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
        new MainMenu().execute();
    }
}
