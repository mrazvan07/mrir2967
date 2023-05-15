package tasks.controller;

import de.saxsys.javafx.test.JfxRunner;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TasksService;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JfxRunner.class)
class NewEditControllerECPTest {

    private static Stage editNewStage;
    private ObservableList<Task> tasksList;
    private TasksService tasksService;
    private DateService dateService;
    private DatePicker datePickerStart;
    private DatePicker datePickerEnd ;

    private static TableView mainTable;
    private TextField txtFieldTimeStart;
    private TextField txtFieldTimeEnd;
    private TextField fieldInterval;
    private TextField fieldTitle;
    private CheckBox checkBoxRepeated;
    private CheckBox checkBoxActive;


    @BeforeAll
    static void initJfxRuntime() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        ArrayTaskList savedTasksList = new ArrayTaskList();
        tasksService = new TasksService(savedTasksList);
        tasksList = tasksService.getObservableList();
        dateService = new DateService(tasksService);

        datePickerStart = new DatePicker();
        datePickerEnd = new DatePicker();

        txtFieldTimeStart = new TextField();
        txtFieldTimeEnd = new TextField();
        fieldInterval = new TextField();
        fieldTitle = new TextField();

        checkBoxRepeated = new CheckBox();
        checkBoxActive = new CheckBox();


        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.of(2025,12,8));

        txtFieldTimeStart.setText("8:00");
        txtFieldTimeEnd.setText("19:00");

        checkBoxRepeated.setSelected(true);
        checkBoxActive.setSelected(true);
    }

    @AfterEach
    void tearDown() {

    }

    @ParameterizedTest
    @ValueSource(strings = { "Tema VVSS", "Mers la cursuri" })
    @Tag("valid")
    @DisplayName("testValid1")
    void testValid1(String title) {

        //ARRANGE
        NewEditController newEditController = new NewEditController(tasksList,tasksService, dateService);
        assert tasksList.size() == 0;
        fieldInterval.setText("00:30");
        fieldTitle.setText(title);
        newEditController.setFXMLFieldsForTesting(fieldTitle,fieldInterval,datePickerStart,datePickerEnd,txtFieldTimeStart,txtFieldTimeEnd,checkBoxActive,checkBoxRepeated);

        //ACT
        newEditController.saveChanges();

        //ASSERT
        assert tasksList.size() == 1;

    }

    @ParameterizedTest
    @ValueSource(strings = { "98:15", "00:01" })
    @Tag("valid")
    @DisplayName("testValid2")
    void testValid2(String interval) {

        //ARRANGE
        NewEditController newEditController = new NewEditController(tasksList,tasksService, dateService);
        assert tasksList.size() == 0;
        fieldInterval.setText(interval);
        fieldTitle.setText("titlu");
        newEditController.setFXMLFieldsForTesting(fieldTitle,fieldInterval,datePickerStart,datePickerEnd,txtFieldTimeStart,txtFieldTimeEnd,checkBoxActive,checkBoxRepeated);

        //ACT
        newEditController.saveChanges();

        //ASSERT
        assert tasksList.size() == 1;



    }


    @RepeatedTest(value = 5, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @Tag("nevalid")
    @DisplayName("testNevalid1")
    void testNeValid1() throws IOException {

        //ARRANGE
        NewEditController newEditController = new NewEditController(tasksList,tasksService, dateService);
        assert tasksList.size() == 0;
        fieldInterval.setText("fasfasf");
        fieldTitle.setText("titlu");
        newEditController.setFXMLFieldsForTesting(fieldTitle,fieldInterval,datePickerStart,datePickerEnd,txtFieldTimeStart,txtFieldTimeEnd,checkBoxActive,checkBoxRepeated);

        //ACT
        Exception exception = assertThrows(IllegalArgumentException.class, () -> newEditController.saveChanges());
        String expectedMessage = "Interval should respect hh:MM format";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        //ASSERT
        assert tasksList.size() == 0;
        assertTrue(actualMessage.contains(expectedMessage));


    }

    @RepeatedTest(value = 5, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @Tag("nevalid")
    @DisplayName("testNevalid2")
    void testNeValid2() throws IOException {

        //ARRANGE
        NewEditController newEditController = new NewEditController(tasksList,tasksService, dateService);
        assert tasksList.size() == 0;
        fieldInterval.setText("10:00");
        fieldTitle.setText("     ");
        newEditController.setFXMLFieldsForTesting(fieldTitle,fieldInterval,datePickerStart,datePickerEnd,txtFieldTimeStart,txtFieldTimeEnd,checkBoxActive,checkBoxRepeated);

        //ACT
        Exception exception = assertThrows(IllegalArgumentException.class, () -> newEditController.saveChanges());
        String expectedMessage = "Title should not be empty";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        //ASSERT
        assert tasksList.size() == 0;
        assertTrue(actualMessage.contains(expectedMessage));


    }

}