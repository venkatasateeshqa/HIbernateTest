package com.neoteric.dbconnection.service;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public  static  Connection connection;


    public static Connection getConnection(){

        try {
            if(connection==null) {
                System.out.println("getting connection");

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank",
                        "root", "root");
            }else {
                System.out.println("returning existing connection");
            }

        }catch (Exception e){
            System.out.println("Exception Occured in getconnection"+e);
        }
        return connection;
    }
}
