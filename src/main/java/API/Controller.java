package API;

import DatabaseConn.MySQLCutie;
import com.darkprograms.speech.translator.GoogleTranslate;
import com.gtranslate.Audio;
import com.gtranslate.Language;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField tfInput;

    @FXML
    private TextArea taOutput;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfInput.setText(MySQLCutie.currentWord);
    }

    @FXML
    public void tfInputHandler() {
        tfInput.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                try {
                    String str = tfInput.getText().trim();
                    if (str.charAt(0) == '\'')
                        str = "\\" + str;
                    taOutput.setText(GoogleTranslate.translate("vi", str));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
