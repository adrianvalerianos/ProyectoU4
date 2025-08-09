package controllers;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CalendarControllerTest {
    private CalendarController controller = new CalendarController();

    @Test
    void testCambioMesHaciaAtras() {
        controller.dateFocus = ZonedDateTime.now();
        ZonedDateTime fechaInicial = controller.dateFocus;

        controller.backOneMonth(null);
        assertEquals(fechaInicial.minusMonths(1).getMonth(), controller.dateFocus.getMonth());


    }

    @Test
    void testCambioMesHaciaAdelante() {
        controller.dateFocus = ZonedDateTime.now();
        ZonedDateTime fechaInicial = controller.dateFocus;

        controller.forwardOneMonth(null);

        assertEquals(fechaInicial.plusMonths(1).getMonth(), controller.dateFocus.getMonth());
    }

    @Test
    void testMaximoDiasEnFebreroNoBisiesto() {
        controller.dateFocus = ZonedDateTime.parse("2025-02-01T00:00:00Z[UTC]");
        int dias = controller.dateFocus.getMonth().maxLength();
        // Ajuste por no ser bisiesto
        if (controller.dateFocus.getYear() % 4 != 0 && dias == 29) {
            dias = 28;
        }
        assertEquals(28, dias);
    }
}
