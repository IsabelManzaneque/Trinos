package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.uned.common.ServicioAutenticacionInterface;
import es.uned.common.User;

//  clase que implementa la interfaz remota ServicioAutenticacionInterface
// Se encarga de registrar y de autenticar a los usuarios del sistema.  Una vez registrado, el usuario puede acceder al sistema haciendo login utilizando 
// este servicio de Autenticación.

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{
		
		
	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}
	

	public boolean registrar(String nick, User user) throws RemoteException {	
		
		return Servidor.getDatos().agregarUsuario(nick, user);
		
	}	

	public boolean autenticar(String nick, String password) throws RemoteException {
		
		return Servidor.getDatos().agregarConectado(nick, password);
	
	}
	
	public void desconectar(String nick) throws RemoteException {	
		
		Servidor.getDatos().borrarConectado(nick);
			
	}
			
				
}
