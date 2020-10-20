package MainPackage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;

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
    public WebView wvMeaning = new WebView();

    @FXML
    ToggleGroup tgLanguage = new ToggleGroup();

    @FXML
    RadioMenuItem rmiEnToVi = new RadioMenuItem();

    @FXML
    RadioMenuItem rmiViToEn = new RadioMenuItem();

    @FXML
    MenuItem miAdd = new MenuItem();

    @FXML
    MenuItem miDelete = new MenuItem();

    private MySQLCutie mysql = new MySQLCutie();

    private String tb = mysql.tbnameAV;



    @Override
    public void initialize(URL location, ResourceBundle recourses) {
        initializeFXML();
        initializeDictionary();
        eventHandler();
    }

    /** First load all the database. */
    public void initializeDictionary() {
        int i = 0;
        try {
            lvTarget.getItems().clear();
            mysql.rs = mysql.state.executeQuery("SELECT " + mysql.colmWord
                    + " FROM " + tb
                    + " ORDER BY " + mysql.colmWord + ";");
            while(mysql.rs.next() && i++ < 15000) {
                lvTarget.getItems().add(mysql.rs.getString(mysql.colmWord));
                for (int j = 0; j < 6; j++) mysql.rs.next();
            }
            lvTarget.getItems().add("...");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** FXML initialize. */
    public void initializeFXML() {
        rmiEnToVi.setToggleGroup(tgLanguage);
        rmiViToEn.setToggleGroup(tgLanguage);
        rmiEnToVi.setSelected(true);
    }

    /** Check language. */
    public void checkLanguage() {
        if(rmiEnToVi.isSelected() && tb.equals(mysql.tbnameVA)) {
            tb = mysql.tbnameAV;
            initializeDictionary();
        }
        if(rmiViToEn.isSelected() && tb.equals(mysql.tbnameAV)) {
            tb = mysql.tbnameVA;
            initializeDictionary();
        }
    }

    /** Handle event. */
    public void eventHandler() {

        /** Click button Search. */
        btSearch.setOnMouseClicked(mouseEvent -> {

            String searchedWord = tfSearch.getText().trim();
            if (!searchedWord.equals("")) {
                LoadHtmlContent(lookUpInDatabase(searchedWord));
            }
        });

        /** Keyboard input. */
        tfSearch.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchedWord = tfSearch.getText().trim();
                if (!searchedWord.equals("")) {
                    LoadHtmlContent(lookUpInDatabase(searchedWord));
                }
            }

            if (!tfSearch.getText().trim().equals("")) {
                eventUpdate(tfSearch.getText().trim());
            }
            else {
                lvTarget.getItems().clear();
                initializeDictionary();
            }
        });

        /** Assign the word. */
        lvTarget.setOnMouseClicked(mouseEvent -> {
            String searchedWord = lvTarget.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                LoadHtmlContent(lookUpInDatabase(searchedWord));
            }
        });
    }

    /** Minimize the word list. */
    public void eventUpdate (String word) {
        try {
            String query = "SELECT * FROM " + tb + " WHERE " + mysql.colmWord + " LIKE ('" + word + "%');";
            mysql.rs = mysql.state.executeQuery(query);
            lvTarget.getItems().clear();
            int tmp = 0;
            while(mysql.rs.next() && tmp < 10) {
                lvTarget.getItems().add(mysql.rs.getString(mysql.colmWord));
                tmp++;
            }
            if (mysql.rs.next()) lvTarget.getItems().add("...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Find out the word in database. */
    public String lookUpInDatabase(String word) {
        if (word.charAt(0) == '\'') {
            String tmp = "\\" + word;
            word = tmp;
        }
        try {
            String query = "SELECT * FROM " + tb + " WHERE " + mysql.colmWord + " LIKE ('" + word + "%');";
            mysql.rs = mysql.state.executeQuery(query);
            if (mysql.rs.next())
                try {
                    return mysql.rs.getString(mysql.colmHTML);
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

    void LoadHtmlContent(String word) {
        wvMeaning.getEngine().loadContent(word);
    }

    /** Add new word. */
    public void addWord() {

    }

    /** Delete word. */
    public void deleteWord() {

    }

}
