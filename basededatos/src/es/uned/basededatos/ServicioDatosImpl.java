package es.uned.basededatos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import es.uned.common.Trino;
import es.uned.common.User;
import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioDatosInterface;


/**
 * Clase que mantiene la lista de usuarios registrados y/o conectados al sistema, 
 * junto con sus seguidores y trinos;  Relaciona estos permitiendo operaciones de 
 * consulta, añadir, borrado, bloqueo y desbloqueo
 */
public class ServicioDatosImpl extends UnicastRemoteObject implements ServicioDatosInterface{
	
	protected ServicioDatosImpl() throws RemoteException {
		super();
	}

	private HashMap<String, User> usuariosRegistrados = new HashMap<>();	
	private HashMap<String, User> usuariosConectados = new HashMap<>();	
	private HashMap<String, User> usuariosBloqueados = new HashMap<>();	
	private HashMap<String, ArrayList<User>> contactos = new HashMap<>();
	private HashMap<String, ArrayList<User>> seguidores = new HashMap<>();
	private HashMap<String, ArrayList<Trino>> trinosPendientes = new HashMap<>();	
	private ArrayList<Trino> trinos = new ArrayList<>();
	
	
	/**
	 * Anade un usuario al HashMap usuariosRegistrados. Asocia al nuevo usuario
	 * estructuras para guardar sus trinos pendientes, seguidores y contactos
	 */
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
	
	/**
	 * Borra un usuario del HashMap usuariosRegistrados. Borra las estructuras
	 * asociadas para guardar sus trinos pendientes, seguidores y contactos
	 */
	public void borrarUsuario(String nick) throws RemoteException {	
		
		usuariosRegistrados.remove(nick);
		contactos.remove(nick);
		seguidores.remove(nick);
		trinosPendientes.remove(nick);
	}
	
	/**
	 * Anade un usuario al HashMap usuariosConectados. Comprueba si el usuario
	 * tiene trinos pendientes y si se cumple con las condiciones, los muestra.
	 * Se asocia al usuario un objeto que implementa CallbackUsuarioInterface
	 */
	public boolean agregarConectado(String nick, String password, CallbackUsuarioInterface objCallback) throws RemoteException {
		
		if(!usuariosRegistrados.containsKey(nick) || !usuariosRegistrados.get(nick).getPassword().equals(password) 
		    || usuariosConectados.containsKey(nick)) {
			return false;
		}		
		usuariosConectados.put(nick, usuariosRegistrados.get(nick));		
		usuariosConectados.get(nick).setObjCallback(objCallback);		
		
		// Si el usuario no esta bloqueado
		if(!usuariosBloqueados.containsKey(nick)) {
			CallbackUsuarioInterface objCB =  usuariosConectados.get(nick).getObjCallback();		
			
			for(Trino trino: trinosPendientes.get(nick)) {	
				//Si el emisor del trino pendiente no esta bloqueado
				if(!usuariosBloqueados.containsKey(trino.GetNickPropietario())) {
					objCB.notificame(trino);
				}
			}
			limpiarBuffer(nick);	
		}		
		return true;
	}
	
	/**
	 * Borra un usuario del HashMap usuariosConectados. Al hacerlo, evalua 
	 * su Objeto CallbackUsuarioInterface a null.
	 */
	public void borrarConectado(String nick) throws RemoteException {
		
		usuariosConectados.get(nick).setObjCallback(null);
		usuariosConectados.remove(nick);
	}
	
