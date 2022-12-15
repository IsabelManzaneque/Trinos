package es.uned.basededatos;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * La clase Basededatos se encarga de almacenar todos los datos del sistema. 
 * Ofrece su servicio a la entidad Servidor
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */
public class Basededatos {	
	
	private static int puerto = 5555;
	private static String URLDatos = "rmi://localhost:"+ puerto + "/Datos";
	private static ServicioDatosImpl datos;
	
	/**
	 * Main de la Base de datos que levanta el servicio y publica
	 * su servicio de datos 
	 */
	public static void main(String[] args) {
		
		try {			
			
			datos = new ServicioDatosImpl();			
			arrancarRegistro(puerto);			
			// Exporta el objeto remoto	
			Naming.rebind(URLDatos, datos);				
			menu();
			
		}catch(Exception e){
			System.out.println("Excepción en Basededatos.main: " + e);			
		}		
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
	 * Menu principal de la base de datos que llama a cada funcion que implementa
	 * el servicio de datos
	 */	
	public static void menu() throws RemoteException{
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
                	System.out.println("\tServicios activos: \n\t- " + URLDatos);
                	System.out.println("\tUsuarios registrados: \n\t- " + datos.getUsuariosRegistrados().size());
                	System.out.println("\tUsuarios conectados: \n\t- " + datos.getUsuariosConectados().size());
                	System.out.println("\tUsuarios bloqueados: \n\t- " + datos.getUsuariosBloqueados().size());
                    break;
                case "2":
                	datos.mostrarTrinos();
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

}
