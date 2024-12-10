package org.example.demo.page_controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.BaseController;
import org.example.demo.SceneSwitcher;
import org.example.demo.database.DatabaseManager;
import org.example.demo.exceptions.LoadArtistsError;
import org.example.demo.exceptions.SelectItemError;
import org.example.demo.objectclasses.Artist;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.page_controllers.artists_buttons.EditArtistController;

import java.sql.SQLException;
import java.util.List;

public class ArtistsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ArtistsController.class);

    @FXML
    void initialize() {
        logger.info("Инициализация контроллера началась.");

        try {
            artistsTableMusicGroupCol.setCellValueFactory(new PropertyValueFactory<>("musicGroup"));
            artistsTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            artistsTablePhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

            // Инициализируем кнопки меню из базового класса
            initializeMenuButtons();

            loadArtistsFromDatabase();

            // Устанавливаем сортировку по умолчанию
            artistsTable.getSortOrder().add(artistsTableMusicGroupCol); // Устанавливаем сортировку на колонку
            artistsTableMusicGroupCol.setSortType(TableColumn.SortType.ASCENDING); // Сортировка по возрастанию

            // Отключаем сортировку для каждого столбца таблицы
            for (TableColumn<?, ?> column : artistsTable.getColumns()) {
                column.setSortable(false);
            }

            artistsTableMusicGroupCol.setSortable(true);
            artistsTable.sort();
            artistsTableMusicGroupCol.setSortable(false);

            logger.info("Инициализация контроллера завершена успешно.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации контроллера: ", e);
        }
    }

    @FXML
    private TableView<Artist> artistsTable;
    @FXML
    private TableColumn<Artist, MusicGroup> artistsTableMusicGroupCol;
    @FXML
    private TableColumn<Artist, String> artistsTableNameCol;
    @FXML
    private TableColumn<Artist, Integer> artistsTablePhoneCol;

    @FXML
    public Button AddButton, EditButton, DeleteButton;

    @FXML
    void AddButtonClick(ActionEvent event) throws LoadArtistsError {
        logger.info("Открытие окна добавления артиста.");
        SceneSwitcher.openScene("pages/artists-buttons/add-artist-view.fxml", "Добавить артиста");
        loadArtistsFromDatabase(); // Обновляем данные таблицы

        artistsTableMusicGroupCol.setSortable(true);
        artistsTable.sort();
        artistsTableMusicGroupCol.setSortable(false);
    }

    @FXML
    void DeleteButtonClick(ActionEvent event) {
        try {
            Artist selectedArtist = selectItem();

            // Подтверждение удаления (опционально)
            boolean confirm = confirmDeletion("Удаление артиста", "Вы уверены, что хотите удалить артиста: " + selectedArtist + "?");
            if (!confirm) {
                logger.info("Удаление отменено пользователем.");
                return;
            }

            DatabaseManager.deleteArtist(selectedArtist);
            loadArtistsFromDatabase();
            logger.info("Артиста успешно удалён.");

            artistsTableMusicGroupCol.setSortable(true);
            artistsTable.sort();
            artistsTableMusicGroupCol.setSortable(false);
        } catch (SQLException | SelectItemError | LoadArtistsError e) {
            logger.error("Ошибка при удалении артиста: ", e);
            showErrorAlert("Ошибка удаления", e.getMessage());
        }
    }

    @FXML
    void EditButtonClick(ActionEvent event) throws SelectItemError, LoadArtistsError {
        Artist selectedArtist = selectItem();

        SceneSwitcher.openScene("pages/artists-buttons/edit-artist-view.fxml", "Редактировать артиста", controller -> {
            EditArtistController editController = (EditArtistController) controller;
            editController.setArtist(selectedArtist); // Передача выбранной группы в контроллер
        });

        loadArtistsFromDatabase();

        artistsTableMusicGroupCol.setSortable(true);
        artistsTable.sort();
        artistsTableMusicGroupCol.setSortable(false);
    }

    public Artist selectItem() throws SelectItemError {
        logger.debug("Выбор записи из таблицы.");
        Artist selectedItem = artistsTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            logger.info("Выбрана запись: {}", selectedItem);
            return selectedItem;
        } else {
            logger.warn("Попытка выбрать запись, но ничего не выбрано.");
            throw new SelectItemError();
        }
    }

    private void loadArtistsFromDatabase() throws LoadArtistsError {
        try {
            List<Artist> artists = DatabaseManager.getArtist();
            artistsTable.getItems().setAll(artists);
            logger.info("Данные успешно загружены из базы.");
        } catch (SQLException e) {
            logger.error("Ошибка загрузки данных из базы: ", e);
            throw new LoadArtistsError("Ошибка загрузки данных из базы.", e);
        }
    }
}
