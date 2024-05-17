package com.example.MartinP;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;
public class Trabajador implements Initializable {
    public TextField nameText;
    public ComboBox<String> choice;
    public TextField salaryText;
    public Button insertButton;
    @FXML
    public ListView<String> consultText;
    @FXML
    public Button refreshBTN;
    @FXML
    public Button deleteBTN;
    @FXML
    public Button editBTN;
    @FXML
    public Label nameLabel;
    @FXML
    public Label salaryLabel;
    @FXML
    public Label posLabel;

    @FXML
    public Label dateLabel;
    @FXML
    public Label idLabel;
    public Button loadDataBTN;




    public void trabajador() {
        if (nameText.getText().isEmpty() ||
                salaryText.getText().isEmpty() ||
                choice.getItems().isEmpty()) {
            error();
        } else {
            if (insertar()) {
                Informacion();
            }
        }
    }

    public void error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText("Si no rellena los campos no se puede añadir a la lista de los trabajadores");
        alert.setTitle("IMPOSIBLE AÑADIR TRABAJADOR");
        alert.show();
    }
    public void Informacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Mensaje");
        alert.setContentText("Empleado " + nameText.getText() + " introducido en la base de datos");
        alert.setTitle("HECHO");
        alert.show();
    }

    public Connection conectar() {
        String direccion = "jdbc:mysql://localhost:3306/gestorEmpleados";
        String usuario = "root";
        String contrasenya = "root";
        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(direccion, usuario, contrasenya);
            if (conexion != null) {
                return conexion;
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder");
        }
        return null;
    }

    public void parsearLinea() {
        File fichero = new File("GestorEmpleadosMartinPenalva-master/src/main/resources/com/example/MartinP/trabajadores.txt");
        try {
            Connection miConexion = null;
            miConexion = conectar();
            PreparedStatement statmen = miConexion.prepareStatement("insert into Empleados (nombre, puesto, salario, fecha) values (?,?,?,now())");
            Scanner scanner = new Scanner(fichero);
            while (scanner.hasNext())
            {
                String[] trabajador;
                trabajador = scanner.nextLine().split(";");
                String Nombre =trabajador[0];
                String Puesto =trabajador[1];
                int Salario = Integer.parseInt(trabajador[2]);
                statmen.setString(1, Nombre);
                statmen.setString(2, Puesto);
                statmen.setInt(3, Salario);
                statmen.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra el archivo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean insertar() {
        Connection miConexion = null;
        try {
            miConexion = conectar();
            PreparedStatement statment = miConexion.prepareStatement("insert into Empleados (nombre, puesto, salario, fecha) values (?,?,?,now())");
            statment.setString(1, nameText.getText());
            statment.setString(2, choice.getValue());
            try {
                statment.setInt(3, Integer.parseInt(salaryText.getText()));
            }
            catch (NumberFormatException e)
            {
                System.out.println("Formato erroneo");
                return false;
            }
            statment.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("No funciona la conexion");
            return false;
        } finally {
            if (miConexion != null) {
                try {
                    miConexion.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar");
                }
            }
        }
    }
    public void refresh() {
        try
        {
            Connection connection = conectar();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select nombre from Empleados");
            consultText.getItems().clear();
            while (resultSet.next())
            {
                consultText.getItems().add(resultSet.getString("NOMBRE"));
            }
        } catch (SQLException e) {
            System.out.println("Error al refrescar");
        }
    }

    public void eliminar() {
        eliminarDatos(consultText.getSelectionModel().getSelectedItem());
    }
    public void Losdatos(String nombre)
    {
        try {
            Connection connection = conectar();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Empleados where nombre = ?");
            preparedStatement.setString(1, nombre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                idLabel.setText(resultSet.getString("ID"));nameLabel.setText(resultSet.getString("NOMBRE"));
                posLabel.setText(resultSet.getString("PUESTO"));dateLabel.setText(resultSet.getString("FECHA"));
                salaryLabel.setText(resultSet.getString("SALARIO"));
            }
        } catch (SQLException e) {
            System.out.println("Error al Seleccionar");
        }
    }

    public void eliminarDatos(String nombre) {
        try {
            Connection connection = conectar();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from empleados where nombre = ?");
            preparedStatement.setString(1, nombre);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al Borrar");
        }
    }
}
