package org.example.demo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.example.demo.page_controllers.ConcertsController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ConcertsControllerTest {

    private ConcertsController controller;
    private ObservableList<TourSchedule> tourList;

    @BeforeAll
    static void initPlatform() {
        // Инициализация JavaFX Toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new ConcertsController();
        tourList = FXCollections.observableArrayList();
        controller.tourScheduleTable = new TableView<>(tourList);
    }

    @Test
    void testAddTour() {
        TourSchedule newTour = new TourSchedule("2024-12-01", "Band A", "New York", "Venue 1", "Organizer 1", "12345");
        controller.addTour(newTour);

        assertEquals(1, tourList.size());
        assertEquals("2024-12-01", tourList.get(0).getDate());
    }

    @Test
    void testGenerateXMLFromTable() {
        TourSchedule newTour = new TourSchedule("2024-12-01", "Band A", "New York", "Venue 1", "Organizer 1", "12345");
        controller.addTour(newTour);

        File tempFile = new File("temp.xml");
        try {
            controller.generateXMLFromTable(tempFile);
            assertTrue(tempFile.exists());
        } catch (Exception e) {
            fail("Exception during XML generation: " + e.getMessage());
        }
    }

    @Test
    void testGeneratePDF() {
        File tempXmlFile = new File("temp.xml");
        File pdfFile = new File("tour_schedule.pdf");

        try {
            controller.generatePDF(tempXmlFile, pdfFile);
            assertTrue(pdfFile.exists());
        } catch (Exception e) {
            fail("Exception during PDF generation: " + e.getMessage());
        }
    }

    @Test
    void testGenerateHTML() {
        File tempXmlFile = new File("temp.xml");
        File htmlFile = new File("tour_schedule.html");

        try {
            controller.generateHTML(tempXmlFile, htmlFile);
            assertTrue(htmlFile.exists());
        } catch (Exception e) {
            fail("Exception during HTML generation: " + e.getMessage());
        }
    }
}
