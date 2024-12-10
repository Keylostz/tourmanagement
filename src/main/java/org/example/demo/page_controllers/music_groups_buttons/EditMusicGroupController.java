package org.example.demo.page_controllers.music_groups_buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.MusicGroup;

public class EditMusicGroupController {

    private MusicGroup selectedGroup;

    @FXML
    private TextField groupNameField;

    @FXML
    private TextField groupYearField;

    @FXML
    private Button editButton, cancelButton;

    public void setSelectedGroup(MusicGroup group) {
        this.selectedGroup = group;
        groupNameField.setText(group.getName());
        groupYearField.setText(String.valueOf(group.getFormationYear()));
    }

    @FXML
    void editButtonClick(ActionEvent event) {
        String name = groupNameField.getText();
        int year;

        try {
            year = Integer.parseInt(groupYearField.getText());
        } catch (NumberFormatException e) {
            return;
        }

        try {
            selectedGroup.setName(name);
            selectedGroup.setFormationYear(year);

            DatabaseManager.updateMusicGroup(selectedGroup);

            ((Stage) editButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelButtonClick(ActionEvent event) {
        cancelButton.getScene().getWindow().hide();
    }
}
