package tasks.model;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

@Tag("tetset")
class TaskTest {
    private Task task;
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;

    @BeforeEach
    void setUp() {
        try {
            task=new Task("new task",Task.getDateFormat().parse("2023-02-12 10:10"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testNevalid1() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2023, 10, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 10, 15, 10, 30,0).getTime();
        Date testDate2 = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();

        Task task = new Task("test_task",startDate,endDate,0);
        task.setActive(true);

       // ACT
        Date result1 = task.nextTimeAfter(testDate1);
        Date result2 = task.nextTimeAfter(testDate2);

        //ASSERT
        assert result1 == null;
        assert result2 == null;
    }

    @Test
    void testNevalid2() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2023, 10, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 9, 15, 10, 30,0).getTime();


        Task task = new Task("test_task",startDate,endDate,0);
        task.setActive(false);

        // ACT
        Date result1 = task.nextTimeAfter(testDate1);

        //ASSERT
        assert result1 == null;
    }


    @Test
    void testValid1() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2024, 10, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 7, 15, 10, 30,0).getTime();

        Task task = new Task("test_task",startDate,endDate,60);
        task.setActive(true);

        // ACT
        Date result1 = task.nextTimeAfter(testDate1);

        //ASSERT
        assert testDate1.before(startDate);
        assert result1 == startDate;
    }

    @Test
    void testValid2() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2024, 10, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 8, 15, 10, 30,45).getTime();

        Task task = new Task("test_task",startDate,endDate,60);
        task.setActive(true);

        // ACT
        Date result1 = task.nextTimeAfter(testDate1);

        //ASSERT
        assert result1 == startDate;
    }

    @Test
    void testValid3() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2024, 8, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 9, 15, 10, 30,0).getTime();

        Task task = new Task("test_task",startDate,endDate,180);
        task.setActive(true);

        // ACT
        Date result1 = task.nextTimeAfter(testDate1);

        //ASSERT
        assert result1.equals(new GregorianCalendar(2023, 9, 15, 10, 33,0).getTime());
    }

    @Test
    void testValid4() {

        // ARRANGE
        Date startDate = new GregorianCalendar(2023, 8, 15, 10, 30,0).getTime();
        Date endDate = new GregorianCalendar(2024, 10, 15, 10, 30,0).getTime();

        Date testDate1 = new GregorianCalendar(2023, 7, 15, 10, 30,0).getTime();

        Task task = new Task("test_task",startDate,endDate,0);
        task.setActive(true);

        // ACT
        Date result1 = task.nextTimeAfter(testDate1);

        //ASSERT
        assert result1.equals(startDate);
    }


    private static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}