package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        BufferedReader br = null;
        Connection connection = null;

        try {
            connection = new DatabaseConnection().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        InputCommandOperations inputCommandOperations = new InputCommandOperations(connection);

        System.out.println("----- WELCOME FOR LIBRARY MANAGEMENT PROJECT -----\n");
        inputCommandOperations.PrintGeteralInformation();

        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            inputCommandOperations.ReadLinesFromBuffer(br);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}