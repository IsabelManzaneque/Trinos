package es.uned.cliente;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.User;



// Para ejecutar esta clase el servidor debe estar corriendo!! arrancar registro, ejecutar
// mainServidor y no cerrarlo

// Clase que contiene el main de la entidad Usuario/Cliente.
// Clientes: A trav�s de estos, los usuarios, que son los actores principales del sistema, interact�an entre ellos 
// envi�ndose trinos y haci�ndose seguidores unos de otros. Los usuarios se registran en el sistema a trav�s de la 
// entidad Servidor. Una ver registrados se logean usando tambi�n esta entidad Servidor para poder enviar trinos y 
// hacerse seguidores de otros usuarios. As� pues, se tiene que implementar un callback para recibir los trinos de 
// aquellos usuarios a los que siguen

public class Usuario {
	
	static int puerto = 8888;	
	// Crea una URL para los objetos remotos de los cuales utilizara metodos
	static String URLGestor = "rmi://localhost:" + puerto + "/Gestor";
	static String URLAutenticador = "rmi://localhost:" + puerto + "/Autenticador";
	
	static ServicioGestorInterface gestor;
	static ServicioAutenticacionInterface autenticador;
	
	public static void main(String[] args){		
					
		try {			 
			 
			 // ENLCADE CON SERVIDOR: SERVICIOS GESTOR Y AUTENTICADOR 			 
			 // Busqueda de los objetos remotos y cast del objeto de la interfaz
			 gestor = (ServicioGestorInterface)Naming.lookup(URLGestor);
			 autenticador = (ServicioAutenticacionInterface)Naming.lookup(URLAutenticador);			 
			 System.out.println("Busqueda de gestor y autenticador completa");			
			 
			 mainMenu();
			 
		 } catch (Exception e) {
			 System.out.println("Excepcion en Cliente: " + e);
		 }
	}
	
	
	public static void mainMenu() throws RemoteException{
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
                	register();
                    break;
                case "2":
                	login();
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
	public static void userMenu(String nick) throws RemoteException{
		Scanner scanner = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Usuario *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("Bienvenid@, " + nick + "!");
        System.out.println("1: Informaci�n del Usuario.");
        System.out.println("2: Enviar Trino.");
        System.out.println("3: Listar Usuarios del Sistema.");
        System.out.println("4: Seguir a.");
        System.out.println("5: Dejar de seguir a.");
        System.out.println("6: Borrar trino a los usuarios que todav�a no lo han recibido (opcional).");
        System.out.println("7: Salir \"Logout\"");
                
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = scanner.nextLine().trim();		
            switch(option){
                case "1":
                   	System.out.println("\tServicios activos: \n\t- " + URLGestor + "\n\t- " + URLAutenticador);
                    break;
                case "2":
                	System.out.print("wow 2");
                    break;
                case "3":
                	listarUsuarios();
                    break;
                case "4":
                	follow(nick);
                    break;
                case "5":
                	unfollow(nick);
                    break;
                case "6":
                	System.out.print("wow 6");
                    break;
                case "7":
                    scanner.close();
                    autenticador.desconectar(nick);
                    System.out.print("\nCerrando cliente...\n");
                    System.exit(1);
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="7");
		
	}
	
	
	// LA INFO PERSONAL ES NOMBRE< NICK (ID) Y PASSWORD

	private static void register() {
		Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca su nombre: ");
	    String name = scanner.nextLine().trim().toLowerCase();
		System.out.print("Introduzca su nick: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    System.out.print("Introduzca su password: ");
	    String password = scanner.nextLine().trim().toLowerCase();
	    
	    
	    try {
			if(autenticador.registrar(nick, new User(name, nick, password))) {
				System.out.println("Se ha registrado correctamente");	    	
			}else {
				System.out.println("Ya existe un usuario con ese nick");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
	}
	
	private static void login() {
		
		Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca su nick: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    System.out.print("Introduzca su password: ");
	    String password = scanner.nextLine().trim().toLowerCase();
	    
	    
	    try {
			if(autenticador.autenticar(nick, password)) {
				userMenu(nick);
			}else {
				System.out.println("No se encuentra usuario con ese nick / password");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  		
	}
	
	
	private static void listarUsuarios() throws RemoteException {
		
		if(gestor.mostrarUsuarios().isEmpty()) {
			System.out.println("No hay usuarios registrados");
		}else {
			gestor.mostrarUsuarios().forEach((k, v) ->
			{			
			    System.out.println("key: " + k + " value: " + v);	           
		    });		
		}			
	}
	
	
	private static void follow(String miNick) {
		
		Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca nick de usuario que quiere seguir: ");
	    String suNick = scanner.nextLine().trim().toLowerCase();
		       
	    try {
			if(gestor.seguir(miNick, suNick)) {
				System.out.println("Se ha comenzado a seguir al usuario");
			}else {
				System.out.println("No se encuentra usuario con ese nick");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}  
	}
	
	private static void unfollow(String miNick) {
		
        Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca nick de usuario que quiere dejar de seguir: ");
	    String suNick = scanner.nextLine().trim().toLowerCase();
		       
	    try {
			if(gestor.dejarDeSeguir(miNick, suNick)) {
				System.out.println("Se ha dejado de seguir al usuario");
			}else {
				System.out.println("El usuario no esta en su lista de contactos");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}   
	}
		
	
	
	private static void enviarTrino() {
		
	}

}	




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