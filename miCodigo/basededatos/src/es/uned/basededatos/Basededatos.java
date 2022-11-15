package es.uned.basededatos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// Clase que contiene el main de la entidad Base de Datos.
//Base de datos: Esta entidad es la encargada de almacenar todos los datos del sistema:
//Usuarios, Seguidores, Trinos, ...; S�lo la entidad Servidor puede consumir el servicio
//que suministra esta entidad
public class Basededatos {	
	
	public static void main(String[] args) {
		
		
		int puerto = 5555;
		
		try {
			
			arrancarRegistro(puerto);
			
			ServicioDatosImpl datos = new ServicioDatosImpl();
			String URLDatos = "rmi://localhost:"+ puerto + "/Datos";
			
			Naming.rebind(URLDatos, datos);
			
			System.out.println("BBDD registrada. El registro contiene actualmente:");
			
			// lista de los nombres que se encuentran en el registro actualmente
			listaRegistro(URLDatos);
			System.out.println("Base de datos preparada.");
			
		}catch(Exception e){
			System.out.println("Excepci�n en Servidor.main: " + e);			
		}		
	}
	
	
	// arranca un servidor de registro RMI si no est� actualmente en ejecuci�n, 
	// en un n�mero de puerto especificado por el usuario
	private static void arrancarRegistro(int numPuertoRMI) throws RemoteException{
		
		try {
			Registry registro = LocateRegistry.getRegistry(numPuertoRMI);
			registro.list(); // Esta llamada lanza una excepci�n si el registro no existe
		
		}catch(RemoteException e) {
			// Registro no v�lido en este puerto
			 System.out.println("El registro RMI no se puede localizar en el puerto "+ numPuertoRMI);
			 Registry registro = LocateRegistry.createRegistry(numPuertoRMI);
			 System.out.println("Registro RMI creado en el puerto " + numPuertoRMI);			
		}
	}
	
	// Este m�todo lista los nombres registrados con un objeto Registry
	private static void listaRegistro(String URLRegistro) throws RemoteException, MalformedURLException {
		
		System.out.println("Registro " + URLRegistro + " contiene: ");
		 String [] names = Naming.list(URLRegistro);
		 for (int i=0; i<names.length; i++)
			 System.out.println(names[i]);
	}

}
