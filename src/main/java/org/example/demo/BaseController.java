package org.example.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public abstract class BaseController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @FXML
    protected Button musicGroupsMenuButton, repertoireMenuButton, concertsMenuButton, artistsMenuButton, exitButton;

    @FXML
    public void initializeMenuButtons() {
        // Устанавливаем обработчики событий для кнопок меню
        musicGroupsMenuButton.setOnAction(event ->
                SceneSwitcher.switchScene(musicGroupsMenuButton, "pages/music-groups-view.fxml")
        );
        repertoireMenuButton.setOnAction(event ->
                SceneSwitcher.switchScene(repertoireMenuButton, "pages/repertoire-view.fxml")
        );
        concertsMenuButton.setOnAction(event ->
                SceneSwitcher.switchScene(concertsMenuButton, "pages/concerts-view.fxml")
        );
        artistsMenuButton.setOnAction(event ->
                SceneSwitcher.switchScene(artistsMenuButton, "pages/artists-view.fxml")
        );

        exitButton.setOnMouseClicked(event -> {
            logger.info("Нажата кнопка выхода из приложения.");
            Platform.exit();
            logger.info("Приложение завершило работу.");
        });
    }

    public boolean confirmDeletion(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType okButton = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == okButton;
    }

    protected void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Метод для отображения всплывающего окна
    protected void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
