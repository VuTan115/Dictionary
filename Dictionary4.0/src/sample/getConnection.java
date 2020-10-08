package sample;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class getConnection {
    String url, user, password;
    public Connection conn;
    public Statement state;
    public ResultSet rs = null;

    public getConnection() {
        url = "jdbc:mysql://localhost:3306/dictionary";
        user = "root";
        password = "vucaotan200147";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            state = conn.createStatement();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Destructor() {
        try {
            state.close();
            conn.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
