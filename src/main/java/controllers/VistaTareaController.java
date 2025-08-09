package controllers;

import application.App;
import database.DBManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Tarea;

public class VistaTareaController {


    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblTitulo;

    @FXML
    private Text txtDescripcion;

    private Tarea tareaActual;

    public void setTarea(Tarea tarea) {
        this.tareaActual = tarea;
        lblTitulo.setText(tarea.getTitulo());
        lblFecha.setText(tarea.getFecha());
        txtDescripcion.setText(tarea.getDescripcion());
    }

    @FXML
    void aceptar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    void editar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("editarTarea.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            EditarTareaController EditarTareaController = fxmlLoader.getController();
            EditarTareaController.setTarea(tareaActual);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Agregar nueva tarea");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        DBManagement.eliminarTarea(tareaActual.getId());
        mostrarAlerta(Alert.AlertType.INFORMATION, "La tarea se eliminó con éxito");
        cerrarVentana();
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

    private void cerrarVentana() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
}
