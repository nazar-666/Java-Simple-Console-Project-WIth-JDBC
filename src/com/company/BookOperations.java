package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookOperations {
    Statement stmt = null;

    public void AddBook(BookModel model, Connection connection) {
        String query = "INSERT INTO LIBRARY.Books (name, author) VALUES ('"+
                model.getBookName()+ "', '" +
                model.getAuthor() + "')";

        executeUpdate(connection, query);
        System.out.println("Book \"" + model.getBookName() + "\" successfully added");
    }

    public void RemoveBook(BookModel model, Connection connection){
        List<String> booksList = selectBooksByName(connection, model.getBookName());
        int booksCount = booksList.size();

        String deleteQuery = "";

        if (booksCount == 1)
            deleteQuery = "DELETE FROM LIBRARY.Books WHERE name='" + model.getBookName() + "'";
        else if (booksCount > 1)
            deleteQuery = "DELETE FROM LIBRARY.Books WHERE id=";

        ExecuteAction(connection, model, booksList, booksCount, deleteQuery, ConsoleCommands.REMOVE);
    }

    public void EditBook(BookModel model, Connection connection){
        String editQuery = "";
        String newBookName = "";

        List<String> booksList = selectBooksByName(connection, model.getBookName());
        int booksCount = booksList.size();

        if (booksCount > 0){
            System.out.println("Please input new book name: ");
            try {
                while (newBookName.length() <= 0)
                    newBookName = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (booksCount == 1)
            editQuery = "UPDATE LIBRARY.Books SET name='" + newBookName + "' WHERE name='" + model.getBookName() + "'";
        else if (booksCount > 1)
            editQuery = "UPDATE LIBRARY.Books SET name='" + newBookName + "' WHERE id=";

        ExecuteAction(connection, model, booksList, booksCount, editQuery, ConsoleCommands.EDIT);
    }

    public void AllBooks(Connection connection){
        String query = "SELECT * FROM LIBRARY.Books";

        List<String> booksList = ExecuteQuery(connection, query);

        if (booksList.size() == 0){
            System.out.println("There is no one book here, please add some books");
        }
        for (String element : booksList) {
            String[] book = element.split("_");
            System.out.println("  - " + book[1] + " \"" + book[2] + "\"");
        }
    }

    private List<String> selectBooksByName(Connection connection, String name){
        String selectQuery = "SELECT * FROM LIBRARY.Books WHERE name='" + name + "'";
        return ExecuteQuery(connection, selectQuery);
    }

    private void ExecuteAction(Connection connection, BookModel model, List<String> booksList, int resultCount, String query, ConsoleCommands consoleCommand){
        try {
            if (resultCount == 0){
                System.out.println("Such book doesn't exist");
            }
            else if(resultCount == 1){
                executeUpdate(connection, query);
                System.out.println("Book \"" + model.getBookName() + "\" successfuly " + consoleCommand.toString().toLowerCase());
            }
            else {
                ArrayList list = new ArrayList();
                System.out.println("\n Books with such name:");
                for (String element : booksList){
                    String[] book = element.split("_");
                    System.out.println("  - bookID: " + book[0] + ", book details:" + book[1] + " \"" + book[2] + "\"");
                    list.add(book[0]);
                }

                boolean tryReadID = true;
                while (tryReadID){
                    System.out.println("\nPlease, select and input bookID that you want to " + consoleCommand.toString().toLowerCase() + ":");
                    int id = 0;
                    try{
                        id = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine().trim());
                    }catch (NumberFormatException e){
                        System.out.println("Input Id is not correct, please try again");
                    }

                    if(list.contains(id+"")){
                        tryReadID = false;
                        executeUpdate(connection, (query + id));
                        System.out.println("Book \"" + model.getBookName() + "\" successfuly " + consoleCommand.toString().toLowerCase());
                    }
                    else if (!list.contains(id) && id > 0) System.out.println("Input Id is not correct, please try again");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeUpdate(Connection connection, String query){
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> ExecuteQuery(Connection connection, String query){
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String bookName = rs.getString("name");

                list.add(id + "_" + author + "_" + bookName);
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
