package es.uned.cliente;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
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
	
	static int puerto = 8888;	
	// Crea una URL para los objetos remotos de los cuales utilizara metodos
	static String URLGestor = "rmi://localhost:" + puerto + "/Gestor";
	static String URLAutenticador = "rmi://localhost:" + puerto + "/Autenticador";
		
	public static void main(String[] args){			
	
		try {			 
			 
			 // ENLCADE CON SERVIDOR: SERVICIOS GESTOR Y AUTENTICADOR 			 
			 // Busqueda del objeto remoto y cast del objeto de la interfaz
			 ServicioGestorInterface gestor = (ServicioGestorInterface)Naming.lookup(URLGestor);
			 ServicioAutenticacionInterface autenticador = (ServicioAutenticacionInterface)Naming.lookup(URLAutenticador);			 
			 System.out.println("Busqueda de gestor y autenticador completa");			
			 
			 mainMenu(gestor, autenticador);
			 
		 } catch (Exception e) {
			 System.out.println("Excepcion en HolaMundoCliente: " + e);
		 }
	}
	public static void mainMenu(ServicioGestorInterface gestor, ServicioAutenticacionInterface autenticador) throws RemoteException{
		Scanner key = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Usuario *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("1: Registrar un nuevo usuario.");
        System.out.println("2: Hacer login");
        System.out.println("3: Salir.");
                       
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = key.nextLine().trim();		
            switch(option){
                case "1":
                	System.out.print("wow 2");
                    break;
                case "2":
                	menu(gestor, autenticador);
                    break;           
                case "3":
                    key.close();
                    System.out.print("\nCerrando cliente...\n");
                    System.exit(1);
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="3");
		
	}
	public static void menu(ServicioGestorInterface gestor, ServicioAutenticacionInterface autenticador) throws RemoteException{
		Scanner key = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Usuario *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("1: Información del Usuario.");
        System.out.println("2: Enviar Trino.");
        System.out.println("3: Listar Usuarios del Sistema.");
        System.out.println("4: Seguir a.");
        System.out.println("5: Dejar de seguir a.");
        System.out.println("6: Borrar trino a los usuarios que todavía no lo han recibido (opcional).");
        System.out.println("7: Salir \"Logout\"");
                
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = key.nextLine().trim();		
            switch(option){
                case "1":
                   	System.out.println("\tServicios activos: \n\t- " + URLGestor + "\n\t- " + URLAutenticador);
                    break;
                case "2":
                	System.out.print("wow 2");
                    break;
                case "3":
                	System.out.print("wow 3");
                    break;
                case "4":
                	System.out.print("wow 4");
                    break;
                case "5":
                	System.out.print("wow 5");
                    break;
                case "6":
                	System.out.print("wow 6");
                    break;
                case "7":
                    key.close();
                    System.out.print("\nCerrando cliente...\n");
                    System.exit(1);
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="7");
		
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
