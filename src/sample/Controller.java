package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
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

    MySQLCutie mysql = new MySQLCutie();



    @Override
    public void initialize(URL location, ResourceBundle recourses) {
        initializeDictionary();

        btSearch.setOnMouseClicked(mouseEvent -> {
            String searchedWord = tfSearch.getText().trim();
            if (!searchedWord.equals("")) {
                taMeaning.setText(lookUpInDatabase(searchedWord));
            }
        });

        tfSearch.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchedWord = tfSearch.getText().trim();
                if (!searchedWord.equals("")) {
                    taMeaning.setText(lookUpInDatabase(searchedWord));
                }
            }
                eventUpdate(tfSearch.getText().trim());
                System.out.println(tfSearch.getText().trim());
        });

        lvTarget.setOnMouseClicked(mouseEvent -> {
            String searchedWord = lvTarget.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                taMeaning.setText(lookUpInDatabase(searchedWord));
            }
        });
    }

    public void initializeDictionary() {
        try {
            mysql.rs = mysql.state.executeQuery("SELECT * FROM wjbu.dictev;");

            while(mysql.rs.next()) {
                lvTarget.getItems().add(mysql.rs.getString("word"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void eventUpdate (String word) {
        try {
            String query = "SELECT * FROM wjbu.dictev WHERE LOCATE('" + word + "',word) > 0;";
            mysql.rs = mysql.state.executeQuery(query);
            lvTarget.getItems().clear();
            while(mysql.rs.next()) {
                lvTarget.getItems().add(mysql.rs.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String lookUpInDatabase(String word) {
        try {
            String query = "SELECT * FROM wjbu.dictev WHERE LOCATE('" + word + "',word) > 0;";
            mysql.rs = mysql.state.executeQuery(query);
            if (mysql.rs.next())
                try {
                    return mysql.rs.getString("detail");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            else
            {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
