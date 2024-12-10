package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.stage.Modality;

import java.io.IOException;

public class SceneSwitcher {
    private static final Logger logger = LogManager.getLogger(SceneSwitcher.class);
    
    // Метод для переключения сцен
    public static void switchScene(Node sourceNode, String fxmlFilePath) {
        try {
            // Загружаем новую сцену из FXML файла
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFilePath));
            Parent root = loader.load();

            // Получаем текущую сцену
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            // Устанавливаем новую сцену
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка загрузки FXML файла: " + fxmlFilePath);
        }
    }

    /**
     * Открывает новую сцену.
     *
     * @param fxmlPath путь к FXML-файлу.
     * @param title    заголовок окна.
     */
    public static void openScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Делает окно модальным
            stage.showAndWait(); // Ожидает, пока окно не закроется
        } catch (IOException e) {
            logger.error("Ошибка при открытии сцены: " + fxmlPath, e);
        }
    }

    /**
     * Открывает новую сцену с передачей данных контроллеру.
     *
     * @param fxmlPath  путь к FXML-файлу.
     * @param title     заголовок окна.
     * @param dataConsumer     данные для передачи.
     * @param <T>       тип контроллера.
     */
    public static <T> void openScene(String fxmlPath, String title, DataConsumer<T> dataConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Получаем контроллер
            T controller = loader.getController();
            if (dataConsumer != null) {
                dataConsumer.accept(controller); // Передача данных через лямбду
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Делает окно модальным
            stage.showAndWait(); // Ожидает, пока окно не закроется
        } catch (IOException e) {
            logger.error("Ошибка при открытии сцены: " + fxmlPath, e);
        }
    }

    @FunctionalInterface
    public interface DataConsumer<T> {
        void accept(T t);
    }
}
