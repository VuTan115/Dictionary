package sample;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLCutie {
    String url, user, password, dbname, tbnameVA, tbnameAV;
    String colmId, colmWord, colmHTML, colmDesc, colmProno;
    public Connection conn;
    public Statement state;
    public ResultSet rs = null;

    public MySQLCutie() {
        url = "jdbc:mysql://localhost:3306/wjbu";
        user = "root";
        password = "root";
        dbname = "wjbu";
        tbnameAV = "wjbu.av";
        tbnameVA = "wjbu.va";
        colmId = "id";
        colmWord = "word";
        colmHTML = "html";
        colmDesc = "description";
        colmProno = "pronounce";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            state = conn.createStatement();

        } catch (SQLException | ClassNotFoundException throwables) {
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
