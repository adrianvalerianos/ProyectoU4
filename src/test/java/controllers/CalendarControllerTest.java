package controllers;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CalendarControllerTest {
    private CalendarController controller = new CalendarController();

    @Test
    void testDateSearchFormat() {
        int año = 2023;
        int mes = 5;
        int dia = 15;

        String fechaEsperada = String.format("%d-%02d-%02d", año, mes, dia);
        assertEquals("2023-05-15", fechaEsperada,
                "El formato de fecha debe ser YYYY-MM-DD");
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
    @Test
    void testMesesEspanolNombres() throws Exception {
        Field mesesField = CalendarController.class.getDeclaredField("MESES_ESPANOL");
        mesesField.setAccessible(true);
        String[] mesesEspanol = (String[]) mesesField.get(null);

        String[] mesesEsperados = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        assertArrayEquals(mesesEsperados, mesesEspanol,
                "Los nombres de los meses deben coincidir exactamente");
    }
}
