/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Maritza Vargas
 */
public class Conexion {
    //puerto base de datos, nombre de la base de datos, url conexion, usuraio, contraseña, driver conector jdbc
    
    private final String puerto ="3306";
    private final String db ="empresa";
    private final String urlConexion = String.format("jdbc:mysql://127.0.0.1:%s/%s?serverTimezone=UTC",puerto,db);
    private final String usuario = "root";
    private final String contrasenia= "root1234";
    private final String jdbc="com.mysql.cj.jdbc.Driver";
    public Connection conexionDB;

    public void abrir_conexion(){
        try{
            Class.forName(jdbc);
            conexionDB = DriverManager.getConnection(urlConexion, usuario, contrasenia);
        //JOptionPane.showMessageDialog(null, "Conexion Exitosa...");
        }catch(ClassNotFoundException | SQLException ex)
            {System.out.println("algo salio mal :("+ex.getMessage());}
    }
    
    public void cerrar_conexion(){
        try{
           conexionDB.close();
        }catch(SQLException ex)
            {System.out.println("algo salio mal :("+ex.getMessage());}
    }   
    
    
    public ArrayList<String> obtenerPuestos() {
        ArrayList<String> puestos = new ArrayList<>();
        try {
            abrir_conexion(); // Abre la conexión

            String query = "SELECT puesto FROM puestos;";
            Statement stmt = conexionDB.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                puestos.add(rs.getString("puesto"));
            }

            rs.close();
            stmt.close();
            cerrar_conexion(); // Cierra la conexión

        } catch (SQLException ex) {
            System.out.println("Error al obtener los puestos: " + ex.getMessage());
        }
        return puestos;
    }
}
