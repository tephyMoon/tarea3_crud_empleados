/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import vista.frm_personas;
/**
 *
 * @author Maritza Vargas
 */
public class Empleado extends Persona{
    private String codigo;
    private int id;
    private JComboBox<String> cmb_puestos; 
    Conexion cn;
    
    public Empleado(){}
      
    public Empleado(int id,String codigo, String nombres, String apellidos, String direccion, String telefono, String fecha_nacimiento,JComboBox<String> cmb_puestos) {
        super(nombres, apellidos, direccion, telefono, fecha_nacimiento);
        this.codigo = codigo;
        this.id = id;
        this.cmb_puestos = cmb_puestos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        public JComboBox<String> getCmb_puestos() {
        return cmb_puestos;
    }

    public void setCmb_puestos(JComboBox<String> cmb_puestos) {
        this.cmb_puestos = cmb_puestos;
    }

    
    
    public DefaultTableModel leer(){
        DefaultTableModel tabla = new DefaultTableModel();
        try{
            cn = new Conexion();
            cn.abrir_conexion();
            String query;
            query = "SELECT e.id_empleados, e.codigo, e.nombres, e.apellidos, e.direccion, e.telefono, e.fecha_nacimiento, p.puesto FROM empleados e LEFT JOIN puestos p ON e.id_puesto = p.id_puestos;";
            ResultSet consulta =cn.conexionDB.createStatement().executeQuery(query);
            
            String encabezado[]={"id","Codigo","Nombres","Apellidos","Direccion","Telefono","Nacimiento","Puesto"};
            tabla.setColumnIdentifiers(encabezado);
            
            String datos[]= new String[8];
            while(consulta.next()){
                datos [0]= consulta.getString("id_empleados");
                datos [1]= consulta.getString("codigo");
                datos [2]= consulta.getString("nombres");
                datos [3]= consulta.getString("apellidos");
                datos [4]= consulta.getString("direccion");
                datos [5]= consulta.getString("telefono");
                datos [6]= consulta.getString("fecha_nacimiento");
                datos [7]= consulta.getString("puesto");
                tabla.addRow(datos);
            }
            
            cn.cerrar_conexion();
            
        }catch(SQLException ex){
            cn.cerrar_conexion();
            System.out.println("Error... "+ ex.getMessage() );
    }
        return tabla;
    }

    
       
    public int obtenerIdPuesto(String nombrePuesto) {
     int idPuesto = 0;
    Conexion cn = new Conexion();
    try {
        cn.abrir_conexion();
        
        // Consulta SQL para obtener el ID del puesto basado en su nombre
        String query = "SELECT id_puestos FROM puestos WHERE puesto = ?;";
        PreparedStatement parametro = cn.conexionDB.prepareStatement(query);
        parametro.setString(1, nombrePuesto);  // Nombre del puesto seleccionado en el JComboBox
        
        ResultSet resultado = parametro.executeQuery();
        
        // Si hay resultados, obtener el ID del puesto
        if (resultado.next()) {
            idPuesto = resultado.getInt("id_puestos");
        } else {
            System.out.println("No se encontró el puesto: " + nombrePuesto);
        }
        
        cn.cerrar_conexion();
    } catch (SQLException ex) {
        System.out.println("Error al obtener el ID del puesto: " + ex.getMessage());
    }
    
    return idPuesto;
}
    
    
     @Override
    public void agregar() {
    try {
        PreparedStatement parametro;
        String query = "INSERT INTO `empresa`.`empleados`(codigo, nombres, apellidos, direccion, telefono, fecha_nacimiento, id_puesto) VALUES (?, ?, ?, ?, ?, ?, ?);";
        
        cn = new Conexion(); 
        cn.abrir_conexion();
        
        // Preparar la consulta
        parametro = (PreparedStatement) cn.conexionDB.prepareStatement(query);
        parametro.setString(1, getCodigo());  
        parametro.setString(2, getNombres());
        parametro.setString(3, getApellidos());
        parametro.setString(4, getDireccion());
        parametro.setString(5, getTelefono());
        parametro.setString(6, getFecha_nacimiento());
        
        // Obtener el puesto seleccionado desde el JComboBox
        String puestoSeleccionado = (String) cmb_puestos.getSelectedItem();
        
        // Obtener el ID del puesto desde la base de datos
        int id_puestos = obtenerIdPuesto(puestoSeleccionado);  
        parametro.setInt(7, id_puestos); 
        
        // Ejecutar la consulta
        int executar = parametro.executeUpdate();
        cn.cerrar_conexion();
        JOptionPane.showMessageDialog(null, Integer.toString(executar) + " Registro ingresado", "Agregar", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception ex) {
        System.out.println("Error..." + ex.getMessage());
    }
}

    @Override
public void actualizar() {
    try {
        PreparedStatement parametro;
        String query = "UPDATE `empresa`.`empleados` SET `codigo` = ?, `nombres` = ?, `apellidos` = ?, `direccion` = ?, `telefono` = ?, `fecha_nacimiento` = ?, `id_puesto` = ? WHERE (`id_empleados` = ?);";
        
        cn = new Conexion();
        cn.abrir_conexion();
        
        // Preparar la consulta
        parametro = (PreparedStatement) cn.conexionDB.prepareStatement(query);
        parametro.setString(1, getCodigo());
        parametro.setString(2, getNombres());
        parametro.setString(3, getApellidos());
        parametro.setString(4, getDireccion());
        parametro.setString(5, getTelefono());
        parametro.setString(6, getFecha_nacimiento());
                
        // Obtener el puesto seleccionado desde el JComboBox
        String puestoSeleccionado = (String) cmb_puestos.getSelectedItem();
        
        // Obtener el ID del puesto desde la base de datos
        int id_puestos = obtenerIdPuesto(puestoSeleccionado);
        parametro.setInt(7, id_puestos);
        
        // Usar el ID del empleado para la cláusula WHERE
        parametro.setInt(8, getId()); // Suponiendo que getId() obtiene el ID del empleado a actualizar
        
        // Ejecutar la consulta
        int executar = parametro.executeUpdate();
        cn.cerrar_conexion();
        
        // Mostrar un mensaje de confirmación
        JOptionPane.showMessageDialog(null, Integer.toString(executar) + " Registro actualizado", "Actualizar", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception ex) {
        System.out.println("Error..." + ex.getMessage());
    }
}
          
      @Override
      public void eliminar(){
          
       try{
            PreparedStatement parametro;
            String query = "delete from `empresa`.`empleados` where id_empleados = ?";
        cn = new Conexion();
        cn.abrir_conexion();
        parametro = (PreparedStatement) cn.conexionDB.prepareStatement(query);
        parametro.setInt(1, getId());
        
        int executar = parametro.executeUpdate();
        cn.cerrar_conexion();
        JOptionPane.showMessageDialog(null, Integer.toString(executar) + " Registro Eliminado", "Agregar",JOptionPane.INFORMATION_MESSAGE );
        
        }catch(Exception ex){
            System.out.println("Error..."+ex.getMessage());
        }  
      
      
      }


    
    
    
}
