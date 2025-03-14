package br.com.dio.persistence.migration;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;

@AllArgsConstructor
public class MigrationStrategy {
    private final Connection connection;

    public void executeMigration() {
        var originalOut = System.out;
        var originalErr = System.err;

        try (var fos = new FileOutputStream("liquibase.log")) {
            System.setOut(new PrintStream(fos));
            System.setErr(new PrintStream(fos));

            try (var jdbcConnection = new JdbcConnection(this.connection)) { // ✅ Correção aqui
                var liquibase = new Liquibase(
                        "db/changelog/db.changelog-master.yml", // Caminho correto
                        new ClassLoaderResourceAccessor(),
                        jdbcConnection
                );
                liquibase.update(""); // Adiciona contexto vazio para evitar erro

                System.out.println("Liquibase executado com sucesso!");

            } catch (LiquibaseException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
