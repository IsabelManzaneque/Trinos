package es.uned.cliente;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.ServicioDatosInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;
import es.uned.common.User;

/**
 * Clase que contiene el main de la entidad Usuario. Contiene funciones que interactuan con 
 * el servidor para registrarse y logearse/deslogearse en el sitema e interactuar con otros
 * usuarios enviandose trinos y haciendose seguidores unos de otros
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */

public class Usuario {
	
	private static int puerto = 8888;	
	private static String URLGestor = "rmi://localhost:" + puerto + "/Gestor";
	private static String URLAutenticador = "rmi://localhost:" + puerto + "/Autenticador";
	private static ServicioGestorInterface gestor;
	private static ServicioAutenticacionInterface autenticador;
	
	public static void main(String[] args){		
					
		try {			 
			  
			 // Busqueda de los objetos remotos 
			 gestor = (ServicioGestorInterface)Naming.lookup(URLGestor);
			 autenticador = (ServicioAutenticacionInterface)Naming.lookup(URLAutenticador);		
	 
			 mainMenu();
			 
		 } catch (Exception e) {
			 System.out.println("Excepcion en Cliente: " + e);
		 }
	}
	
	/**
	 * Menu principal de Usuario. Permite a los usuarios registrarse y autenticarse
	 * en el sistema, mostrando el menu especifico de cada usuario. 
	 */	
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
	
	/**
	 * Menu de especifico de usuario que permite a estos acceder a las funciones que les permiten
	 * interactuar con otros usuarios. Al hacer logout, volveran al menu principal de usuario
	 */	
	public static void userMenu(String nick) throws RemoteException{
		Scanner scanner = new Scanner(System.in); 
		String option; 
		  
        System.out.println("\n -----------------------------------------------------------");
        System.out.println(" |                    * Menu Usuario *                    |");                               
        System.out.println(" -----------------------------------------------------------\n");
        System.out.println("Bienvenid@, " + nick + "!");
        System.out.println("1: Información del Usuario.");
        System.out.println("2: Enviar Trino.");
        System.out.println("3: Listar Usuarios del Sistema.");
        System.out.println("4: Seguir a.");
        System.out.println("5: Dejar de seguir a.");
        System.out.println("6: Borrar trino a los usuarios que todavía no lo han recibido (opcional).");
        System.out.println("7: Salir \"Logout\"");        
 
        do {         
        	System.out.print("\nEscoja una opcion: ");
        	option = scanner.nextLine().trim();		
            switch(option){
                case "1":
                   	System.out.println("\tServicios activos: \n\t- " + URLGestor + "\n\t- " + URLAutenticador);
                    break;
                case "2":
                	sendTrino(nick);
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
                	borrarTrinos(nick);
                    break;
                case "7":
                    autenticador.desconectar(nick);
                    System.out.print("\nCerrando sesions...\n");
                    mainMenu();
                default: 
                    System.out.print("\nInserte una opcion valida");    
            }            
        } while(option !="7");		
	}	
	
	/**
	 * Registra un nuevo usuario pidiendo sus datos personales y mandandolos
	 * al servidor. El nick debe ser unico para cada usuario
	 */	
	private static void register() throws RemoteException {
		Scanner scanner = new Scanner(System.in); 			
		
		System.out.print("Introduzca su nombre: ");
	    String name = scanner.nextLine().trim().toLowerCase();
		System.out.print("Introduzca su nick: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    System.out.print("Introduzca su password: ");
	    String password = scanner.nextLine().trim().toLowerCase();    
	    
		if(autenticador.registrar(nick, new User(name, nick, password))) {
			System.out.println("Se ha registrado correctamente");	    	
		}else {
			System.out.println("Ya existe un usuario con ese nick");
		}	
	}
	
	/**
	 * Autentica un usuario pidiendo a traves de su nick y contrasena. Si el 
	 * usuario existe, se le asigna un objeto CallbackUsuarioImple
	 */	
	private static void login() throws RemoteException {
		
		Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca su nick: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    System.out.print("Introduzca su password: ");
	    String password = scanner.nextLine().trim().toLowerCase();  
	    	   
		if(autenticador.autenticar(nick, password, new CallbackUsuarioImpl())) {			
			userMenu(nick);
		}else {
			System.out.println("No se encuentra usuario con ese nick / password");
		}			
	}
	
	/**
	 * Muestra a todos los usuarios registrados en el sistema
	 */	
	private static void listarUsuarios() throws RemoteException {
		
		if(gestor.mostrarUsuarios().isEmpty()) {
			System.out.println("No hay usuarios registrados");
		}else {
			System.out.println("Nicks de los usuarios registrados en el sistema: ");
			gestor.mostrarUsuarios().forEach((k, v) ->
			{			
			    System.out.println(" - " + k);	           
		    });		
		}			
	}
	
	
	/**
	 * Comienza a seguir a un usuario si este esta registrado en el sistema.
	 * Al hacerlo, el seguidor comenzara a recibir los trinos del nuevo
	 * contacto si este no esta conectado
	 */	
	private static void follow(String nickFollower) throws RemoteException {
		
		Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca nick de usuario que quiere seguir: ");
	    String nickFollowed = scanner.nextLine().trim().toLowerCase();
		           
		if(gestor.seguir(nickFollower, nickFollowed)) {
			System.out.println("Se ha comenzado a seguir al usuario");
		}else {
			System.out.println("No se encuentra usuario con ese nick");
		}
	}
	

	/**
	 * Deja de seguir a un usuario si este esta en la lista de contactos.
	 * Al hacerlo, el usuario dejara de recibir los trinos del ya no contacto
	 */	
	private static void unfollow(String nickFollower) throws RemoteException {
		
        Scanner scanner = new Scanner(System.in); 		
		
		System.out.print("Introduzca nick de usuario que quiere dejar de seguir: ");
	    String nickFollowed = scanner.nextLine().trim().toLowerCase();
		       	    
		if(gestor.dejarDeSeguir(nickFollower, nickFollowed)) {
			System.out.println("Se ha dejado de seguir al usuario");
		}else {
			System.out.println("El usuario no esta en su lista de contactos");
		}		   
	}		
	
	/**
	 * Envia un trino que recibiran los seguidores del usuario. Si el usuario
	 * esta bloqueado, sus trinos llegaran a sus seguidores cuando se le desbloquee
	 */	
	private static void sendTrino(String miNick) throws RemoteException {
		
		Scanner scanner = new Scanner(System.in); 
		
	    System.out.print("Escriba su trino: ");
	    String mensaje = scanner.nextLine().trim();
	    
	    Trino trino = new Trino(miNick, mensaje);
	    
	    if(gestor.enviarTrino(trino)) {
	    	System.out.print("El trino se ha enviado con éxito");
	    }else {
	    	System.out.print("Tus trinos llegaran cuando se te desbloquee");
	    }
	}
	
	/**
	 * Borra los trinos enviados de aquellos usuarios que no los hubieran 
	 * recibido aun por estar bloqueados o desconectados
	 */	
	private static void borrarTrinos(String nickEmisor) throws RemoteException {
		
		Scanner scanner = new Scanner(System.in); 
		
		System.out.print("Desea borrar sus trinos a los seguidores que aún no los han recibido?(s/n): ");
	    String opcion = scanner.nextLine().trim();
	    
	    while(!opcion.equals("s") && !opcion.equals("s")) {
	    	System.out.print("Inserte una opcion correcta");
	    	opcion = scanner.nextLine().trim();
	    }
	    
	    if(opcion.equals("s")) {
	    	gestor.borrarPendientes(nickEmisor);
	    }		
	}
	
}	




