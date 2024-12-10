package org.example.demo.page_controllers.artists_buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.BaseController;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.objectclasses.Artist;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EditArtistController extends BaseController implements Initializable {

    @FXML
    private Button EditButton, CancelButton;

    @FXML
    private TextField nameTextField, phoneTextField;

    @FXML
    private ComboBox<MusicGroup> musicGroupComboBox;

    private Artist currentArtist; // Текущий репертуар для редактирования

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Загрузка списка музыкальных групп в ComboBox
        try {
            List<MusicGroup> musicGroups = DatabaseManager.getAllMusicGroups();
            musicGroupComboBox.getItems().addAll(musicGroups);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось загрузить список музыкальных групп.");
        }
    }

    // Метод для передачи текущего репертуара
    public void setArtist(Artist artist) {
        this.currentArtist = artist;
        initializeFormFields();
    }

    // Инициализация полей формы значениями из текущего репертуара
    private void initializeFormFields() {
        if (currentArtist != null) {
            musicGroupComboBox.setValue(currentArtist.getMusicGroup());
            nameTextField.setText(currentArtist.getName());
            phoneTextField.setText(currentArtist.getPhone());
        }
    }

    @FXML
    void EditButtonClick(ActionEvent event) {
        if (currentArtist == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не найден артист для редактирования.");
            return;
        }

        try {
            // Получение данных из формы
            MusicGroup selectedMusicGroup = musicGroupComboBox.getValue();
            String name = nameTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            // Обновления объекта Artist
            currentArtist.setMusicGroup(selectedMusicGroup);
            currentArtist.setName(name);
            currentArtist.setPhone(phone);

            // Вызов метода обновления
            DatabaseManager.updateArtist(currentArtist);

            // Закрыть окно после успешного изменения
            showInfoAlert("Успех", "Артист успешно изменён.");
            ((Stage) EditButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка", "Ошибка при обновлении артиста: " + e.getMessage());
        }
    }

    @FXML
    void CancelButtonClick(ActionEvent event) {
        // Закрываем окно
        ((Stage) CancelButton.getScene().getWindow()).close();
    }

    // Проверка корректности ввода
    private boolean validateInput() {
        if (musicGroupComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите музыкальную группу.");
            return false;
        }
        if (nameTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите имя артиста.");
            return false;
        }
        return true;
    }
}
