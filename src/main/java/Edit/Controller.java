package Edit;

import DatabaseConn.MySQLCutie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    int id;

    String chosenTb = "";
    String tmpMeanig = "";
    String tmpWord = "";

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    private CheckBox cbAV;

    @FXML
    private CheckBox cbVA;

    @FXML
    private TextField tfWord;

    @FXML
    private HTMLEditor htmlE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (MySQLCutie.currentWord.equals("")) {
            htmlE.setHtmlText("<html dir=\"ltr\"><head></head><body "
                    + "contenteditable=\"true\"><p><font face=\"Segoe UI\" size=\"4\">"
                    + "<b>&lt;Word&gt;</b></font></p><p><font face=\"Segoe UI\" size=\"4\">"
                    + "<b>&lt;Description&gt;</b></font></p><br></body></html>\n");
            cbAV.setSelected(true);
            chosenTb = MySQLCutie.tbnameAV;
        } else {
            tmpWord = MySQLCutie.currentWord;
            tmpMeanig = MySQLCutie.currentMeaning;
            tfWord.setText(tmpWord);
            htmlE.setHtmlText(tmpMeanig);

            if (MySQLCutie.currenttb.equals(MySQLCutie.tbnameAV)) {
                chosenTb = MySQLCutie.tbnameAV;
                cbAV.setSelected(true);
                cbVA.setSelected(false);
            } else {
                chosenTb = MySQLCutie.tbnameVA;
                cbVA.setSelected(true);
                cbAV.setSelected(false);
            }
        }
        checkCheckBoxLanguage();

        btCancelHandle();
    }

    @FXML
    void checkCheckBoxLanguage() {
        cbVA.setOnMouseClicked(mouseEvent -> {
            cbAV.setSelected(false);
            chosenTb = MySQLCutie.tbnameVA;
        });
        cbAV.setOnMouseClicked(mouseEvent -> {
            cbVA.setSelected(false);
            chosenTb = MySQLCutie.tbnameAV;
        });
    }

    /** Check if s was in database or not. */
    private boolean checkEdit(String s) {
        if (s.charAt(0) == '\'') {
            s = "\\" + s;
        }
        String query = "SELECT * FROM " + chosenTb + " WHERE " + MySQLCutie.colmWord
                + " LIKE CONCAT(CONVERT('" + s + "', BINARY));";
        try {
            MySQLCutie.rs = MySQLCutie.state.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (MySQLCutie.rs.next()) {
                tmpMeanig = MySQLCutie.rs.getString(MySQLCutie.colmHTML);
                id = MySQLCutie.rs.getInt(MySQLCutie.colmId);
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @FXML
    public void btSaveHandle() {
        Stage stage = (Stage) btCancel.getScene().getWindow();
        btSave.setOnMouseClicked(mouseEvent -> {
            /* Failed alert. */
            if (tfWord.getText().trim().equals("") || htmlE.getHtmlText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Maybe you have filled incorrectly. Please check again.");
                alert.showAndWait();
            } else
                if (!stage.getTitle().equals("Edit Window") && checkEdit(tfWord.getText().trim())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Change to edit");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to change to Edit Window?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.isPresent() && action.get() == ButtonType.OK) {
                    stage.setTitle("Edit Window");
                    tfWord.setText(tfWord.getText().trim());
                    htmlE.setHtmlText(tmpMeanig);
                }
            } else
                /* Added. */
                {
                    if (stage.getTitle().equals("Add Window")) {
                        String tmp = tfWord.getText().trim();
                        if (tmp.charAt(0) == '\'') tmp = "\\" + tmp;
                        String query = "INSERT INTO `" + chosenTb + "` (`word`, `html`) VALUES ('" + tmp
                                + "', '<h1>" + tfWord.getText().trim() + "</h1><h2></h2>" + htmlE.getHtmlText().trim() +"');\n";
                        String query1 = "ALTER TABLE " + chosenTb + " DROP `id`;\n";
                        String query2 = "alter table " + chosenTb + " oder by `word`;\n";
                        String query3 = "ALTER TABLE " + chosenTb + " AUTO_INCREMENT = 1;\n";
                        String query4 = "ALTER TABLE " + chosenTb + " ADD `id`"
                                + " int UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;";
                        try {
                            MySQLCutie.state.executeUpdate(query);
                            /* MySQLCutie.state.executeUpdate(query1);
                            System.out.println(1);
                            MySQLCutie.state.executeLargeUpdate(query2);
                            System.out.println(2);
                            MySQLCutie.state.executeUpdate(query3);
                            System.out.println(3);
                            MySQLCutie.state.executeUpdate(query4);
                            System.out.println(4);*/
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else {
                        checkEdit(tfWord.getText().trim());
                        String query = "UPDATE " + chosenTb + " SET " + MySQLCutie.colmHTML + " = '"
                                + htmlE.getHtmlText().trim() + "' WHERE (`id` = '" + id + "');";
                        try {
                            MySQLCutie.state.executeUpdate(query);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully!");
                    Optional<ButtonType> action = alert.showAndWait();
                    if(action.isPresent() && action.get() == ButtonType.OK) {
                    stage.close();
                }
            }
        });
    }

    @FXML
    public void btCancelHandle()
    {
        btCancel.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Closing confirmation");
            alert.setHeaderText(null);
            alert.setContentText("If closed, the updated data will be lost. Are you sure?");
            Optional<ButtonType> action = alert.showAndWait();
            if(action.isPresent() && action.get() == ButtonType.OK) {
                Stage stage = (Stage) btCancel.getScene().getWindow();
                stage.close();
            }
        });
    }
}
