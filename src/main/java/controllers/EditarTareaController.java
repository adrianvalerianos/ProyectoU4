package controllers;

import database.DBManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

public class EditarTareaController {

    private Tarea tarea;
    private final Map<String, String> coloresMap = new HashMap<>();

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    @FXML
    private DatePicker dtFecha;

    @FXML
    private ChoiceBox<String> seleccionColor;

    @FXML
    private TextField txfTitulo;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    void editar(ActionEvent event) {
        if (camposCompletos()) {
            editarTarea();
            mostrarAlerta(Alert.AlertType.INFORMATION, "La tarea se guardó con éxito");
            cerrarVentana();
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Faltan campos por llenar");
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    public void initialize() {
        inicializarColores();
        seleccionColor.getItems().addAll(coloresMap.keySet());
    }

    private void cargarDatosTarea() {
        if (tarea != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(tarea.getFecha(), formatter);

            txfTitulo.setText(tarea.getTitulo());
            txtDescripcion.setText(tarea.getDescripcion());
            dtFecha.setValue(parsedDate);
            String claveColor = obtenerClavePorValor(tarea.getColor());
            seleccionColor.setValue(claveColor);
        }
    }

    private String obtenerClavePorValor(String valor) {
        return coloresMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(valor))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean camposCompletos() {
        return !txfTitulo.getText().isEmpty()
                && !txtDescripcion.getText().isEmpty()
                && dtFecha.getValue() != null
                && seleccionColor.getValue() != null;
    }

    private void editarTarea() {
        String colorReal = coloresMap.get(seleccionColor.getValue());
        tarea.setTitulo(txfTitulo.getText());
        tarea.setDescripcion(txtDescripcion.getText());
        tarea.setFecha(dtFecha.getValue().toString());
        tarea.setColor(colorReal);
        DBManagement.editarTarea(tarea);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Calendario dice:");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alert.showAndWait();
    }

    private void inicializarColores() {
        coloresMap.put("Azul", "ROYALBLUE");
        coloresMap.put("Rojo", "CRIMSON");
        coloresMap.put("Amarillo", "GOLD");
        coloresMap.put("Verde", "LIMEGREEN");
        coloresMap.put("Morado", "BLUEVIOLET");
        coloresMap.put("Turquesa", "DARKTURQUOISE");
        coloresMap.put("Café", "SIENNA");
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnAgregar.getScene().getWindow();
        stage.close();
    }

    public void setTarea(Tarea tareaActual) {
        this.tarea = tareaActual;
        cargarDatosTarea();
    }
}
