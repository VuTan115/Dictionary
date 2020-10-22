package MainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainStage.fxml"));
        primaryStage.setTitle("Dictionary");
        //primaryStage.getIcons().add(new Image("ASset/book.png"));
        //primaryStage.getIcons().add(new Image("https://www.flaticon.com/svg/static/icons/svg/3528/3528186.svg"));
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
