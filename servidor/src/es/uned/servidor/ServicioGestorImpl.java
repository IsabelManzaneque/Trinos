package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;
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

	}

	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().borrarContacto(miNick, suNick);	

	}
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException {
		
		return Servidor.getDatos().getUsuariosRegistrados();

	}
	
	public void bloquearUsuario() throws RemoteException{
		
		Scanner scanner = new Scanner(System.in); 			
		
		System.out.print("Introduzca el nick del usuario a bloquear: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    
	    if(Servidor.getDatos().agregarBloqueado(nick)) {
	    	System.out.print("Se ha bloqueado el usuario");
	    }else {
	    	System.out.print("No se encuentra el usuario");
	    }		
	}
	
	public void desbloquearUsuario() throws RemoteException{
		
		Scanner scanner = new Scanner(System.in); 			
		
		System.out.print("Introduzca el nick del usuario a desbloquear: ");
	    String nick = scanner.nextLine().trim().toLowerCase();
	    
	    if(Servidor.getDatos().borrarBloqueado(nick)) {
	    	System.out.print("Se ha desbloqueado el usuario");
	    }else {
	    	System.out.print("No se encuentra el usuario");
	    }	
	}
	

	
    public void limpiarBuffer(String nick) throws RemoteException {
    	Servidor.getDatos().limpiarBuffer(nick);
    }
    
    /* Itera por la lista de seguidores de un usuario y si los seguidores no están conectados
     * llama a la funcion borrarTrinosPendientes de la base de datos */
    public void borrarPendientes(String nick) throws RemoteException {
    	
    	ArrayList<User>seguidores = Servidor.getDatos().getSeguidores().get(nick);
    	
    	for(User u : seguidores) {
    		
    		if(!Servidor.getDatos().getUsuariosConectados().containsKey(u.getNick())) {      			
    			Servidor.getDatos().borrarTrinosPendientes(nick, u.getNick()); 
    		}
    	}
    }
	
	public boolean enviarTrino(Trino trino) throws RemoteException{
		
		hacerCallbacks(trino);
		Servidor.getDatos().agregarTrino(trino);
		
		if(Servidor.getDatos().getUsuariosBloqueados().containsKey(trino.GetNickPropietario())) {
			return false;
		}		
		return true;
	}	
	
	
	private synchronized void hacerCallbacks(Trino trino) throws RemoteException {
		
		
		ArrayList<User>seguidores = Servidor.getDatos().getSeguidores().get(trino.GetNickPropietario());
		
		for(User u : seguidores) {
			
			if(Servidor.getDatos().getUsuariosConectados().containsKey(u.getNick()) && 
			  !Servidor.getDatos().getUsuariosBloqueados().containsKey(u.getNick()) &&
			  !Servidor.getDatos().getUsuariosBloqueados().containsKey(trino.GetNickPropietario())){			
				CallbackUsuarioInterface seguidor =  u.getObjCallback();
				seguidor.notificame(trino);
			}else {
				Servidor.getDatos().agregarTrinoPendiente(u.getNick(), trino);
			}
		}	
	 }
	
	
	
}
