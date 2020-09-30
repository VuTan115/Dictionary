package sample;

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
        dictionary.put("hello", "Xin chào");
        dictionary.put("thank you", "Cảm ơn");
        dictionary.put("school", "Trường học");
        dictionary.put("class", "Lớp học");
        lvTarget.getItems().addAll(dictionary.keySet());
    }
}
