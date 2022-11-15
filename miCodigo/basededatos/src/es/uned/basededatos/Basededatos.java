package es.uned.basededatos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

// Clase que contiene el main de la entidad Base de Datos.
//Base de datos: Esta entidad es la encargada de almacenar todos los datos del sistema:
//Usuarios, Seguidores, Trinos, ...; Sólo la entidad Servidor puede consumir el servicio
//que suministra esta entidad
public class Basededatos {	
	
	static int puerto = 5555;
	// Crea una URL para los objetos remotos de los cuales utilizara metodos
	static String URLDatos = "rmi://localhost:"+ puerto + "/Datos";
	
	public static void main(String[] args) {

		
		try {
			
			// ENLCADE CON SERVICIOS GESTOR Y AUTENTICADOR
			arrancarRegistro(puerto);			
			// Crea un objeto de la clase impl que implementa la interfaz remota
			ServicioDatosImpl datos = new ServicioDatosImpl();			
			// Exporta el objeto remoto	
			Naming.rebind(URLDatos, datos);			
			
			menu(datos);
			
		}catch(Exception e){
			System.out.println("Excepción en Basededatos.main: " + e);			
		}		
	}
	
	
	// arranca un servidor de registro RMI si no está actualmente en ejecución, 
	// en un número de puerto especificado por el usuario
	private static void arrancarRegistro(int numPuertoRMI) throws RemoteException{
		
		try {
			Registry registro = LocateRegistry.getRegistry(numPuertoRMI);
			registro.list(); // Esta llamada lanza una excepción si el registro no existe
		
		}catch(RemoteException e) {
			 // Registro no válido en este puerto
			 System.out.println("El registro RMI no se puede localizar en el puerto "+ numPuertoRMI);
			 Registry registro = LocateRegistry.createRegistry(numPuertoRMI);
			 System.out.println("Registro RMI creado en el puerto " + numPuertoRMI);			
		}
	}
	
	
	public static void menu(ServicioDatosImpl datos) throws RemoteException{
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
                	System.out.println("\tUsuarios registrados: \n\t- " + datos.getUsuarios().size());
                	System.out.println("\tUsuarios conectados: \n\t- " + datos.getUsuariosConectados().size());
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

}
