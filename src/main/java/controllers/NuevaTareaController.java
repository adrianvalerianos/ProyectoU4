package controllers;

import database.DBManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Tarea;

import java.util.HashMap;
import java.util.Map;

public class NuevaTareaController {

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
    void agregar(ActionEvent event) {
        if (camposCompletos()) {
            guardarTarea();
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

    private boolean camposCompletos() {
        return !txfTitulo.getText().isEmpty()
                && !txtDescripcion.getText().isEmpty()
                && dtFecha.getValue() != null
                && seleccionColor.getValue() != null;
    }

    private void guardarTarea() {
        String colorReal = coloresMap.get(seleccionColor.getValue());
        tarea = new Tarea(txfTitulo.getText(), txtDescripcion.getText(), dtFecha.getValue().toString(), colorReal);
        DBManagement.guardarTarea(tarea);
        System.out.printf("%s %s %s %s%n",
                txfTitulo.getText(), txtDescripcion.getText(), dtFecha.getValue(), colorReal);
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
        coloresMap.put("Rojo", "TOMATO");
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
}
