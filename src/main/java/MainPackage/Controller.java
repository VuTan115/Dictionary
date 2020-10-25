package MainPackage;

import main.java.DatabaseConn.MySQLCutie;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import java.sql.*;


public class Controller implements Initializable {

    @FXML
    public Button btSearch = new Button();

    @FXML
    public Button btBack = new Button();

    @FXML
    public Button btAPI = new Button();

    @FXML
    public Button btSpeaker;

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
    RadioMenuItem rmiViToEn;

    @FXML
    MenuItem miClose;

    @FXML
    MenuItem miAdd;

    @FXML
    MenuItem miEdit = new MenuItem();

    @FXML
    MenuItem miDelete = new MenuItem();

    private List<String> previousWords = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle recourses) {
        MySQLCutie.currenttb = MySQLCutie.tbnameAV;
        initializeFXML();
        initializeDictionary();
        eventHandler();
    }

    /** First load all the database. */
    public void initializeDictionary() {
        int i = 0;
        try {
            lvTarget.getItems().clear();
            MySQLCutie.rs = MySQLCutie.state.executeQuery("SELECT " + MySQLCutie.colmWord
                    + " FROM " + MySQLCutie.currenttb + ";");
            while(MySQLCutie.rs.next() && i++ < 15000) {
                lvTarget.getItems().add(MySQLCutie.rs.getString(MySQLCutie.colmWord));
                for (int j = 0; j < 6; j++) MySQLCutie.rs.next();
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
        btBack.setDisable(true);
        wvMeaning.getEngine().loadContent("");
        tsukareta();
        addWord();
        editWord();
        deleteWord();
    }

    /** Check language. */
    public void checkLanguage() {
        if (rmiEnToVi.isSelected() && MySQLCutie.currenttb.equals(MySQLCutie.tbnameVA)) {
            MySQLCutie.currenttb = MySQLCutie.tbnameAV;
            initializeDictionary();
        }
        if (rmiViToEn.isSelected() && MySQLCutie.currenttb.equals(MySQLCutie.tbnameAV)) {
            MySQLCutie.currenttb = MySQLCutie.tbnameVA;
            initializeDictionary();
        }
    }

    /** Handle event. */
    public void eventHandler() {

        /* Click button Search. */
        btSearchHandle();

        /* Keyboard input. */
        keyboardInputHandle();

        /* Assign the word. */
        clickedHandle();

        /* Recall the previous words. */
        thePreviousWords();

        btSpeakHandle();

        btAPIHandler();

        contextMenu();
    }

    /** Minimize the word list. */
    public void eventUpdate (String word) {
        try {
            String query = "SELECT * FROM " + MySQLCutie.currenttb + " WHERE " + MySQLCutie.colmWord + " LIKE CONCAT(CONVERT('" + word + "', BINARY), '%');";
            MySQLCutie.rs = MySQLCutie.state.executeQuery(query);
            lvTarget.getItems().clear();
            int tmp = 0;
            while(MySQLCutie.rs.next() && tmp < 10) {
                lvTarget.getItems().add(MySQLCutie.rs.getString(MySQLCutie.colmWord));
                tmp++;
            }
            if (MySQLCutie.rs.next()) lvTarget.getItems().add("...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Find out the word in database. */
    public String lookUpInDatabase(String word) {
        if (word.charAt(0) == '\'') {
            word = "\\" + word;
        }
        try {
            String query = "SELECT * FROM " + MySQLCutie.currenttb + " WHERE " + MySQLCutie.colmWord
                    + " LIKE CONCAT(CONVERT('" + word + "', BINARY));";
            MySQLCutie.rs = MySQLCutie.state.executeQuery(query);
            if (MySQLCutie.rs.next())
                try {

                    /* For delete word. */
                    MySQLCutie.currentId = MySQLCutie.rs.getInt(MySQLCutie.colmId);

                    /* For previous words list. */
                    if (previousWords.isEmpty()) {
                        previousWords.add(word);
                        MySQLCutie.tbList.add(MySQLCutie.currenttb);
                    }
                    else if (!previousWords.get(previousWords.size() - 1).equals(word)) {
                        previousWords.add(word);
                        MySQLCutie.tbList.add(MySQLCutie.currenttb);
                    }
                    btBack.setDisable(previousWords.size() <= 1);

                    /* For add and edit word. */
                    MySQLCutie.currentWord = word;
                    MySQLCutie.currentMeaning = MySQLCutie.rs.getString(MySQLCutie.colmHTML);

                    return MySQLCutie.rs.getString(MySQLCutie.colmHTML);
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

    /** Load html meaning of the word. */
    void LoadHtmlContent(String word) {
        wvMeaning.getEngine().loadContent(word);
    }

    /** Click button Search. */
    void btSearchHandle() {
        btSearch.setOnMouseClicked(mouseEvent -> {
            String searchedWord = tfSearch.getText().trim();
            if (!searchedWord.equals("")) {
                LoadHtmlContent(lookUpInDatabase(searchedWord));
            }
        });
    }

    /** Keyboard input. */
    public void keyboardInputHandle() {
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
    }

    /** Assign the word. */
    public void clickedHandle() {
        lvTarget.setOnMouseClicked(mouseEvent -> {
            String searchedWord = lvTarget.getSelectionModel().getSelectedItem();
            if (searchedWord != null && !searchedWord.equals("")) {
                LoadHtmlContent(lookUpInDatabase(searchedWord));
            }
        });
    }

    /** Recall the previous words. */
    public void thePreviousWords () {
        btBack.setOnMouseReleased(mouseEvent -> {
            String searchedWord = previousWords.get(previousWords.size() - 2);

            /* Check the previous language. */
            if (!MySQLCutie.currenttb.equals(MySQLCutie.tbList.get(previousWords.size() - 2))) {
                MySQLCutie.currenttb = MySQLCutie.tbList.get(previousWords.size() - 2);
                if (rmiViToEn.isSelected()) rmiEnToVi.setSelected(true);
                else rmiViToEn.setSelected(true);
                initializeDictionary();
            }

            MySQLCutie.tbList.remove(previousWords.size() - 1);
            previousWords.remove(previousWords.size() - 1);
            if (!searchedWord.equals("")) {
                LoadHtmlContent(lookUpInDatabase(searchedWord));
            }
        });
    }

    /** Add word. */
    public void addWord() {
        miAdd.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                addH();
            }
        });
    }

    public void addH() {
        MySQLCutie.currentWord = "";
        MySQLCutie.currentMeaning = "";
        try {
            Parent rootEdit = FXMLLoader.load(getClass().getResource("/Edit.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Window");
            stage.setScene(new Scene(rootEdit, 600, 500));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Edit word. */
    public void editWord() {
        miEdit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                editH();
            }

        });
    }

    public void editH() {
        try {
            if (MySQLCutie.currentWord.equals("")) {
                MySQLCutie.currentWord = "";
                MySQLCutie.currentMeaning = "";
                try {
                    Parent rootEdit = FXMLLoader.load(getClass().getResource("/Edit.fxml"));
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Add Window");
                    stage.setScene(new Scene(rootEdit, 600, 500));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Parent rootEdit = FXMLLoader.load(getClass().getResource("/Edit.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Edit Window");
                stage.setScene(new Scene(rootEdit, 600, 500));
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Delete word. */
    public void deleteWord() {
        miDelete.setOnAction(mouseEvent -> {
            deleteH();
        });
    }

    public void deleteH() {
        if (!MySQLCutie.currentWord.equals("")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting confirmation");
            alert.setHeaderText(null);
            alert.setContentText("If deleted, the data will be lost. Are you sure?");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.isPresent() && action.get() == ButtonType.OK) {
                String query = "DELETE FROM " + MySQLCutie.currenttb + " WHERE (`id` = '" + MySQLCutie.currentId + "');";
                try {
                    MySQLCutie.state.executeUpdate(query);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public static boolean checkNetwork() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (MalformedURLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Network error");
            alert.setHeaderText(null);
            alert.setContentText("NO INTERNET ACCESS");
            alert.showAndWait();
            return false;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Network error");
            alert.setHeaderText(null);
            alert.setContentText("NO INTERNET ACCESS");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void btAPIHandler() {
        btAPI.setOnMouseClicked(mouseEvent -> {
            Parent rootEdit = null;
            try {
                rootEdit = FXMLLoader.load(getClass().getResource("/API.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(checkNetwork()) {

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("API Window");
                stage.setScene(new Scene(rootEdit, 600, 500));
                stage.show();
            }
        });
    }

    /**
     * button speak
     */
    public void btSpeakHandle() {
        btSpeaker.setOnMouseClicked(mouseEvent -> {
            if(checkNetwork()) {
                String searchedWord = MySQLCutie.currentWord;
                if (!searchedWord.equals("")) {
                    try {

                        if (MySQLCutie.currenttb.equals(MySQLCutie.tbnameAV)) {
                            Audio audio = Audio.getInstance();
                            InputStream sound = audio.getAudio(searchedWord + "&client=tw-ob", Language.ENGLISH);
                            audio.play(sound);
                        } else {
                            Audio audio = Audio.getInstance();
                            InputStream sound = audio.getAudio(searchedWord + "&client=tw-ob", Language.VIETNAMESE);
                            audio.play(sound);
                        }
                    } catch (IOException | JavaLayerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });

    }

    /** Context Menu. */
    public void contextMenu() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Add word");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addH();
            }
        });

        MenuItem item2 = new MenuItem("Delete word");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteH();
            }
        });

        MenuItem item3 = new MenuItem("Edit word");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editH();
            }
        });

        wvMeaning.setContextMenuEnabled(false);

        contextMenu.getItems().addAll(item1, item2, item3);

        wvMeaning.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(wvMeaning, event.getScreenX(), event.getScreenY());
            }
        });

    }

    /** Closing. */
    public void tsukareta() {
        miClose.setOnAction(mouseEvent -> {
            Stage stage = (Stage) btSearch.getScene().getWindow();
            stage.close();
        });
    }
}
