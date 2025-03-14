package br.com.dio.persistence.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final/*NAO PERMITE HERANCA*/ class ConnectionConfig {
    public static Connection getConnection() throws SQLException {
        var connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/board", "root", "admin263733");
        connection.setAutoCommit(false);
        return connection;
    } /*<<< Configurar Banco de Dados*/

}
