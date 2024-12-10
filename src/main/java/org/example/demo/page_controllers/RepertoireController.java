package org.example.demo.page_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.BaseController;
import org.example.demo.SceneSwitcher;
import org.example.demo.database.DatabaseManager;
import org.example.demo.exceptions.SelectItemError;
import org.example.demo.objectclasses.Repertoire;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.page_controllers.repertoire_buttons.EditRepertoireController;

import java.sql.SQLException;
import java.util.List;

public class RepertoireController extends BaseController {

    private static final Logger logger = LogManager.getLogger(RepertoireController.class);

    @FXML
    void initialize() {
        logger.info("Инициализация контроллера началась.");

        try {
            repertoireTableMusicGroupCol.setCellValueFactory(new PropertyValueFactory<>("musicGroup"));
            repertoireTableTrackCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            repertoireTableChartPosCol.setCellValueFactory(new PropertyValueFactory<>("chartPos"));

            repertoireTableChartPosCol.setCellFactory(col -> new TableCell<Repertoire, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null); // Оставляем пустую ячейку для пустых строк
                    } else if (item == null || item == 0) {
                        setText("-"); // Черточка для строк с данными и значением 0 или null
                    } else {
                        setText(item.toString()); // Отображаем значение, если оно не 0
                    }
                }
            });

            repertoireTableChartPosCol.setComparator((Integer a, Integer b) -> {
                if (a == null || a == 0) {
                    return (b == null || b == 0) ? 0 : 1; // Если `a` = 0 или null, отправить в конец
                }
                if (b == null || b == 0) {
                    return -1; // Если `b` = 0 или null, `a` идет выше
                }
                return Integer.compare(a, b); // Обычная сортировка чисел
            });

            // Инициализируем кнопки меню из базового класса
            initializeMenuButtons();

            loadRepertoireFromDatabase();

            // Устанавливаем сортировку по умолчанию
            repertoireTable.getSortOrder().add(repertoireTableChartPosCol); // Устанавливаем сортировку на колонку
            repertoireTableChartPosCol.setSortType(TableColumn.SortType.ASCENDING); // Сортировка по возрастанию

            // Применяем сортировку (это важно для отображения при загрузке)
            repertoireTable.sort();

            // Отключаем сортировку для каждого столбца таблицы
            for (TableColumn<?, ?> column : repertoireTable.getColumns()) {
                column.setSortable(false);
            }

            logger.info("Инициализация контроллера завершена успешно.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации контроллера: ", e);
        }
    }

    private void loadRepertoireFromDatabase() {
        try {
            List<Repertoire> repertoire = DatabaseManager.getRepertoire();
            repertoireTable.getItems().setAll(repertoire);
            logger.info("Данные успешно загружены из базы.");
        } catch (SQLException e) {
            logger.error("Ошибка загрузки данных из базы: ", e);
            showErrorAlert("Ошибка загрузки данных", e.getMessage());
        }
    }

    @FXML
    private TableView<Repertoire> repertoireTable;
    @FXML
    private TableColumn<Repertoire, MusicGroup> repertoireTableMusicGroupCol;
    @FXML
    private TableColumn<Repertoire, String> repertoireTableTrackCol;
    @FXML
    private TableColumn<Repertoire, Integer> repertoireTableChartPosCol;

    @FXML
    public Button AddButton, EditButton, DeleteButton;

    @FXML
    void AddButtonClick(ActionEvent event) {
        logger.info("Открытие окна добавления репертуара.");
        SceneSwitcher.openScene("pages/repertoire-buttons/add-repertoire-view.fxml", "Добавить репертуар");
        loadRepertoireFromDatabase(); // Обновляем данные таблицы

        repertoireTableChartPosCol.setSortable(true);
        repertoireTable.sort();
        repertoireTableChartPosCol.setSortable(false);
    }

    @FXML
    void DeleteButtonClick(ActionEvent event) {
        try {
            Repertoire selectedRepertoire = selectItem();

            // Подтверждение удаления (опционально)
            boolean confirm = confirmDeletion("Удаление репертуара", "Вы уверены, что хотите удалить репертуар: " + selectedRepertoire + "?");
            if (!confirm) {
                logger.info("Удаление отменено пользователем.");
                return;
            }

            DatabaseManager.deleteRepertoire(selectedRepertoire);
            loadRepertoireFromDatabase();
            logger.info("Репертуар успешно удалён.");

            repertoireTableChartPosCol.setSortable(true);
            repertoireTable.sort();
            repertoireTableChartPosCol.setSortable(false);
        } catch (SQLException | SelectItemError e) {
            logger.error("Ошибка при удалении репертуара: ", e);
            showErrorAlert("Ошибка удаления", e.getMessage());
        }
    }

    @FXML
    void EditButtonClick(ActionEvent event) throws SelectItemError {
        Repertoire selectedRepertoire = selectItem();

        SceneSwitcher.openScene("pages/repertoire-buttons/edit-repertoire-view.fxml", "Редактировать репертуар", controller -> {
            EditRepertoireController editController = (EditRepertoireController) controller;
            editController.setRepertoire(selectedRepertoire); // Передача выбранной группы в контроллер
        });

        loadRepertoireFromDatabase();

        repertoireTableChartPosCol.setSortable(true);
        repertoireTable.sort();
        repertoireTableChartPosCol.setSortable(false);
    }

    public Repertoire selectItem() throws SelectItemError {
        logger.debug("Выбор записи из таблицы.");
        Repertoire selectedItem = repertoireTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            logger.info("Выбрана запись: {}", selectedItem);
            return selectedItem;
        } else {
            logger.warn("Попытка выбрать запись, но ничего не выбрано.");
            throw new SelectItemError();
        }
    }
}
