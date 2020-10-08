package sample;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import javax.swing.plaf.nimbus.State;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button btSearch;

    @FXML
    public TextField tfSearchedWord;

    @FXML
    public ListView<String> lvWords;

    @FXML
    public TextArea taMeaning;
    @FXML
    public getConnection connect= new getConnection();

    Map<String, String> dictionary = new HashMap<String, String>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeWordList();
        btSearch.setOnMouseClicked(event -> {
            System.out.println("Search!!!");
            String searchedWord = tfSearchedWord.getText().trim();
            if (!searchedWord.equals("")) {
                System.out.println("Searched World: " + searchedWord);
                String wordMeaning = dictionary.get(searchedWord);
                taMeaning.setText(wordMeaning);
            }
        });
        tfSearchedWord.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchedWord = tfSearchedWord.getText().trim();
                if (!searchedWord.equals("")) {
                    System.out.println("Searched World: " + searchedWord);
                    String wordMeaning = dictionary.get(searchedWord);
                    taMeaning.setText(wordMeaning);
                }
            }
             eventUpdate(tfSearchedWord.getText().trim());
                System.out.println(tfSearchedWord.getText().trim());
        });
        lvWords.setOnMouseClicked(event -> {
            String searchedWord = lvWords.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                System.out.println("Searched World: " + searchedWord);
                String wordMeaning = dictionary.get(searchedWord);
                taMeaning.setText(wordMeaning);
            }
        });
        lvWords.setOnMouseClicked(mouseEvent -> {
            String searchedWord = lvWords.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                taMeaning.setText(lookUpInDatabase(searchedWord));
            }
        });
    }

    public void initializeWordList() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "vucaotan200147");
            Statement state = myConn.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM dictionary.tbl_edict ORDER BY idx;");
            while (rs.next()) {
                lvWords.getItems().add(rs.getString("word"));
                System.out.println(rs.getString("word"));
            }

        } catch (Exception ect) {
            ect.printStackTrace();
        }
        lvWords.getItems().addAll(dictionary.keySet());
    }
    public void eventUpdate (String word) {
        try {
            String query = "SELECT * FROM dictionary.tbl_edict WHERE LOCATE('" + word + "',word) > 0;";
            connect.rs = connect.state.executeQuery(query);
            lvWords.getItems().clear();
            while(connect.rs.next()) {
                lvWords.getItems().add(connect.rs.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String lookUpInDatabase(String word) {
        try {
            String query = "SELECT * FROM dictionary.tbl_edict WHERE LOCATE('" + word + "',word) > 0;";
            connect.rs = connect.state.executeQuery(query);
            if (connect.rs.next())
                try {
                    return connect.rs.getString("detail");
                } catch (SQLException ect) {
                    ect.printStackTrace();
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