package com.company;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

public class InputCommandOperations {
    BookOperations bookOperations =  new BookOperations();
    Connection connection;

    public InputCommandOperations(Connection connection) {
        this.connection = connection;
    }

    public void PrintGeteralInformation(){
        System.out.println("-------------------------------------------------------");
        System.out.println("You can do next things:");
        System.out.println(" - add new book: add {author \"book_name\"}");
        System.out.println(" - remove book: remove {book_name}");
        System.out.println(" - edit book: edit {book_name}");
        System.out.println(" - see all books: all");
        System.out.println("\n - help: help");
        System.out.println(" - exit from library management project: exit");
        System.out.println("-------------------------------------------------------");
    }

    public void ReadLinesFromBuffer(BufferedReader br) {

        while (true) {
            System.out.print("\nPlease input what do you want to do: ");
            try {
                String getLineFromBuffer = br.readLine().trim();
                if (getLineFromBuffer.length()>0)
                    ParseLineFromBuffer(getLineFromBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ParseLineFromBuffer(String lineFromBuffer){
        ConsoleCommands consoleCommand = null;
        BookModel model = null;
        boolean correctModel = true;

        int endCommandPositionInLine = lineFromBuffer.indexOf(" ");

        if (endCommandPositionInLine == -1){
            try {
                consoleCommand = ConsoleCommands.valueOf(lineFromBuffer.toUpperCase());
            }catch (IllegalArgumentException e) {
                System.out.println("\nYou typed wrong command, please try again!");
            }

            if (consoleCommand == ConsoleCommands.EDIT ||
                    consoleCommand == ConsoleCommands.REMOVE ||
                    consoleCommand == ConsoleCommands.ADD){
                correctModel = false;
                System.out.println("\nYou typed wrong command, please try again!");
            }
        }
        else {
            String command = lineFromBuffer.substring(0, endCommandPositionInLine);

            try {
                consoleCommand = ConsoleCommands.valueOf(command.toUpperCase());
            }catch (IllegalArgumentException e) {
                System.out.println("\nYou typed wrong command, please try again!");
            }

            String [] authorAndBookName = lineFromBuffer.substring(++endCommandPositionInLine).split("\"");

            model = new BookModel();

            if (authorAndBookName.length == 1 && consoleCommand != ConsoleCommands.ADD){
                model.setBookName(authorAndBookName[0].trim());
            }else if (authorAndBookName.length == 2 && consoleCommand == ConsoleCommands.ADD){
                model.setAuthor(authorAndBookName[0].trim());
                model.setBookName(authorAndBookName[1].trim());
            }else{
                correctModel = false;
                System.out.println("\nYou typed illegal arguments, please try again!");
            }
        }

        if (consoleCommand != null && correctModel) {
            CallSomeCommand(consoleCommand, model);
        }
    }

    private void CallSomeCommand(ConsoleCommands command, BookModel model){
        switch (command) {
            case EXIT:
                System.out.println("Exit from library management project!");
                System.exit(0);
                break;
            case ADD:
                bookOperations.AddBook(model, connection);
                break;
            case REMOVE:
                bookOperations.RemoveBook(model, connection);
                break;
            case EDIT:
                bookOperations.EditBook(model, connection);
                break;
            case ALL:
                bookOperations.AllBooks(connection);
                break;
            case HELP:
                PrintGeteralInformation();
                break;
            default:
                throw new NotImplementedException();
        }
    }
}
