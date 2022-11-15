package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.Map;


import es.uned.common.Trino;
import es.uned.common.ServicioDatosInterface;


// Servicio Datos: Este servicio hará las funciones de una “base de datos” que relacione Usuarios-Seguidores-Trinos.
// Es decir, mantendrá la lista de usuarios registrados y/o conectados al sistema, junto con sus seguidores y trinos; 
// y los relacionará permitiendo operaciones típicas de consulta, añadir y borrado.
// Además, también debe posibilitar la operación de bloqueo de una cuenta (banear). Los dos servicios anteriores 
// (Servicio Autenticación y Servicio Gestor) harán uso de este servicio para realizar las operaciones sobre el estado 
// de los usuarios del sistema y sus seguidores.
// El equipo docente recomienda para la implementación del servicio las clases List y HashMap de Java.

//Los metodos remotos para autenticarse y registrarse los debe tener la clase impl de la base de datos

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	protected ServicioDatosImpl() throws RemoteException {
		super();
	}

	private HashMap<String,String> usuarios = new HashMap<>();	
	private HashMap<String,String> usuariosConectados = new HashMap<>();	
	// lista de trinos por usuario, key es el 
	private HashMap<String,List<String>> trinos = new HashMap<>();
	// mapa con la sesion del usuario pendiente de recibir mensajes y una lista de los mensajes pendientes
	private Map<String, List<Trino>> buffer = new HashMap<>();
	
	
	
//	private Map<Integer, String> sesion_nombre = new HashMap<Integer, String>();
//	private Map<String, Integer> nombre_sesion = new HashMap<String, Integer>();
//	//lista con las sesiones de los usuario conectados
//	private Map<Integer, List<Integer>> contactos = new HashMap<Integer, List<Integer>>(); 
	

	public void menu() throws RemoteException{
		Scanner key = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n ----------------------------------------------------------");
        System.out.println(" |                 * Menu Base de Datos *                 |");                               
        System.out.println(" ----------------------------------------------------------\n");
        System.out.println("1: Informacion de la base de datos.");
        System.out.println("2: Listar Trinos (solo nick del propietario y el timestamp).");
        System.out.println("3: Salir.");
                
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = key.nextLine().trim();		
            switch(option){
                case "1":
                	mostrarUsuarios();
                    break;
                case "2":
                	System.out.print("wow 2");
                    break;
                case "3":
                    key.close();
                    System.out.print("\nCerrando base de datos...\n");
                    System.exit(1);
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="3");
		
	}
	
	@Override
	public void agregarUsuario(String name, String password) throws RemoteException {
		
		usuarios.put(name, password);
		
	}
	
	@Override
	public void mostrarUsuarios() throws RemoteException {
		
		 usuarios.forEach((key, value) ->
		 {
			 System.out.println("key: " + key + " value: " + value);
	            
	     });		
	}
	

	@Override
	public void borrarUsuario() throws RemoteException {
		
		
		
	}

	@Override
	public void banearUsuario() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agregarTrino(Trino trino) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarTrino(Trino trino) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void limpiarBuffer(String name) throws RemoteException {
		
		// comprueba que la sesion es valida
		if (!usuarios.containsKey(name)) {
			throw new RuntimeException("no exite el usuario");
		}
		// si lo es, borra todos los elementos de la lista
		buffer.get(name).clear();
		
	}

	@Override
	public String decirHola(String nombre) throws RemoteException {
		return "Soy el metodo de ServicioDatosImpl " + nombre;
	}

}
