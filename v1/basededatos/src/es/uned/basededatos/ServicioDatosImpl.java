package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import es.uned.common.Trino;
import es.uned.common.User;
import es.uned.common.ServicioDatosInterface;


// Servicio Datos: Este servicio hará las funciones de una “base de datos” que relacione Usuarios-Seguidores-Trinos.
// Es decir, mantendrá la lista de usuarios registrados y/o conectados al sistema, junto con sus seguidores y trinos; 
// y los relacionará permitiendo operaciones típicas de consulta, añadir y borrado. También debe posibilitar la operación 
// de bloqueo de una cuenta (banear). (Servicio Autenticación y Servicio Gestor) harán uso de este servicio para realizar 
// las operaciones sobre el estado  de los usuarios del sistema y sus seguidores.
// El equipo docente recomienda para la implementación del servicio las clases List y HashMap de Java.

//Los metodos remotos para autenticarse y registrarse los debe tener la clase impl de la base de datos

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	protected ServicioDatosImpl() throws RemoteException {
		super();
	}

	private HashMap<String, User> usuariosRegistrados = new HashMap<>();	
	private HashMap<String, User> usuariosConectados = new HashMap<>();	
	// mapa donde key es un usuario y value su lista de contactos
	private HashMap<String, ArrayList<User>> contactos = new HashMap<>();
	// mapa donde key es el usuario y value su lista de trinos 
	private HashMap<String, ArrayList<Trino>> trinos = new HashMap<>();
	// mapa con el usuario pendiente de recibir mensajes y una lista de los mensajes pendientes
	private HashMap<String, ArrayList<Trino>> trinosPendientes = new HashMap<>();	
	

	
	public boolean agregarUsuario(String nick, User user) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nick)) {
			return false;
		}
		usuariosRegistrados.put(nick, user);
		contactos.put(nick, new ArrayList<>());
		trinos.put(nick,new ArrayList<>());
		return true;
	}
	
	
	public void borrarUsuario(String nick) throws RemoteException {	
		
		usuariosRegistrados.remove(nick);
	}
	
	public boolean agregarConectado(String nick, String password) throws RemoteException {
		
		if(!usuariosRegistrados.containsKey(nick) || !usuariosRegistrados.get(nick).getPassword().equals(password)) {
			return false;
		}
		usuariosConectados.put(nick, usuariosRegistrados.get(nick));
		return true;
	}
	
	public void borrarConectado(String nick) throws RemoteException {
		
		usuariosConectados.remove(nick);
	}
	
	public boolean agregarContacto(String miNick, String suNick) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(suNick)) {			
			contactos.get(miNick).add(usuariosRegistrados.get(suNick));
			return true;		
		}
		return false;
	}
	
	public boolean borrarContacto(String miNick, String suNick) throws RemoteException{
		
		if(contactos.get(miNick).contains(usuariosRegistrados.get(suNick))) {
			contactos.get(miNick).remove(usuariosRegistrados.get(suNick));        
			return true;
		}
		return false;		
	}		

	
	public void banearUsuario() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public void mostrarTrinos() throws RemoteException{
		if(trinos.isEmpty()) {
			System.out.println("No hay trinos en la base de datos");
		}else {
			trinos.forEach((k, v) ->
			{			
			    System.out.println("Usuario: " + k + "  Trinos (timestamp):");	  
			    
			    for(Trino t : v) {
			    	System.out.println("	- " + t.GetTimestamp());	
			    }
		    });		
		}		
	}

	
	public boolean agregarTrino(Trino trino) throws RemoteException {
		
		//el trino lo deben recibir todos los contactos del emisor 
		//los que esten online, automaticamente
		//los que no, cuando se conecten
		return true;
		
	}

	
	public void borrarTrino(Trino trino) throws RemoteException {
		
		
	}

	
	public void limpiarBuffer(String name) throws RemoteException {
		
		// comprueba que la sesion es valida
		if (!usuariosRegistrados.containsKey(name)) {
			throw new RuntimeException("no exite el usuario");
		}
		// si lo es, borra todos los elementos de la lista
		trinosPendientes.get(name).clear();
		
	}
	
	public HashMap<String, User> getUsuariosRegistrados() throws RemoteException {
		return usuariosRegistrados;
	}

	public HashMap<String, User> getUsuariosConectados() throws RemoteException {
		return usuariosConectados;
	}
	
	public HashMap<String, ArrayList<User>> getContactos() throws RemoteException {
		return contactos;
	}

	

}
