/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package E14_Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DAM2Alu7
 */
public class LogicaNegocio {
    
    static List<Cliente> listaClientes = new ArrayList();
    
    public static void cargaPrueba(){
        //String[]columnas = {"Nombre", "Apellidos", "Provincia", "Email", "Edad", "Alta"};
        for (int i = 0; i < 30; i++) {
            Cliente cliente = new Cliente("nombre" + i, "apellidos"+i, "provincia"+i, 20+i, "mail"+i, new Date());
            listaClientes.add(cliente);
        }
        
    }
    
    public static void addCliente(Cliente cliente){
        listaClientes.add(cliente);
    }
    
    public static void removeCliente (Cliente cliente) {
        listaClientes.remove(cliente);
    }
    
     public static void removeClienteID (int id) {
         
     }
     
     public static Cliente getCliente(int id){
        for (Cliente cliente : listaClientes) {
            if(cliente.getId()==id){
            return cliente;
            }
        }
        return null;
    }
    
    public static void editCliente(int id, Cliente cliente){
        
    }
    
    public static List<Cliente> getClientes(){
        return listaClientes;
    }
    
}
