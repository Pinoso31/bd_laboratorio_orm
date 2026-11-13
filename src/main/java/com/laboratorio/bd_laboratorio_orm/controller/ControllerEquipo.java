package com.laboratorio.bd_laboratorio_orm.controller;

import com.laboratorio.bd_laboratorio_orm.dao.EquipoDAO;
import com.laboratorio.bd_laboratorio_orm.dao.TipoEquipoDAO;
import com.laboratorio.bd_laboratorio_orm.model.Equipo;
import com.laboratorio.bd_laboratorio_orm.model.TipoEquipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class ControllerEquipo {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtMarca;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox<TipoEquipo> cmbTipoEquipo;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableView<Equipo> tblEquipos;
    @FXML private TableColumn<Equipo, String> colCodigo;
    @FXML private TableColumn<Equipo, String> colNombre;
    @FXML private TableColumn<Equipo, String> colMarca;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, Integer> colCantidad;
    @FXML private TableColumn<Equipo, String> colTipoEquipo;
    @FXML private TextField txtBuscarNombre;
    @FXML private ComboBox<TipoEquipo> cmbFiltroTipo;
    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnBuscar;

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final TipoEquipoDAO tipoEquipoDAO = new TipoEquipoDAO();
    private final ObservableList<Equipo> listaEquipos = FXCollections.observableArrayList();
    private Equipo equipoSeleccionado = null;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarTiposEquipo();
        cargarEstados();
        cargarEquipos();
        configurarEventos();
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colTipoEquipo.setCellValueFactory(cellData -> {
            Equipo equipo = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getNombre() : ""
            );
        });
        tblEquipos.setItems(listaEquipos);
    }

    private void cargarTiposEquipo() {
        List<TipoEquipo> tipos = tipoEquipoDAO.obtenerParaComboBox();
        ObservableList<TipoEquipo> observableTipos = FXCollections.observableArrayList(tipos);
        cmbTipoEquipo.setItems(observableTipos);
        cmbFiltroTipo.setItems(FXCollections.observableArrayList(tipos));
        
        configurarComboBoxTipoEquipo(cmbTipoEquipo);
        configurarComboBoxTipoEquipo(cmbFiltroTipo);
        
        cmbFiltroTipo.getItems().add(0, null);
        cmbFiltroTipo.getSelectionModel().selectFirst();
    }

    private void configurarComboBoxTipoEquipo(ComboBox<TipoEquipo> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<TipoEquipo>() {
            @Override
            protected void updateItem(TipoEquipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });
        comboBox.setButtonCell(new ListCell<TipoEquipo>() {
            @Override
            protected void updateItem(TipoEquipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNombre());
            }
        });
    }

    private void cargarEstados() {
        cmbEstado.getItems().addAll("Disponible", "En mantenimiento", "Fuera de servicio");
    }

    private void cargarEquipos() {
        listaEquipos.clear();
        listaEquipos.addAll(equipoDAO.listarEquipos());
    }

    private void configurarEventos() {
        tblEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarEquipoEnFormulario(newSel);
            }
        });
    }

    private void cargarEquipoEnFormulario(Equipo equipo) {
        this.equipoSeleccionado = equipo;
        txtCodigo.setText(equipo.getCodigo());
        txtNombre.setText(equipo.getNombre());
        txtMarca.setText(equipo.getMarca() != null ? equipo.getMarca() : "");
        txtCantidad.setText(String.valueOf(equipo.getCantidad()));
        cmbTipoEquipo.getSelectionModel().select(equipo.getTipoEquipo());
        cmbEstado.getSelectionModel().select(equipo.getEstado());
        
        txtCodigo.setDisable(true);
        btnGuardar.setDisable(true);
        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }
}