package org.example.demo.page_controllers.repertoire_buttons;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.BaseController;
import org.example.demo.database.DatabaseManager;
import org.example.demo.objectclasses.Concert;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.objectclasses.Repertoire;
import org.example.demo.page_controllers.ConcertsController;
import org.example.demo.page_controllers.music_groups_buttons.AddMusicGroupController;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AddRepertoireController extends BaseController implements Initializable {

    private static final Logger logger = LogManager.getLogger(AddRepertoireController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMusicGroups();
    }

    @FXML
    private Button AddButton, CancelButton;

    @FXML
    private TextField trackTextField, chartPosTextField;

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

            // Создание объекта Repertoire
            Repertoire repertoire = new Repertoire(selectedMusicGroup, name, chartPos);

            // Вызов метода добавления
            DatabaseManager.addRepertoire(repertoire);

            // Закрыть окно после успешного добавления
            showInfoAlert("Успех", "Репертуар успешно добавлен.");
            ((Stage) AddButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Ошибка", "Ошибка при добавлении репертуара: " + e.getMessage());
        }
    }

    @FXML
    void CancelButtonClick(ActionEvent event) {
        // Закрываем окно при нажатии на кнопку "Отмена"
        ((Stage) CancelButton.getScene().getWindow()).close();
    }
}
