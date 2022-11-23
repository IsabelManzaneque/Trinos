package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.Map;


import es.uned.common.Trino;
import es.uned.common.ServicioDatosInterface;


// Servicio Datos: Este servicio hará las funciones de una “base de datos” que relacione Usuarios-Seguidores-Trinos.
// Es decir, mantendrá la lista de usuarios registrados y/o conectados al sistema, junto con sus seguidores y trinos; 
// y los relacionará permitiendo operaciones típicas de consulta, añadir y borrado.
// Además, también debe posibilitar la operación de bloqueo de una cuenta (banear). Los dos servicios anteriores 
// (Servicio Autenticación y Servicio Gestor) harán uso de este servicio para realizar las operaciones sobre el estado 
// de los usuarios del sistema y sus seguidores.
// El equipo docente recomienda para la implementación del servicio las clases List y HashMap de Java.

//Los metodos remotos para autenticarse y registrarse los debe tener la clase impl de la base de datos

public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	protected ServicioDatosImpl() throws RemoteException {
		super();
	}

	private HashMap<String,String> usuariosRegistrados = new HashMap<>();	
	private HashMap<String,String> usuariosConectados = new HashMap<>();	
	// mapa donde key es el usuario y value su lista de trinos 
	private HashMap<String, ArrayList<Trino>> trinos = new HashMap<>();
	// mapa donde key es un usuario y value su lista de contactos
	private HashMap<String, ArrayList<String>> contactos = new HashMap<>();
	// mapa con el usuario pendiente de recibir mensajes y una lista de los mensajes pendientes
	private HashMap<String, ArrayList<Trino>> buffer = new HashMap<>();	
	

	
	public boolean agregarUsuario(String nick, String password) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nick)) {
			return false;
		}
		usuariosRegistrados.put(nick, password);
		contactos.put(nick, new ArrayList<>());
		trinos.put(nick,new ArrayList<>());
		return true;
	}
	
	
	public void borrarUsuario(String nick) throws RemoteException {	
		
		usuariosRegistrados.remove(nick);
	}
	
	public boolean agregarConectado(String nick, String password) throws RemoteException {
		
		if(!usuariosRegistrados.containsKey(nick) || !usuariosRegistrados.get(nick).equals(password)) {
			return false;
		}
		usuariosConectados.put(nick, password);
		return true;
	}
	
	public void borrarConectado(String nick) throws RemoteException {
		
		usuariosConectados.remove(nick);
	}
	
	public boolean agregarContacto(String miNick, String suNick) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(suNick)) {			
			contactos.get(miNick).add(suNick);
			return true;		
		}
		return false;
	}
	
	public boolean borrarContacto(String miNick, String suNick) throws RemoteException{
		
		if(contactos.get(miNick).contains(suNick)) {
			contactos.get(miNick).remove(suNick);        
			return true;
		}
		return false;
		
	}		

	

	@Override
	public void banearUsuario() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agregarTrino(Trino trino) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void borrarTrino(Trino trino) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void limpiarBuffer(String name) throws RemoteException {
		
		// comprueba que la sesion es valida
		if (!usuariosRegistrados.containsKey(name)) {
			throw new RuntimeException("no exite el usuario");
		}
		// si lo es, borra todos los elementos de la lista
		buffer.get(name).clear();
		
	}
	
	public HashMap<String, String> getUsuariosRegistrados() throws RemoteException {
		return usuariosRegistrados;
	}

	public HashMap<String, String> getUsuariosConectados() throws RemoteException {
		return usuariosConectados;
	}
	
	public HashMap<String, ArrayList<String>> getContactos() throws RemoteException {
		return contactos;
	}

	

}
