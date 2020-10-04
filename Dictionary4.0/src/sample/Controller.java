package sample;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    Map<String, String> dictionary = new HashMap<String, String>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btSearch.setOnMouseClicked(event -> {
            System.out.println("Search!!!");
            String searchedWord = tfSearchedWord.getText();
            if (searchedWord != null && !searchedWord.equals("")) {
                System.out.println("Searched World: " + searchedWord);
                String wordMeaning = dictionary.get(searchedWord);
                taMeaning.setText(wordMeaning);
            }
        });
        this.initializeWordList();
        lvWords.setOnMouseClicked(event -> {
            String searchedWord = lvWords.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                System.out.println("Searched World: " + searchedWord);
                String wordMeaning = dictionary.get(searchedWord);
                taMeaning.setText(wordMeaning);
            }
        });
    }

    public void initializeWordList() {
        dictionary.put("hello", "Xin chao");
        dictionary.put("thank you", "Cam on");
        dictionary.put("school", "Truong hoc");
        dictionary.put("class", "Lop hoc");
        //get connection.
        //Connection myConn= DriverManager.getConnection("Connection")
        lvWords.getItems().addAll(dictionary.keySet());
    }
}