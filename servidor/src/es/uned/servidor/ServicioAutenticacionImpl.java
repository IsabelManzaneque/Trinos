package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.User;


/**
 * Clase que se encarga de registrar y de autenticar a los usuarios del sistema. 
 * El usuario accede y se desconecta del sistema utilizando este servicio.
 * Implementa la interfaz remota ServicioAutenticacionInterface. 
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */
public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{
		
		
	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}
	
	/**
	 * Envia la informacion de un nuevo usuario a la base de datos 
	 */
	public boolean registrar(String nick, User user) throws RemoteException {	
		
		return Servidor.getDatos().agregarUsuario(nick, user);		
	}	
	
	/**
	 * Verifica la informacion de un usuario con la guardada en la base de datos
	 */
	public boolean autenticar(String nick, String password, CallbackUsuarioInterface objCallback) throws RemoteException {
		
		return Servidor.getDatos().agregarConectado(nick, password, objCallback);	
	}
	
	/**
	 * Envia la informacion de un usuario a desconectar a la base de datos  
	 */
	public void desconectar(String nick) throws RemoteException {	
		
		Servidor.getDatos().borrarConectado(nick);			
	}			
				
}
