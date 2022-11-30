package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import es.uned.common.ServicioGestorInterface;
import es.uned.common.User;

// clase que implementa la interfaz remota ServicioGestorInterface
// Servicio Gestor: Este servicio se encarga de gestionar todas las operaciones de los usuarios en relación 
// con enviar trinos, bloquear y hacerse seguidor de otros  usuarios. Cuando un usuario se hace seguidor de otro 
// usuario, el primero recibe automáticamente los trinos del segundo cuando los publica. 

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{	


	protected ServicioGestorImpl() throws RemoteException {
		super();		
	}

	public boolean seguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().agregarContacto(miNick, suNick);		
		//return Servidor.followUsuario(miNick, suNick);
	}

	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().borrarContacto(miNick, suNick);	
		//return Servidor.unFollowUsuario(miNick, suNick);
	}
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException {
		
		return Servidor.getDatos().getUsuariosRegistrados();
		//return Servidor.enviarRegistrados();
	}
	

}
