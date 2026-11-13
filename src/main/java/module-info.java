module com.laboratorio.bd_laboratorio_orm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.laboratorio.bd_laboratorio_orm to javafx.fxml;
    exports com.laboratorio.bd_laboratorio_orm;
}