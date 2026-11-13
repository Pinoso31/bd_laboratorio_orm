package com.laboratorio.bd_laboratorio_orm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LaboratorioApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaboratorioApplication.class.getResource("/com/example/proyectohibernate/libro-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ventana registro libro");
        stage.setScene(scene);
        stage.show();
    }
}
