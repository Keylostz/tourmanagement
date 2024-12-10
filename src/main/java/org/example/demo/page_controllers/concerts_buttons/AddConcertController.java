package org.example.demo.page_controllers.concerts_buttons;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.Concert;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.page_controllers.ConcertsController;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AddConcertController implements Initializable {

    @FXML
    private Button addConcertButton, cancelAddButton;

    @FXML
    private TextField cityTextField, organizerPNTextField, organizerTextField, venueTextField;

    @FXML
    private DatePicker concertDatePicker;

    @FXML
    private ComboBox<MusicGroup> musicGroupComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMusicGroups();
    }

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
    void addConcertButtonClick(ActionEvent event) {
        try {
            // Получение данных из формы
            LocalDate date = concertDatePicker.getValue();
            MusicGroup selectedMusicGroup = musicGroupComboBox.getValue();
            String city = cityTextField.getText().trim();
            String venue = venueTextField.getText().trim();
            String organizer = organizerTextField.getText().trim();
            String organizerPhone = organizerPNTextField.getText().trim();

            // Проверка на пустые поля
            if (date == null || selectedMusicGroup == null || city.isEmpty() || venue.isEmpty() || organizer.isEmpty() || organizerPhone.isEmpty()) {
                showErrorAlert("Ошибка", "Все поля должны быть заполнены.");
                return;
            }

            // Создание объекта Concert
            Concert concert = new Concert(date, selectedMusicGroup, city, venue, organizer, organizerPhone);

            // Вызов метода добавления
            DatabaseManager.addConcert(concert);

            // Закрыть окно после успешного добавления
            showInfoAlert("Успех", "Концерт успешно добавлен.");
            ((Stage) addConcertButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка", "Ошибка при добавлении концерта: " + e.getMessage());
        }
    }


    @FXML
    void cancelAddButtonClick(ActionEvent event) {
        // Закрываем окно при нажатии на кнопку "Отмена"
        ((Stage) cancelAddButton.getScene().getWindow()).close();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