	/**
	 * Anade un usuario al HashMap de contactos de otro usuario. 
	 */
	public boolean agregarContacto(String nickFollower, String nickFollowed) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nickFollowed)) {		
			contactos.get(nickFollower).add(usuariosRegistrados.get(nickFollowed));
			seguidores.get(nickFollowed).add(usuariosRegistrados.get(nickFollower));
			return true;		
		}
		return false;
	}
	
	/**
	 * Borra un usuario del HashMap de contactos de otro usuario. 
	 */
	public boolean borrarContacto(String nickFollower, String nickFollowed) throws RemoteException{
		
		if(contactos.get(nickFollower).contains(usuariosRegistrados.get(nickFollowed))) {
			contactos.get(nickFollower).remove(usuariosRegistrados.get(nickFollowed));  
			seguidores.get(nickFollowed).remove(usuariosRegistrados.get(nickFollower));
			return true;
		}
		return false;		
	}		

	/**
	 * Anade un usuario al HashMap de usuarios bloqueados. 
	 */
	public boolean agregarBloqueado(String nick) throws RemoteException {
		
		if(usuariosRegistrados.containsKey(nick)) {		
			usuariosBloqueados.put(nick, usuariosRegistrados.get(nick));
			return true;
		}
		return false;
		
	}
	
	/**
	 * Elimina un usuario al HashMap de usuarios bloqueados. Si tenia trinos
	 * pendientes de usuarios, los recibe. Si otros usuarios tenian trinos
	 * pendientes del usuario desbloqueado, los reciben.
	 */
	public boolean borrarBloqueado(String nick) throws RemoteException {
		
		if(usuariosBloqueados.containsKey(nick)) {
			usuariosBloqueados.remove(nick);
			
			// cuando un usuario es desbloqueado, recibe los trinos pendientes de sus contactos. 
			// Si se le desbloquea mientras esta offline, recibira los pendientes al logearse
			if(usuariosConectados.containsKey(nick)) {
				CallbackUsuarioInterface objCB1 =  usuariosConectados.get(nick).getObjCallback();		
				for(Trino trino: trinosPendientes.get(nick)) {
					objCB1.notificame(trino);
				}
				limpiarBuffer(nick);
			}			
			// si los seguidores del usuario desbloqueado tienen trinos pendientes suyos, los 
			// reciben. Si los seguidores estan offline, recibiran los trinos al conectarse
			for(User u : seguidores.get(nick)) {		
				if(usuariosConectados.containsKey(u.getNick())) {
					CallbackUsuarioInterface objCB2 = u.getObjCallback();
					for(Trino trino: trinosPendientes.get(u.getNick())) {
						if(trino.GetNickPropietario().equals(nick)) {
							objCB2.notificame(trino);
						}
					}					
					borrarTrinosPendientes(nick, u.getNick());					
				}					
			}			
			return true;
		}
		return false;		
	}
	
	/**
	 * Muestra los trinos guardados en el ArrayList de trinos si los hay. Si no
	 * los hay, se informa al usuario
	 */
	public void mostrarTrinos() throws RemoteException{
		if(trinos.isEmpty()) {
			System.out.println("No hay trinos en la base de datos");
		}else {
			
			for(Trino t : trinos) {
				System.out.println("Propietario: " + t.GetNickPropietario() + "  |  Timestamp: " + t.GetTimestamp());
			}
		}		
	}

	/**
	 * Anade un nuevo trino al ArrayList de trinos
	 */
	public void agregarTrino(Trino trino) throws RemoteException {
		
		trinos.add(trino);				
	}
	
	/**
	 * Anade un nuevo trino al HashMap de trinos pendientes vinculado a
	 * un usuario 
	 */
	public void agregarTrinoPendiente(String nickReceptor, Trino trino) throws RemoteException {
		
		trinosPendientes.get(nickReceptor).add(trino);				
	}

	/**
	 * Itera por la lista de trinos pendientes de un usuario y si el nick del emisor
	 * del trino es el especificado, se borra el trino
	 */
	public void borrarTrinosPendientes(String nickEmisor, String nickReceptor) throws RemoteException {
				
		Iterator<Trino> it = getTrinosPendientes().get(nickReceptor).iterator();
		
		while(it.hasNext()) {
			if(it.next().GetNickPropietario().equals(nickEmisor)) {
				it.remove();
			}
		}	
	}

	/**
	 * Elimina todos los trinos pendientes asociados a un usuario
	 */
	public void limpiarBuffer(String nick) throws RemoteException {		

		trinosPendientes.get(nick).clear();		
	}
	
	/**
	 * Getter de HashMap usuariosRegistrados
	 */
	public HashMap<String, User> getUsuariosRegistrados() throws RemoteException {
		return usuariosRegistrados;
	}
	
	/**
	 * Getter de HashMap usuariosConectados
	 */
	public HashMap<String, User> getUsuariosConectados() throws RemoteException {
		return usuariosConectados;
	}
	
	/**
	 * Getter de HashMap usuariosBloqueados
	 */
	public HashMap<String, User> getUsuariosBloqueados() throws RemoteException {
		return usuariosBloqueados;
	}
	
	/**
	 * Getter de HashMap de contactos asociado a un usuario
	 */
	public HashMap<String, ArrayList<User>> getContactos() throws RemoteException {
		return contactos;
	}
	
	/**
	 * Getter de HashMap de seguidores asociado a un usuario
	 */
	public HashMap<String, ArrayList<User>> getSeguidores() throws RemoteException {
		return seguidores;
	}
	
	/**
	 * Getter de HashMap de trinos pendientes asociado a un usuario
	 */
	public HashMap<String, ArrayList<Trino>> getTrinosPendientes() throws RemoteException {
		return trinosPendientes;
	}
	
}
