package org.example.demo.page_controllers.concerts_buttons;

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
import java.util.List;
import java.util.ResourceBundle;

public class EditConcertController implements Initializable {

    @FXML
    private Button editConcertButton, cancelButton;

    @FXML
    private TextField cityTextField, organizerPNTextField, organizerTextField, venueTextField;

    @FXML
    private DatePicker concertDatePicker;

    @FXML
    private ComboBox<MusicGroup> musicGroupComboBox;

    private ConcertsController mainController; // Ссылка на основной контроллер
    private Concert currentConcert; // Текущий концерт для редактирования

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

    // Метод для передачи текущего концерта
    public void setConcert(Concert concert) {
        this.currentConcert = concert;
        initializeFormFields();
    }

    // Метод для передачи основного контроллера
    public void setMainController(ConcertsController mainController) {
        this.mainController = mainController;
    }

    // Инициализация полей формы значениями из текущего концерта
    private void initializeFormFields() {
        if (currentConcert != null) {
            concertDatePicker.setValue(currentConcert.getDate());
            cityTextField.setText(currentConcert.getCity());
            venueTextField.setText(currentConcert.getVenue());
            organizerTextField.setText(currentConcert.getOrgName());
            organizerPNTextField.setText(currentConcert.getOrgPhone());
            musicGroupComboBox.setValue(currentConcert.getMusicGroup());
        }
    }

    @FXML
    void editConcertButtonClick(ActionEvent event) {
        if (currentConcert == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не найден концерт для редактирования.");
            return;
        }

        // Проверка корректности ввода
        if (!validateInput()) {
            return;
        }

        // Обновление данных концерта
        try {
            currentConcert.setDate(concertDatePicker.getValue());
            currentConcert.setCity(cityTextField.getText());
            currentConcert.setVenue(venueTextField.getText());
            currentConcert.setOrgName(organizerTextField.getText());
            currentConcert.setOrgPhone(organizerPNTextField.getText());
            currentConcert.setMusicGroup(musicGroupComboBox.getValue());

            DatabaseManager.updateConcert(currentConcert);

            // Закрытие окна
            ((Stage) editConcertButton.getScene().getWindow()).close();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось обновить данные концерта.");
        }
    }

    @FXML
    void cancelButtonClick(ActionEvent event) {
        // Закрываем окно
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    // Проверка корректности ввода
    private boolean validateInput() {
        if (concertDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите дату концерта.");
            return false;
        }
        if (musicGroupComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите музыкальную группу.");
            return false;
        }
        if (cityTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите город.");
            return false;
        }
        if (venueTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите место проведения.");
            return false;
        }
        if (organizerTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите организатора.");
            return false;
        }
        if (organizerPNTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите номер телефона организатора.");
            return false;
        }
        return true;
    }

    // Метод для отображения всплывающего окна
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
