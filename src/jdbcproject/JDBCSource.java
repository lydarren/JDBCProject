/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbcproject;

import beans.Book;
import beans.Publisher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import tables.WritingGroups;
import tables.Books;
import tables.Publishers;

/**
 *
 * @author Josef
 */
public class JDBCSource {

    static String USER;
    static String PASS;
    static String DBNAME; // will hold the database name
    static String DB_URL = "jdbc:derby://localhost:1527/";

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        Scanner inputInt = new Scanner(System.in);
        boolean cont = true;
        int userOp = 0;
        String usrStr = "";

        System.out.print("Enter the name of the database: ");
        DBNAME = input.nextLine();

        System.out.print("Enter your username: ");
        USER = input.nextLine();

        System.out.print("Enter your password: ");
        PASS = input.nextLine();

        DB_URL += DBNAME;

        // Making a connection with a try with resources statement
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);) {
            System.out.println("ACCESS GRANTED!");

            System.out.println("\n\nWhat would you like to do?");

            // The while loop will continue until the user enters 10
            // setting cont variable to false.
            while (cont) {
                System.out.println("\n\n1) List all groups"
                        + "\n2) List group details for a specified group"
                        + "\n3) List all publishers"
                        + "\n4) List publisher details for a specified publisher"
                        + "\n5) List all books"
                        + "\n6) List all the data for a book specified"
                        + "\n7) Insert a new book"
                        + "\n8) Insert a new publisher to update a publishers info"
                        + "\n9) Remove a specified book"
                        + "\n10) Exit");

                // used for user option selection
                userOp = inputInt.nextInt();

                if (userOp == 1) {
                    WritingGroups.displayAllGroups(conn);
                } else if (userOp == 2) {
                    System.out.print("Enter group name: ");
                    usrStr = input.nextLine();

                    WritingGroups.displayAllInfo(conn, usrStr);
                } else if (userOp == 3) {
                    Publishers.displayAllPublishers(conn);

                } else if (userOp == 4) {
                    System.out.println("Enter a publisher name: ");
                    usrStr = input.nextLine();
                    Publishers.displayAllInfo(conn, usrStr);
                } else if (userOp == 5) {
                    Books.displayAllBooks(conn);
                } else if (userOp == 6) {
                    System.out.println("Enter a book name: ");
                    usrStr = input.nextLine();
                    Books.displayAllInfo(conn, usrStr);
                } else if (userOp == 7) {
                    // Java bean created to pass information for a Book
                    // the bean contains groupname, title, publishername, yearpublished
                    // and number of pages.

                    Book bean = new Book();

                    System.out.print("Enter the group name for the book: ");
                    bean.setGroupName(input.nextLine());

                    if (WritingGroups.checkWriting(conn, bean.getGroupName())) {

                        System.out.print("Publisher Name: ");
                        bean.setPublisherName(input.nextLine());

                        if (Publishers.checkPub(conn, bean.getPublisherName())) {

                            System.out.print("Book Title: ");
                            bean.setBookTitle(input.nextLine());

                            System.out.print("Year published: ");
                            bean.setYearpublished(inputInt.nextInt());

                            System.out.print("Number of Pages: ");
                            bean.setNumberofpages(inputInt.nextInt());

                            // passing in Book bean and connection
                            Books.insertBook(conn, bean);
                        }
                        else{
                            System.err.println("THE PUBLISHER DOES NOT EXIST: PLEASE"
                                    + " CHOOSE A PUBLISHER THAT IS LISTED.");
                        }
                    }
                    else{
                        System.err.println("THE WRITING GROUP DOES NOT EXIST: PLEASE"
                                    + " CHOOSE A WRITING GROUP THAT IS LISTED.");
                    }
                    
                    
                    
                } else if (userOp == 8) {
                    String pubName = "";

                    // Java bean created to pass information for a Publisher
                    // the bean contains name, address, phone, and email info
                    Publisher bean = new Publisher();

                    System.out.print("Which publisher info do you want to update?: ");
                    pubName = input.nextLine();

                    if (Publishers.checkPub(conn, pubName)) {

                        System.out.print("New publisher name: ");
                        bean.setpName(input.nextLine());

                        if (!(pubName.equalsIgnoreCase(bean.getpName()))) {
                            if (!(Publishers.checkPub(conn, bean.getpName()))) {
                                System.out.print("New publisher address: ");
                                bean.setpAddress(input.nextLine());

                                System.out.print("New publisher phone number: ");
                                bean.setpPhone(input.nextLine());

                                System.out.print("New publisher email: ");
                                bean.setpEmail(input.nextLine());

                                // passing in Publisher bean, connection, 
                                // and publisher to name
                                Publishers.updatePub(conn, bean, pubName, false);
                            } else {
                                // passing in Publisher bean, connection, 
                                // and publisher to name
                                Publishers.updatePub(conn, bean, pubName, true);
                            }
                        } else {
                            System.err.println("THAT IS THE SAME PUBLISHER!");
                        }
                    } else {

                        System.err.println("NO SUCH PUBLISHER FOUND!");
                    }

                } else if (userOp == 9) {
                    System.out.println("Enter a book name: ");
                    usrStr = input.nextLine();
                    Books.removeBook(conn, usrStr);
                } else if (userOp == 10) {
                    cont = false;
                } else {
                    System.out.println("Enter a valid option");
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
