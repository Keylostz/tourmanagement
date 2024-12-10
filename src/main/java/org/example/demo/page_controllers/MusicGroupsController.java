package org.example.demo.page_controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.BaseController;
import org.example.demo.SceneSwitcher;
import org.example.demo.database.DatabaseManager;
import org.example.demo.exceptions.SelectItemError;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.page_controllers.music_groups_buttons.EditMusicGroupController;

import java.sql.SQLException;


public class MusicGroupsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(MusicGroupsController.class);

    @FXML
    void initialize() {
        logger.info("Инициализация контроллера началась.");

        try {
            // Инициализация кнопок меню из базового класса
            initializeMenuButtons();

            // Настройка колонок таблицы
            musicGroupsTableNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            musicGroupsTableFormationYearCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFormationYear()));

            // Загрузка данных в таблицу
            loadMusicGroups();

            logger.info("Инициализация контроллера завершена успешно.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации контроллера: ", e);
        }
    }

    private void loadMusicGroups() {
        try {
            musicGroupsTable.getItems().setAll(DatabaseManager.getMusicGroups());
        } catch (SQLException e) {
            logger.error("Ошибка при загрузке данных музыкальных групп: ", e);
        }
    }

    @FXML
    void musicGroupAddButtonClick(ActionEvent event) {
        SceneSwitcher.openScene("pages/music-groups-buttons/add-music-group-view.fxml", "Добавить музыкальную группу");
        loadMusicGroups(); // Обновляем таблицу после добавления
    }

    @FXML
    void musicGroupEditButtonClick(ActionEvent event) throws SelectItemError {
        MusicGroup selectedGroup = selectItem();

        SceneSwitcher.openScene("pages/music-groups-buttons/edit-music-group-view.fxml", "Редактировать музыкальную группу", controller -> {
            EditMusicGroupController editController = (EditMusicGroupController) controller;
            editController.setSelectedGroup(selectedGroup); // Передача выбранной группы в контроллер
        });

        loadMusicGroups(); // Обновляем таблицу после редактирования
    }


    public MusicGroup selectItem() throws SelectItemError {
        logger.debug("Выбор записи из таблицы.");
        MusicGroup selectedItem = musicGroupsTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            logger.info("Выбрана запись: {}", selectedItem);
            return selectedItem;
        } else {
            logger.warn("Попытка выбрать запись, но ничего не выбрано.");
            throw new SelectItemError();
        }
    }

    @FXML
    private TableView<MusicGroup> musicGroupsTable;

    @FXML
    private TableColumn<MusicGroup, Integer> musicGroupsTableFormationYearCol;

    @FXML
    private TableColumn<MusicGroup, String> musicGroupsTableNameCol;

    @FXML
    void musicGroupDeleteButtonClick(ActionEvent event) {
        try {
            // Используем метод selectItem для получения выбранной записи
            MusicGroup selectedGroup = selectItem();

            // Подтверждение удаления (опционально)
            boolean confirm = confirmDeletion("Удаление группы", "Вы уверены, что хотите удалить группу: " + selectedGroup.getName() + "?");
            if (!confirm) {
                logger.info("Удаление отменено пользователем.");
                return;
            }

            // Удаляем запись из базы данных
            DatabaseManager.deleteMusicGroup(selectedGroup);

            // Обновляем таблицу
            loadMusicGroups();

            logger.info("Группа '{}' успешно удалена.", selectedGroup.getName());
        } catch (SelectItemError e) {
            logger.warn("Удаление невозможно: элемент не выбран.");
        } catch (Exception e) {
            logger.error("Ошибка при удалении музыкальной группы: ", e);
        }
    }
}
