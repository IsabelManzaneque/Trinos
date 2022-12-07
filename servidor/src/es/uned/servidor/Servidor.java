package es.uned.servidor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.User;


/**
 * La clase Servidor se encarga de controlar el proceso de autenticación de
 * los usuarios del sistema y gestión de sus trinos haciendo uso del servicio 
 * de autenticacion y el servicio gestor 
 */

public class Servidor {

	
	private static int puertoSalida = 8888;	
	private static int puertoEntrada = 5555;
	private static String URLGestor = "rmi://localhost:"+ puertoSalida + "/Gestor";
	private static String URLAutenticador = "rmi://localhost:"+ puertoSalida + "/Autenticador";
	private static String URLDatos = "rmi://localhost:" + puertoEntrada + "/Datos";	
	private static ServicioDatosInterface datos;
	private static ServicioGestorImpl gestor;
	private static ServicioAutenticacionImpl autenticador;
	
	
	/**
	 * Main del Servidor que levanta el servicio y establece la 
	 * conexión con la base de datos y los servicios gestor y autenticador
	 */
	public static void main(String[] args) throws Exception {		
		
		try {			
						
		    // Busqueda del objeto de la base de datos
			datos = (ServicioDatosInterface)Naming.lookup(URLDatos);				 
			// objetos de las clases impl que implementan las interfaces remotas
			gestor = new ServicioGestorImpl();
			autenticador = new ServicioAutenticacionImpl();
			
			arrancarRegistro(puertoSalida);	
			
			//Exporta objetos remotos
			Naming.rebind(URLGestor, gestor);
			Naming.rebind(URLAutenticador, autenticador);
						
			menu();
			
		}catch(Exception e) {
			System.out.println("Excepción en Servidor.main: " + e);
		}

	}
	
	/**
	 * Menu principal del servidor que llama a cada funcion que implementa el servidor
	 */	
	public static void menu() throws RemoteException{
		Scanner key = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Servidor *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("1: Información del Servidor.");
        System.out.println("2: Listar Usuarios Registrados.");
        System.out.println("3: Listar Usuarios Logueados.");
        System.out.println("4: Bloquear (banear) usuario. ");
        System.out.println("5: Desbloquear usuario");
        System.out.println("6: Salir.");
                
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = key.nextLine().trim();		
            switch(option){
                case "1":
                   	System.out.println("\tServicios activos: \n\t- " + URLGestor + "\n\t- " + URLAutenticador);
                    break;
                case "2":
                	mostrarUsuarios(datos.getUsuariosRegistrados(), "registrados");
                    break;
                case "3":
                	mostrarUsuarios(datos.getUsuariosConectados(), "conectados");
                    break;
                case "4":
                	gestor.bloquearUsuario();          
                    break;
                case "5":
                	gestor.desbloquearUsuario();                	
                    break;
                case "6":
                    key.close();
                    System.out.print("\nCerrando servidor...\n");
                    System.exit(1);
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="6");
		
	}
	
	/**
	 * Arranca un servidor de registro RMI si no está actualmente en ejecución 
	 */
	private static void arrancarRegistro(int numPuertoRMI) throws RemoteException{
		
		try {
			Registry registro = LocateRegistry.getRegistry(numPuertoRMI);
			registro.list(); 		
		}catch(RemoteException e) {
			Registry registro = LocateRegistry.createRegistry(numPuertoRMI);	
		}
	}
	
	/**
	 * Funcion auxiliar para mostrar Hashmaps por salida estandar
	 */
	public static void mostrarUsuarios(HashMap<String, User> map, String status) throws RemoteException {
		
		if(map.isEmpty()) {
			System.out.println("\nNo hay usuarios " + status);
		}else {
			map.forEach((k, v) ->
			{			
			    System.out.println(" - " + v);	           
		    });		
		}		
	}
	
	/**
	 * Getter del servicio de datos
	 */
	public static ServicioDatosInterface getDatos() {
		return datos;
	}
	
}
