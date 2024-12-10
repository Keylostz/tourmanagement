package org.example.demo.page_controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlgraphics.util.MimeConstants;
import org.example.demo.*;
import org.example.demo.database.DatabaseManager;
import org.example.demo.exceptions.SelectItemError;
import org.example.demo.objectclasses.Concert;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.page_controllers.concerts_buttons.EditConcertController;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ConcertsController extends BaseController {

    private static final Logger logger = LogManager.getLogger(ConcertsController.class);

    @FXML
    void initialize() {
        logger.info("Инициализация контроллера началась.");

        try {
            concertsTableDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            concertsTableMusicGroupCol.setCellValueFactory(new PropertyValueFactory<>("musicGroup"));
            concertsTableCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
            concertsTableVenueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));
            concertsTableOrgNameCol.setCellValueFactory(new PropertyValueFactory<>("orgName"));
            concertsTableOrgPhoneCol.setCellValueFactory(new PropertyValueFactory<>("orgPhone"));

            // Инициализируем кнопки меню из базового класса
            initializeMenuButtons();

            loadConcertsFromDatabase();

            logger.info("Инициализация контроллера завершена успешно.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации контроллера: ", e);
        }
    }

    private void loadConcertsFromDatabase() {
        try {
            List<Concert> concerts = DatabaseManager.getConcerts();
            concertsTable.getItems().setAll(concerts);
            logger.info("Данные успешно загружены из базы.");
        } catch (SQLException e) {
            logger.error("Ошибка загрузки данных из базы: ", e);
            showErrorAlert("Ошибка загрузки данных", e.getMessage());
        }
    }

    @FXML
    private Button savePDFButton, importXMLButton, exportXMLButton;

    @FXML
    public TableView<Concert> concertsTable;
    @FXML
    private TableColumn<Concert, LocalDate> concertsTableDateCol;
    @FXML
    private TableColumn<Concert, MusicGroup> concertsTableMusicGroupCol;
    @FXML
    private TableColumn<Concert, String> concertsTableCityCol;
    @FXML
    private TableColumn<Concert, String> concertsTableVenueCol;
    @FXML
    private TableColumn<Concert, String> concertsTableOrgNameCol;
    @FXML
    private TableColumn<Concert, String> concertsTableOrgPhoneCol;

    @FXML
    void AddButtonClick(ActionEvent event) {
        logger.info("Открытие окна добавления концерта.");
        SceneSwitcher.openScene("pages/concerts-buttons/add-concert-view.fxml", "Добавить концерт");
        loadConcertsFromDatabase(); // Обновляем данные таблицы
    }

    @FXML
    void EditButtonClick(ActionEvent event) {
        try {
            Concert selectedConcert = selectItem();

            SceneSwitcher.openScene("pages/concerts-buttons/edit-concert-view.fxml", "Редактировать концерт", controller -> {
                EditConcertController editController = (EditConcertController) controller;
                editController.setConcert(selectedConcert); // Передача выбранной группы в контроллер
            });

            loadConcertsFromDatabase();
        } catch (SelectItemError e) {
            logger.error("Ошибка при выборе концерта для редактирования: ", e);
            showErrorAlert("Ошибка выбора", e.getMessage());
        }
    }

    @FXML
    void DeleteButtonClick(ActionEvent event) {
        try {
            Concert selectedConcert = selectItem();

            // Подтверждение удаления (опционально)
            boolean confirm = confirmDeletion("Удаление концерта", "Вы уверены, что хотите удалить концерт: " + selectedConcert + "?");
            if (!confirm) {
                logger.info("Удаление отменено пользователем.");
                return;
            }

            DatabaseManager.deleteConcert(selectedConcert);
            loadConcertsFromDatabase();
            logger.info("Концерт успешно удалён.");
        } catch (SelectItemError e) {
            logger.error("Ошибка при выборе концерта для удаления: ", e);
            showErrorAlert("Ошибка выбора", e.getMessage());
        } catch (SQLException e) {
            logger.error("Ошибка при удалении концерта: ", e);
            showErrorAlert("Ошибка удаления", e.getMessage());
        }
    }

    public Concert selectItem() throws SelectItemError {
        logger.debug("Выбор записи из таблицы.");
        Concert selectedItem = concertsTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            logger.info("Выбрана запись: {}", selectedItem);
            return selectedItem;
        } else {
            logger.warn("Попытка выбрать запись, но ничего не выбрано.");
            throw new SelectItemError();
        }
    }

    @FXML
    void exportXML(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File xmlFile = fileChooser.showSaveDialog(exportXMLButton.getScene().getWindow());
        generateXMLFromTable(xmlFile);
    }

    @FXML
    void savePDFButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File pdfFile = fileChooser.showSaveDialog(savePDFButton.getScene().getWindow());

        if (pdfFile != null) {
            try {
                // Определение местоположений временных файлов
                File tempXmlFile = File.createTempFile("concert-schedule", ".xml");
                File htmlFile = new File(pdfFile.getParent(), pdfFile.getName().replace(".pdf", "") + ".html");

                // Используем ExecutorService для управления потоками
                ExecutorService executor = Executors.newFixedThreadPool(3);

                // Первый этап: генерация XML
                CompletableFuture<Void> xmlGenerationTask = CompletableFuture.runAsync(() -> {
                    try {
                        generateXMLFromTable(tempXmlFile);
                        logger.info("XML успешно сгенерирован: " + tempXmlFile.getAbsolutePath());
                    } catch (Exception e) {
                        logger.error("Ошибка генерации XML: ", e);
                        throw new RuntimeException("Ошибка генерации XML", e);
                    }
                }, executor);

                // Второй этап: обработка XML (при необходимости)
                CompletableFuture<Void> xmlProcessingTask = xmlGenerationTask.thenRunAsync(() -> {
                    try {
                        // Здесь можно добавить дополнительную обработку XML
                        logger.info("XML обработан для отчета.");
                    } catch (Exception e) {
                        logger.error("Ошибка обработки XML: ", e);
                        throw new RuntimeException("Ошибка обработки XML", e);
                    }
                }, executor);

                // Третий этап: генерация HTML и PDF
                CompletableFuture<Void> reportGenerationTask = xmlProcessingTask.thenRunAsync(() -> {
                    try {
                        generatePDF(tempXmlFile, pdfFile);
                        generateHTML(tempXmlFile, htmlFile);
                        logger.info("PDF и HTML отчеты успешно созданы.");
                    } catch (Exception e) {
                        logger.error("Ошибка создания отчетов: ", e);
                        throw new RuntimeException("Ошибка создания отчетов", e);
                    }
                }, executor);

                // Завершаем выполнение и уведомляем пользователя
                reportGenerationTask.whenComplete((result, error) -> {
                    executor.shutdown(); // Завершаем работу ExecutorService

                    if (error == null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Успешно");
                            alert.setHeaderText(null);
                            alert.setContentText("PDF и HTML файлы успешно сохранены!");
                            alert.showAndWait();
                        });
                    } else {
                        logger.error("Общая ошибка при создании отчетов: ", error);
                        Platform.runLater(() -> showErrorAlert("Ошибка", "Произошла ошибка при создании отчетов."));
                    }
                });
            } catch (Exception e) {
                logger.error("Общая ошибка при сохранении: ", e);
                showErrorAlert("Ошибка", "Произошла ошибка при сохранении файлов.");
            }
        }
    }

    public void generateXMLFromTable(File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Создаем корневой элемент
        org.w3c.dom.Element rootElement = doc.createElement("Concerts");
        doc.appendChild(rootElement);

        // Добавляем элементы из таблицы
        for (Concert concert : concertsTable.getItems()) {
            org.w3c.dom.Element concertElement = doc.createElement("Concert");
            rootElement.appendChild(concertElement);

            concertElement.appendChild(createElement(doc, "Date", concert.getDate().toString()));
            concertElement.appendChild(createElement(doc, "MusicGroup", concert.getMusicGroup().toString()));
            concertElement.appendChild(createElement(doc, "City", concert.getCity()));
            concertElement.appendChild(createElement(doc, "Venue", concert.getVenue()));
            concertElement.appendChild(createElement(doc, "OrgName", concert.getOrgName()));
            concertElement.appendChild(createElement(doc, "OrgPhone", concert.getOrgPhone()));
        }

        // Сохранение XML в файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

    void generatePDF(File xmlFile, File pdfFile) throws Exception {
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

        try (FileOutputStream out = new FileOutputStream(pdfFile)) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource("src/main/resources/org/example/demo/concert-template.xsl"));

            Source src = new StreamSource(xmlFile);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        }
    }

    void generateHTML(File xmlFile, File htmlFile) throws TransformerException {
        try (FileOutputStream out = new FileOutputStream(htmlFile)) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource("src/main/resources/org/example/demo/concert-template-html.xsl"));

            Source src = new StreamSource(xmlFile);
            Result res = new StreamResult(out);
            transformer.transform(src, res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private org.w3c.dom.Element createElement(Document doc, String name, String value) {
        org.w3c.dom.Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }
}