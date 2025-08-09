package controllers;

import application.App;
import database.DBManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Tarea;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {

    private static final String[] MESES_ESPANOL = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Button btnAgregarTarea;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        calendar.getChildren().clear();

        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(MESES_ESPANOL[dateFocus.getMonthValue() - 1]);

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int semanas = 0; semanas < 6; semanas++) {
            for (int dias = 0; dias < 7; dias++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (dias + 1) + (7 * semanas);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        date.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                        stackPane.getChildren().add(date);

                        String fechaBusqueda = String.format("%d-%02d-%02d",
                                dateFocus.getYear(),
                                dateFocus.getMonthValue(),
                                currentDate);

                        System.out.println(fechaBusqueda);
                        ArrayList<Tarea> tareasEncontradas = DBManagement.buscarTareas(fechaBusqueda);
                        if (!tareasEncontradas.isEmpty()) {
                            System.out.println("Pasó por aquí");
                            crearVistaTarea(tareasEncontradas, rectangleHeight, rectangleWidth, stackPane);
                        }


                        if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                            rectangle.setStroke(Color.BLUE);
                            rectangle.setFill(Color.LIGHTBLUE);
                        }
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void crearVistaTarea(List<Tarea> vistasTareas, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox vistaBox = new VBox();
        vistaBox.setSpacing(2);
        vistaBox.setPrefWidth(rectangleWidth * 0.75);

        for (int k = 0; k < vistasTareas.size(); k++) {
            Tarea tareaActual = vistasTareas.get(k);
            StackPane tareaElemento = new StackPane();

            Rectangle tareaRect = new Rectangle();
            tareaRect.setWidth(rectangleWidth * 0.75);
            tareaRect.setHeight(15);

            try {
                Color colorTarea = Color.web(tareaActual.getColor());
                tareaRect.setFill(colorTarea);
                tareaRect.setStroke(colorTarea.darker());
            } catch (Exception e) {
                tareaRect.setFill(Color.LIGHTBLUE);
                tareaRect.setStroke(Color.BLUE);
            }
            tareaRect.setStrokeWidth(0.5);

            Text textoTarea = new Text(tareaActual.getTitulo());
            textoTarea.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            textoTarea.setFill(Color.WHITE);
            if (textoTarea.getBoundsInLocal().getWidth() > rectangleWidth * 0.7) {
                String tituloCorto = tareaActual.getTitulo();
                if (tituloCorto.length() > 12) {
                    tituloCorto = tituloCorto.substring(0, 9) + "...";
                }
                textoTarea.setText(tituloCorto);
            }

            tareaElemento.getChildren().addAll(tareaRect, textoTarea);
            vistaBox.getChildren().add(tareaElemento);

            tareaElemento.setOnMouseClicked(mouseEvent -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("vistaTarea.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    VistaTareaController vistaTareaController = fxmlLoader.getController();
                    vistaTareaController.setTarea(tareaActual);

                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Detalles de la tarea");
                    stage.centerOnScreen();
                    stage.setResizable(false);
                    stage.setAlwaysOnTop(true);

                    stage.showAndWait();
                    drawCalendar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            tareaElemento.setOnMouseEntered(mouseEvent -> {
                tareaRect.setStroke(Color.BLACK);
                tareaRect.setStrokeWidth(1.5);
            });

            tareaElemento.setOnMouseExited(mouseEvent -> {
                try {
                    Color colorTarea = Color.web(tareaActual.getColor());
                    tareaRect.setStroke(colorTarea.darker());
                } catch (Exception e) {
                    tareaRect.setStroke(Color.BLUE);
                }
                tareaRect.setStrokeWidth(0.5);
            });
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vistaBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefWidth(rectangleWidth * 0.8);
        scrollPane.setMaxWidth(rectangleWidth * 0.8);
        scrollPane.setPrefHeight(rectangleHeight * 0.65);
        scrollPane.setMaxHeight(rectangleHeight * 0.65);
        scrollPane.setTranslateY((rectangleHeight / 2) * 0.20);

        stackPane.getChildren().add(scrollPane);
    }

    @FXML
    void agregarTarea(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("nuevaTarea.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Agregar nueva tarea");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            drawCalendar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}