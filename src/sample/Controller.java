package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import jdk.jfr.Event;

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

    @FXML
    ToggleGroup tgLanguage = new ToggleGroup();

    @FXML
    RadioMenuItem rmiEnToVi = new RadioMenuItem();

    @FXML
    RadioMenuItem rmiViToEn = new RadioMenuItem();

    MySQLCutie mysql = new MySQLCutie();

    String tb = mysql.tbnameAV;

    /**Taskbar tkb = new Taskbar(); */


    @Override
    public void initialize(URL location, ResourceBundle recourses) {
        initializeFXML();
        initializeDictionary();
    }

    /** First load all the database. */
    public void initializeDictionary() {
        try {
            lvTarget.getItems().clear();
            mysql.rs = mysql.state.executeQuery("SELECT * FROM " + tb + ";");
            while(mysql.rs.next()) {
                lvTarget.getItems().add(mysql.rs.getString(mysql.colmWord));
            }
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
                taMeaning.setText(lookUpInDatabase(searchedWord));
            }
        });

        /** Keyboard input. */
        tfSearch.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String searchedWord = tfSearch.getText().trim();
                if (!searchedWord.equals("")) {
                    taMeaning.setText(lookUpInDatabase(searchedWord));
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
                taMeaning.setText(lookUpInDatabase(searchedWord));
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
        try {
            String query = "SELECT * FROM " + tb + " WHERE " + mysql.colmWord + " LIKE ('" + word + "%');";
            mysql.rs = mysql.state.executeQuery(query);
            if (mysql.rs.next())
                try {
                    return mysql.rs.getString(mysql.colmDesc);
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
