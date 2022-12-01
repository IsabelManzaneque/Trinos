package es.uned.servidor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.User;


//La entidad Servidor se encarga de controlar el proceso de autenticaci�n de
//los usuarios del sistema y gesti�n de sus mensajes

//La clase del servidor de objeto instancia y exporta un objeto de la implementaci�n de
//la interfaz remota. La Figura 7.10 muestra una plantilla para la clase del servidor de
//objeto.

public class Servidor {

	
	private static int puertoSalida = 8888;	
	private static int puertoEntrada = 5555;
	// Crea una URL para los objetos remotos de los cuales utilizara metodos
	private static String URLGestor = "rmi://localhost:"+ puertoSalida + "/Gestor";
	private static String URLAutenticador = "rmi://localhost:"+ puertoSalida + "/Autenticador";
	private static String URLDatos = "rmi://localhost:" + puertoEntrada + "/Datos";
	
	private static ServicioDatosInterface datos;
	
	public static void main(String[] args) throws Exception {		
		
		try {			
			
			// ENLACE CON LA BBDD			
		    // Busqueda del objeto remoto y cast del objeto de la interfaz
			datos = (ServicioDatosInterface)Naming.lookup(URLDatos);		
			
	  		// ENLCADE CON SERVICIOS GESTOR Y AUTENTICADOR			 
			// arranca el registro
			arrancarRegistro(puertoSalida);
			 
			// Crea un objeto de las clases impl que implementan las interfaces remotas
			ServicioGestorImpl gestor = new ServicioGestorImpl();
			ServicioAutenticacionImpl autenticador = new ServicioAutenticacionImpl();
					
			Naming.rebind(URLGestor, gestor);
			Naming.rebind(URLAutenticador, autenticador);
						
			menu(gestor, autenticador,datos);
			
		}catch(Exception e) {
			System.out.println("Excepci�n en Servidor.main: " + e);
		}

	}
	
	// arranca un servidor de registro RMI si no est� actualmente en ejecuci�n, 
	private static void arrancarRegistro(int numPuertoRMI) throws RemoteException{
		
		try {
			Registry registro = LocateRegistry.getRegistry(numPuertoRMI);
			// lanza una excepci�n si el registro no existe
			registro.list(); 		
		}catch(RemoteException e) {
			// Registro no v�lido en este puerto
			System.out.println("El registro RMI no se puede localizar en el puerto "+ numPuertoRMI);
			Registry registro = LocateRegistry.createRegistry(numPuertoRMI);
			System.out.println("Registro RMI creado en el puerto " + numPuertoRMI);			
		}
	}
	
	
	public static void menu(ServicioGestorImpl gestor,ServicioAutenticacionImpl autenticador, ServicioDatosInterface datos) throws RemoteException{
		Scanner key = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Servidor *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("1: Informaci�n del Servidor.");
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
                	System.out.print("wow 4");
                    break;
                case "5":
                	System.out.print("wow 5");
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
	
	
	public static ServicioDatosInterface getDatos() {
		return datos;
	}
	
	public static void mostrarUsuarios(HashMap<String, User> map, String status) throws RemoteException {
		
		if(map.isEmpty()) {
			System.out.println("No hay usuarios " + status);
		}else {
			map.forEach((k, v) ->
			{			
			    System.out.println("key: " + k + " value: " + v);	           
		    });		
		}		
	}
	
}