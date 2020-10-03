package sample;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.sun.glass.ui.EventLoop;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import java.sql.*;

public class Controller implements Initializable {

    @FXML
    public Button btSearch = new Button();

    @FXML
    public TextField tfSearch = new TextField();

    @FXML
    public ListView<String> lvTarget = new ListView<>();

    @FXML
    public TextArea taMeaning = new TextArea();

    Map<String, String> dictionary = new HashMap<>();

    public String lookUpInDatabase(String word) {
        //String query = "SELECT * FROM " + DatabaseConfig.DatabaseName +
        return "";
    }

    @Override
    public void initialize(URL location, ResourceBundle recourses) {
        initializeDictionary();

        btSearch.setOnMouseClicked(mouseEvent -> {
            String searchedWord = tfSearch.getText().trim();
            if (searchedWord != null && searchedWord.equals("") == false) {
                taMeaning.setText(dictionary.get(searchedWord));
            }
        });

        tfSearch.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchedWord = tfSearch.getText().trim();
                if (searchedWord != null && searchedWord.equals("") == false) {
                    taMeaning.setText(dictionary.get(searchedWord));
                }
            }
        });

        lvTarget.setOnMouseClicked(mouseEvent -> {
            String searchedWord = lvTarget.getSelectionModel().getSelectedItem();
            if (searchedWord != null && searchedWord.equals("") == false) {
                taMeaning.setText(dictionary.get(searchedWord));
            }
        });
    }

    public void initializeDictionary() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wjbu", "root", "root");
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM wjbu.dictev");

            while(rs.next()) {
                lvTarget.getItems().add(rs.getString("word"));
            }

            conn.close();
            state.close();
            rs.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }
}
