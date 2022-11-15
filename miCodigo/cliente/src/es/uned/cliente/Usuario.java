package es.uned.cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioGestorInterface;


// Para ejecutar esta clase el servidor debe estar corriendo!! arrancar registro, ejecutar
// mainServidor y no cerrarlo

// Clase que contiene el main de la entidad Usuario/Cliente.
// Clientes: A través de estos, los usuarios, que son los actores principales del sistema, interactúan entre ellos 
// enviándose trinos y haciéndose seguidores unos de otros. Los usuarios se registran en el sistema a través de la 
// entidad Servidor. Una ver registrados se logean usando también esta entidad Servidor para poder enviar trinos y 
// hacerse seguidores de otros usuarios. Así pues, se tiene que implementar un callback para recibir los trinos de 
// aquellos usuarios a los que siguen

public class Usuario {
	
		
	public static void main(String[] args){
		
		
		int puerto = 8888;		
	
		try {			 
			 
			 // ENLCADE CON SERVIDOR: SERVICIOS GESTOR Y AUTENTICADOR
			 
 			 // Crea una URL para los objetos remotos de los cuales utilizara metodos
			 String URLGestor = "rmi://localhost:" + puerto + "/Gestor";
			 String URLAutenticador = "rmi://localhost:" + puerto + "/Autenticador";
			 
			 // Busqueda del objeto remoto y cast del objeto de la interfaz
			 ServicioGestorInterface gestor = (ServicioGestorInterface)Naming.lookup(URLGestor);
			 ServicioAutenticacionInterface autenticador = (ServicioAutenticacionInterface)Naming.lookup(URLAutenticador);			 
			 System.out.println("Busqueda de gestor y autenticador completa");
			
			 // Invoca los objetos remotos 
			 String mensaje = gestor.decirHola("Pato Donald");
			 System.out.println("HolaGestor: " + mensaje);
			 
			 String mensaje2 = autenticador.decirHola("Pato Donald");
			 System.out.println("HolaAutenticador: " + mensaje2);
			 
		 } catch (Exception e) {
			 System.out.println("Excepcion en HolaMundoCliente: " + e);
		 }
	}
}
			 





		
		
		
		//descargamos una instancia del servidor en la variable registry
//		Registry registry = LocateRegistry.getRegistry();
//		//usamos la funcion lookup, devuelve una referencia remota enlazada al nombre (con el que hicimos el rebind)
//		//esto recoge lo que haya enviado el servidor desde la clase MainServidor, un objeto que implementa IServidor
//		servidor = (IServidor)registry.lookup("Pepito");
//		
//		//una vez tenemos el servidor, arrancamos la gui
//		gui();
//	}
//}
	
//	private static void gui() throws RemoteException {
//		int opt = 0;
//		
//		// la clase gui tiene el metodo menu en el que ponemos el nombre del menu
//		// y un array de string con las entradas que tiene el menu. devuelve un entero con la
//		// opcion seleccionada por del usuario que se usara en el switch
//		do {
//			opt = Gui.menu("Menu Principal", 
//							new String[]{ "Autenticarse", 
//										  "Agregar Contacto", 
//										  "Enviar Mensaje",
//										  "Recibir Mensajes",
//										  "Salir" });
//			
//			switch (opt) {
//				case 0: autenticarse(); break;
//				case 1: agregarContacto(); break;
//				case 2: enviarMensaje(); break;
//				case 3: recibirMensajes(); break;
//			}
//		}
//		while (opt != 4);
//	}
//	
//	
//	//Para la autenticacion tambien deberiamos pedir contrasena 
//	private static void autenticarse() throws RemoteException {
//		String nombre = Gui.input("Autenticarse", "Ingrese su nombre: ");
//		
//		if (nombre != null && !nombre.isEmpty()) {
//			//Primera llamada remota, nos devuelve un entero con el id de sesion
//			miSesion = servidor.autenticar(nombre);
//		}
//	}
//	
//	private static void agregarContacto() throws RemoteException {
//		String contacto = Gui.input("Agregar Contacto", "Ingrese el contacto: ");
//		
//		if (contacto != null && !contacto.isEmpty()) {
//			// llamada remota para agregar a alguien, devuelve la sesion de mi contacto
//			int suSesion = servidor.agregar(contacto, miSesion);
//			System.out.println("La sesion de " + contacto + " es " + suSesion);
//		}
//	}
//	
//	
//	private static void recibirMensajes() throws RemoteException {
//		System.out.println("=== Mensajes recibidos ===");
//		
//		// llamada remota al servidor para guardar los mensajes recibidos en en 
//		// una lista de mensajes
//		List<Mensaje> mensajes = servidor.recibir(miSesion);
//		
//		//especificamos el remitente y el cuerpo
//		for (Mensaje mensaje : mensajes) {
//			System.out.println("De " + mensaje.getRemitente());
//			System.out.println("\t" + mensaje.getCuerpo() + "\n");
//		}
//		
//		System.out.println();
//		
//		// al terminar, el servidor debe limpiar el buffer
//		servidor.limpiarBuffer(miSesion);
//	}
//	
//	
//	private static void enviarMensaje() throws RemoteException {
//		String opts[] = Gui.input("Enviar Mensaje", 
//								  new String[]{ "Ingrese la sesion del contacto: ",
//												"Ingrese el mensaje: "});
//		
//		int suSesion = Integer.parseInt(opts[0]);
//		String mensaje = opts[1];
//		
//		//lamada remota al servidor para enviar el mensaje desde mi sesion
//		// a la del receptor
//		servidor.enviar(mensaje, miSesion, suSesion);
//	}
//}
