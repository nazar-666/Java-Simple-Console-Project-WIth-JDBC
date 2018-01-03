package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private final String SQL_USER_NAME = "root";
    private final String SQL_PASSWORD = "root";
    private final String SQL_CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private final String CREATE_DB_IF_NOT_EXIST = "CREATE DATABASE IF NOT EXISTS LIBRARY";
    private final String CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS LIBRARY.Books (" +
            "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
            "author varchar(50) NOT NULL," + "name varchar(120) NOT NULL" + ")";

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.SQL_USER_NAME);
        connectionProps.put("password", this.SQL_PASSWORD);
        connectionProps.setProperty("useSSL", "false");
        connectionProps.setProperty("autoReconnect", "true");

        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(SQL_CONNECTION_STRING,
                connectionProps);

        PreparedStatement preparedStatement = connection.prepareStatement(CREATE_DB_IF_NOT_EXIST);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement(CREATE_TABLE_IF_NOT_EXIST);
        preparedStatement.execute();

        return connection;
    }
}
