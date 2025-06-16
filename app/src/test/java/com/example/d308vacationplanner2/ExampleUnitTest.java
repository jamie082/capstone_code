package com.example.d308vacationplanner2;

import org.junit.Test;

import static org.junit.Assert.*;
import java.time.LocalDate;


import com.example.d308vacationplanner2.UI.VacationDetails;
import com.example.d308vacationplanner2.entities.Vacation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testVacationDetailsConstructorAndGetters() {
        VacationDetails vacation = new VacationDetails("Beach Trip", "2025-06-20", "2025-06-25");

        assertEquals("Beach Trip", vacation.getName());
        assertEquals("2025-06-20", vacation.getStartDate());
        assertEquals("2025-06-25", vacation.getEndDate());
    }
}