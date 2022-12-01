package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;
import es.uned.common.User;

// clase que implementa la interfaz remota ServicioGestorInterface
// Servicio Gestor: Este servicio se encarga de gestionar todas las operaciones de los usuarios en relaci�n 
// con enviar trinos, bloquear y hacerse seguidor de otros  usuarios. Cuando un usuario se hace seguidor de otro 
// usuario, el primero recibe autom�ticamente los trinos del segundo cuando los publica. 

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{	
	
	private Vector<CallbackUsuarioInterface> listaClientes = new Vector<>();

	protected ServicioGestorImpl() throws RemoteException {
		super();		
	}
	

	public boolean seguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().agregarContacto(miNick, suNick);		

	}

	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().borrarContacto(miNick, suNick);	

	}
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException {
		
		return Servidor.getDatos().getUsuariosRegistrados();

	}
	
	public HashMap<String, ArrayList<Trino>> trinosPendientes() throws RemoteException {
		return Servidor.getDatos().getTrinosPendientes();
	}
	
    public void borrarPendientes(String nick) throws RemoteException {
    	Servidor.getDatos().limpiarBuffer(nick);
    }
	
	public boolean enviarTrino(Trino trino) throws RemoteException{
		
		hacerCallbacks(trino);
		return Servidor.getDatos().agregarTrino(trino);
	}	
	
	
	private synchronized void hacerCallbacks(Trino trino) throws RemoteException {
		
		
		ArrayList<User>seguidores = Servidor.getDatos().getSeguidores().get(trino.GetNickPropietario());
		
		for(User u : seguidores) {
			
			if(Servidor.getDatos().getUsuariosConectados().containsKey(u.getNick())) {			
				CallbackUsuarioInterface proxCliente =  u.getObjCallback();
				proxCliente.notificame(trino);
			}else {
				Servidor.getDatos().agregarTrinoPendiente(u.getNick(), trino);
			}
		}	
	 }
	
	
	
}
