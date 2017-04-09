package team_f.client.gui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import team_f.client.configuration.Configuration;

public class Common {
    public static void closeApp(Stage primaryStage, Configuration configuration) {
        AppConfiguration.saveConfiguration(primaryStage, configuration);

        Platform.exit();
        System.exit(0);
    }

    public static void closeAppWithWarning(Event event, Stage primaryStage, Configuration _configuration) {
        boolean close = true;

        if(_configuration.getShowCloseWarning()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Beenden");
            alert.setHeaderText("Beenden");
            alert.setContentText("Wollen Sie die Anwendung wirklich schließen?");

            ButtonType result = alert.showAndWait().get();

            if(result == ButtonType.OK) {
                close = true;
            } else {
                close = false;
            }
        }

        if(close) {
            Common.closeApp(primaryStage, _configuration);
        } else {
            if(event != null) {
                event.consume();
            }
        }
    }
}
