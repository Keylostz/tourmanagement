package org.example.demo.page_controllers.artists_buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.BaseController;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.objectclasses.Artist;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddArtistController extends BaseController implements Initializable {

    private static final Logger logger = LogManager.getLogger(AddArtistController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMusicGroups();
    }

    @FXML
    private Button AddButton, CancelButton;

    @FXML
    private TextField nameTextField, phoneTextField;

    @FXML
    private ComboBox<MusicGroup> musicGroupComboBox;

    // Загрузка списка музыкальных групп в ComboBox
    private void loadMusicGroups() {
        try {
            // Получаем список музыкальных групп из базы данных
            List<MusicGroup> musicGroups = DatabaseManager.getAllMusicGroups();
            if (!musicGroups.isEmpty()) {
                musicGroupComboBox.getItems().addAll(musicGroups);
            } else {
                System.err.println("Список музыкальных групп пуст.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при загрузке музыкальных групп: " + e.getMessage());
        }
    }

    @FXML
    void AddButtonClick(ActionEvent event) {
        try {
            // Получение данных из формы
            MusicGroup selectedMusicGroup = musicGroupComboBox.getValue();
            String name = nameTextField.getText().trim();
            String phone = phoneTextField.getText().trim();

            // Проверка на пустые обязательные поля
            if (selectedMusicGroup == null || name.isEmpty() || phone.isEmpty()) {
                showErrorAlert("Ошибка", "Все поля должны быть заполнены.");
                return;
            }

            // Создание объекта Artist
            Artist artist = new Artist(selectedMusicGroup, name, phone);

            // Вызов метода добавления
            DatabaseManager.addArtist(artist);

            // Закрыть окно после успешного добавления
            showInfoAlert("Успех", "Артист успешно добавлен.");
            ((Stage) AddButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка", "Ошибка при добавлении артиста: " + e.getMessage());
        }
    }

    @FXML
    void CancelButtonClick(ActionEvent event) {
        // Закрываем окно при нажатии на кнопку "Отмена"
        ((Stage) CancelButton.getScene().getWindow()).close();
    }
}
