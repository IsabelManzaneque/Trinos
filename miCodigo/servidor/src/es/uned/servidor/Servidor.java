package es.uned.servidor;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;

//La entidad Servidor se encarga de controlar el proceso de autenticación de
//los usuarios del sistema y gestión de sus mensajes

//La clase del servidor de objeto instancia y exporta un objeto de la implementación de
//la interfaz remota. La Figura 7.10 muestra una plantilla para la clase del servidor de
//objeto.

public class Servidor {
	
	//private ServicioDatosInterface bbdd;
	
	public static void main(String[] args) throws Exception {
		
		
		int puerto = 8888;	
		int puertoBBDD = 5555;
		
		try {
			
			 // Crea una URL para los objetos remotos de los cuales utilizara metodos
			 String URLDatos = "rmi://localhost:" + puertoBBDD + "/Datos";
		     // Busqueda del objeto remoto y cast del objeto de la interfaz
			 ServicioDatosInterface datos = (ServicioDatosInterface)Naming.lookup(URLDatos);
			 System.out.println("Busqueda de la bbdd completa");			 
			// Invoca los objetos remotos 
			 String mensaje = datos.decirHola("Pato Donald");
			 System.out.println("HolaBBDD: " + mensaje);
			
//			se crea un objeto de la clase que implementa la interfaz remota; Para exportar el objeto, se debe registrar su 
//          referencia en un servicio de directorios. Se utilizará el servicio rmiregistry. Un servidor rmiregistry debe 
//			ejecutarse en el nodo del servidor de objeto para poder registrar objetos RMI.
			
			arrancarRegistro(puerto);
			
			ServicioGestorImpl gestor = new ServicioGestorImpl();
			ServicioAutenticacionImpl autenticador = new ServicioAutenticacionImpl();
			
			String URLGestor = "rmi://localhost:"+ puerto + "/Gestor";
			String URLAutenticador = "rmi://localhost:"+ puerto + "/Autenticador";
			
			Naming.rebind(URLGestor, gestor);
			Naming.rebind(URLAutenticador, autenticador);
			System.out.println("Servidor registrado. El registro contiene actualmente:");
			
			// lista de los nombres que se encuentran en el registro actualmente
			listaRegistro(URLAutenticador);
			System.out.println("Servidor HolaMundo preparado.");

			
		}catch(Exception e) {
			System.out.println("Excepción en Servidor.main: " + e);
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
	
	// Este método lista los nombres registrados con un objeto Registry
	private static void listaRegistro(String URLRegistro) throws RemoteException, MalformedURLException {
		
		System.out.println("Registro " + URLRegistro + " contiene: ");
		 String [] names = Naming.list(URLRegistro);
		 for (int i=0; i<names.length; i++)
			 System.out.println(names[i]);
	}

}
