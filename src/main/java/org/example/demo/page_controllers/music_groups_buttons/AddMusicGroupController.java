package org.example.demo.page_controllers.music_groups_buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.MusicGroup;

public class AddMusicGroupController {

    private static final Logger logger = LogManager.getLogger(AddMusicGroupController.class);

    @FXML
    private TextField groupNameField;

    @FXML
    private TextField groupYearField;

    @FXML
    private Button addButton, cancelButton;

    @FXML
    void addButtonClick(ActionEvent event) {
        String name = groupNameField.getText();
        int year;

        try {
            year = Integer.parseInt(groupYearField.getText());
        } catch (NumberFormatException e) {
            logger.error("Год должен быть числом", e);
            return;
        }

        try {
            MusicGroup group = new MusicGroup();
            group.setName(name);
            group.setFormationYear(year);

            DatabaseManager.addMusicGroup(group);
            logger.info("Музыкальная группа добавлена.");
            ((Stage) addButton.getScene().getWindow()).close();
        } catch (Exception e) {
            logger.error("Ошибка при добавлении музыкальной группы: ", e);
        }
    }

    @FXML
    void cancelButtonClick(ActionEvent event) {
        // Закрытие окна
        cancelButton.getScene().getWindow().hide();
    }
}
