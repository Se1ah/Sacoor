package com.zhyar.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/sacoor";

    public static Connection getConnection() {
        Connection con;
        try {
            con = DriverManager.getConnection(URL, "postgres", "12501384");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 0) { //Error Code 0: database server offline
                //new PromptDialogController("Error!", "Database server is offline!");

            }
            return null;
        }
        return con;
    }
}
