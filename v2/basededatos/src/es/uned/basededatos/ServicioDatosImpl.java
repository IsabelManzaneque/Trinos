package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import es.uned.common.Trino;
import es.uned.common.User;
import es.uned.common.CallbackUsuarioInterface;
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
	private HashMap<String, User> usuariosBloqueados = new HashMap<>();	
	// mapa donde key es un usuario y value su lista de contactos
	private HashMap<String, ArrayList<User>> contactos = new HashMap<>();
	// mapa donde key es un usuario y value su lista de seguidores
	private HashMap<String, ArrayList<User>> seguidores = new HashMap<>();
	// mapa con el usuario pendiente de recibir mensajes y una lista de los mensajes pendientes
	private HashMap<String, ArrayList<Trino>> trinosPendientes = new HashMap<>();	
	// array con todos los trinos de todos los usuarios 
	private ArrayList<Trino> trinos = new ArrayList<>();
	
	

	
	public boolean agregarUsuario(String nick, User user) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nick)) {
			return false;
		}
		usuariosRegistrados.put(nick, user);
		contactos.put(nick, new ArrayList<>());
		seguidores.put(nick, new ArrayList<>());
		trinosPendientes.put(nick,new ArrayList<>());
		return true;
	}
	
	
	public void borrarUsuario(String nick) throws RemoteException {	
		
		usuariosRegistrados.remove(nick);
	}
	
	public boolean agregarConectado(String nick, String password, CallbackUsuarioInterface objCallback) throws RemoteException {
		
		if(!usuariosRegistrados.containsKey(nick) || !usuariosRegistrados.get(nick).getPassword().equals(password) 
		    || usuariosConectados.containsKey(nick)) {
			return false;
		}		
		usuariosConectados.put(nick, usuariosRegistrados.get(nick));		
		usuariosConectados.get(nick).setObjCallback(objCallback);		
		
		if(!usuariosBloqueados.containsKey(nick)) {
			CallbackUsuarioInterface proxCliente =  usuariosConectados.get(nick).getObjCallback();		
			for(Trino trino: trinosPendientes.get(nick)) {
				proxCliente.notificame(trino);
			}
			limpiarBuffer(nick);	
		}
		
		return true;
	}
	
	public void borrarConectado(String nick) throws RemoteException {
		
		usuariosConectados.get(nick).setObjCallback(null);
		usuariosConectados.remove(nick);
	}
	
	public boolean agregarContacto(String miNick, String suNick) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(suNick)) {			
			contactos.get(miNick).add(usuariosRegistrados.get(suNick));
			seguidores.get(suNick).add(usuariosRegistrados.get(miNick));
			return true;		
		}
		return false;
	}
	
	public boolean borrarContacto(String miNick, String suNick) throws RemoteException{
		
		if(contactos.get(miNick).contains(usuariosRegistrados.get(suNick))) {
			contactos.get(miNick).remove(usuariosRegistrados.get(suNick));  
			seguidores.get(suNick).remove(usuariosRegistrados.get(miNick));
			return true;
		}
		return false;		
	}		

	
	public boolean agregarBloqueado(String nick) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nick)) {		
			usuariosBloqueados.put(nick, usuariosRegistrados.get(nick));
			return true;
		}
		return false;
		
	}
	
	public boolean borrarBloqueado(String nick) throws RemoteException {
		
		if(usuariosBloqueados.containsKey(nick)) {
			usuariosBloqueados.remove(nick);
			
			CallbackUsuarioInterface objCB =  usuariosConectados.get(nick).getObjCallback();		
			for(Trino trino: trinosPendientes.get(nick)) {
				objCB.notificame(trino);
			}
			limpiarBuffer(nick);
			
			return true;
		}
		return false;		
	}
	
	public void mostrarTrinos() throws RemoteException{
		if(trinos.isEmpty()) {
			System.out.println("No hay trinos en la base de datos");
		}else {
			
			for(Trino t : trinos) {
				System.out.println("Propietario: " + t.GetNickPropietario() + "  |  Timestamp: " + t.GetTimestamp());
			}
		}		
	}

	
	public void agregarTrino(Trino trino) throws RemoteException {
		
		trinos.add(trino);
				
	}
	
	public void agregarTrinoPendiente(String nickReceptor, Trino trino) throws RemoteException {
		
		trinosPendientes.get(nickReceptor).add(trino);		
		
	}

	/* Itera por la lista de trinos pendientes de un usuario y si el nick del emisor
	 * del trino es el especificado, se borra el trino */
	public void borrarTrinosPendientes(String nickEmisor, String nickReceptor) throws RemoteException {
				
		Iterator<Trino> it = getTrinosPendientes().get(nickReceptor).iterator();
		
		while(it.hasNext()) {
			if(it.next().GetNickPropietario().equals(nickEmisor)) {
				it.remove();
			}
		}	
	}

	
	public void limpiarBuffer(String nick) throws RemoteException {		

		trinosPendientes.get(nick).clear();		
	}
	
	public HashMap<String, User> getUsuariosRegistrados() throws RemoteException {
		return usuariosRegistrados;
	}

	public HashMap<String, User> getUsuariosConectados() throws RemoteException {
		return usuariosConectados;
	}
	
	public HashMap<String, User> getUsuariosBloqueados() throws RemoteException {
		return usuariosBloqueados;
	}
	
	public HashMap<String, ArrayList<User>> getContactos() throws RemoteException {
		return contactos;
	}
	
	public HashMap<String, ArrayList<User>> getSeguidores() throws RemoteException {
		return seguidores;
	}
	
	public HashMap<String, ArrayList<Trino>> getTrinosPendientes() throws RemoteException {
		return trinosPendientes;
	}
	

	

}
