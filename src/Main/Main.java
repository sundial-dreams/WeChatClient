package Main;

import Controller.Controller;
import javafx.application.*;
import javafx.stage.Stage;

import java.lang.String;

public class Main extends Application {
    @Override
    public void start(Stage window) throws Exception {
        new Controller().exec();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
