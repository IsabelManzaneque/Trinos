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


/**
 * Clase que se encarga de gestionar todas las operaciones de los usuarios en 
 * relacion con enviar  trinos, bloquear y hacerse seguidor de otros usuarios.
 * Implementa la interfaz remota ServicioGestorInterface. 
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */
public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{		
	

	protected ServicioGestorImpl() throws RemoteException {
		super();		
	}
	
	/**
	 * Permite que follower comience a seguir a followed
	 */
	public boolean seguir(String follower, String followed) throws RemoteException {
		
		return Servidor.getDatos().agregarContacto(follower, followed);		

	}	
	
	/**
	 * Permite que follower deje de seguir a followed
	 */
	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().borrarContacto(miNick, suNick);	
	}
	
	/**
	 * Devuelve los usuarios registrados de la base de datos
	 */
	public HashMap<String, User> mostrarUsuarios() throws RemoteException {
		
		return Servidor.getDatos().getUsuariosRegistrados();
	}
	
	
	/**
	 * Bloquea un usuario registrado 
	 */
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
	
	/**
	 * Desbloquea un usuario bloqueado 
	 */
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
	

	/**
	 * Invoca la funcion limpiarBuffer de la base de datos para eliminar
	 * todos los trinos pendientes de usuario 
	 */
    public void limpiarBuffer(String nick) throws RemoteException {
    	Servidor.getDatos().limpiarBuffer(nick);
    }
    
    /**
	 * Itera por los seguidores de un usuario y si no están conectados
     * llama a la funcion borrarTrinosPendientes de la base de datos	 
	 */
    public void borrarPendientes(String nick) throws RemoteException {
    	
    	ArrayList<User>seguidores = Servidor.getDatos().getSeguidores().get(nick);
    	
    	for(User u : seguidores) {
    		
    		if(!Servidor.getDatos().getUsuariosConectados().containsKey(u.getNick())) {      			
    			Servidor.getDatos().borrarTrinosPendientes(nick, u.getNick()); 
    		}
    	}
    }
	
    /**
	 * Envia los trinos de los usuarios a la base de datos y a los
	 * usuarios seguidores del emisor
	 */
	public boolean enviarTrino(Trino trino) throws RemoteException{
		
		hacerCallbacks(trino);
		Servidor.getDatos().agregarTrino(trino);
		
		if(Servidor.getDatos().getUsuariosBloqueados().containsKey(trino.GetNickPropietario())) {
			return false;
		}		
		return true;
	}	
	
	/**
	 * Realiza callsback para notificar a los usuarios de los trinos
	 * que envian sus contactos. 
	 */
	private synchronized void hacerCallbacks(Trino trino) throws RemoteException {		
		
		ArrayList<User>seguidores = Servidor.getDatos().getSeguidores().get(trino.GetNickPropietario());
		
		for(User u : seguidores) {
			
			if(Servidor.getDatos().getUsuariosConectados().containsKey(u.getNick()) && 
			  !Servidor.getDatos().getUsuariosBloqueados().containsKey(u.getNick()) &&
			  !Servidor.getDatos().getUsuariosBloqueados().containsKey(trino.GetNickPropietario())){			
				CallbackUsuarioInterface seguidor =  u.getObjCallback();
				seguidor.notificame(trino);
			}else {
				// si el emisor esta bloqueado o el seguidor esta offline 
				// o bloqueado, el trino se guarda en la base de datos
				Servidor.getDatos().agregarTrinoPendiente(u.getNick(), trino);
			}
		}	
	 }
	
}
