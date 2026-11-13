package com.laboratorio.bd_laboratorio_orm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LaboratorioApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaboratorioApplication.class.getResource("/com/laboratorio/bd_laboratorio_orm/equipo.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Control de Equipos de Laboratorio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
