package main.java.DatabaseConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCutie {

    static public String url = "jdbc:mysql://localhost:3306/wjbu";
    static public String user = "root";
    static public String password = "vucaotan200147";

    static public String dbname = "wjbu";
    static public String tbnameAV = "av";
    static public String tbnameVA = "va";
    static public String colmId = "id";
    static public String colmWord = "word";
    static public String colmHTML = "html";
    static public String colmDesc = "description";

    static public String currentWord = "";
    static public String currentMeaning = "";
    static public String currenttb = "";
    static public int currentId = 0;

    /** 0: English - Vietnames  1: Vietnamese - English. */
    static public List<String> tbList = new ArrayList<>();

    static public Connection conn;
    static public Statement state;

    static public ResultSet rs = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException throwables) {
            throwables.printStackTrace();
        }
    }

    static {
        try {
            state = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Destructor (){
        try {
            state.close();
            conn.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
