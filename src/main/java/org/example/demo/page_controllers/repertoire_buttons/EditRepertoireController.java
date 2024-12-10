package org.example.demo.page_controllers.repertoire_buttons;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.BaseController;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.Concert;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.objectclasses.Repertoire;
import org.example.demo.page_controllers.ConcertsController;
import org.example.demo.page_controllers.RepertoireController;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EditRepertoireController extends BaseController implements Initializable {

    @FXML
    private Button EditButton, CancelButton;

    @FXML
    private TextField trackTextField, chartPosTextField;

    @FXML
    private ComboBox<MusicGroup> musicGroupComboBox;

    private Repertoire currentRepertoire; // Текущий репертуар для редактирования

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
    public void setRepertoire(Repertoire repertoire) {
        this.currentRepertoire = repertoire;
        initializeFormFields();
    }

    // Инициализация полей формы значениями из текущего репертуара
    private void initializeFormFields() {
        if (currentRepertoire != null) {
            musicGroupComboBox.setValue(currentRepertoire.getMusicGroup());
            trackTextField.setText(currentRepertoire.getName());
            if (currentRepertoire.getChartPos() != null) {
                chartPosTextField.setText(String.valueOf(currentRepertoire.getChartPos()));
            }
        }
    }

    @FXML
    void EditButtonClick(ActionEvent event) {
        if (currentRepertoire == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Не найден репертуар для редактирования.");
            return;
        }

        try {
            // Получение данных из формы
            MusicGroup selectedMusicGroup = musicGroupComboBox.getValue();
            String name = trackTextField.getText().trim();
            Integer chartPos = null; // Используем Integer, чтобы учитывать null

            // Попытка преобразовать позицию в чарте, если поле не пустое
            if (!chartPosTextField.getText().trim().isEmpty()) {
                try {
                    chartPos = Integer.parseInt(chartPosTextField.getText().trim());
                } catch (NumberFormatException e) {
                    showErrorAlert("Ошибка", "Позиция в чарте должна быть числом или оставлена пустой.");
                    return;
                }
            }

            // Проверка на пустые обязательные поля
            if (selectedMusicGroup == null || name.isEmpty()) {
                showErrorAlert("Ошибка", "Все поля, кроме позиции в чарте, должны быть заполнены.");
                return;
            }

            // Обновления объекта Repertoire
            currentRepertoire.setMusicGroup(selectedMusicGroup);
            currentRepertoire.setName(name);
            currentRepertoire.setChartPos(chartPos);

            // Вызов метода обновления
            DatabaseManager.updateRepertoire(currentRepertoire);

            // Закрыть окно после успешного изменения
            showInfoAlert("Успех", "Репертуар успешно изменён.");
            ((Stage) EditButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка", "Ошибка при обновлении репертуара: " + e.getMessage());
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
        if (trackTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите название трека.");
            return false;
        }
        return true;
    }
}
