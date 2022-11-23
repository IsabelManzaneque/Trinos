package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import es.uned.common.ServicioAutenticacionInterface;

//  clase que implementa la interfaz remota ServicioAutenticacionInterface
// Se encarga de registrar y de autenticar a los usuarios del sistema.  Una vez registrado, el usuario puede acceder al sistema haciendo login utilizando 
// este servicio de Autenticación.

public class ServicioAutenticacionImpl extends UnicastRemoteObject implements ServicioAutenticacionInterface{
	
		
	protected ServicioAutenticacionImpl() throws RemoteException {
		super();
	}
	
	
	// La operación de registro consiste en que los usuarios introducen en el sistema por primera vez sus datos personales
		// (nombre, nick (apodo) y password), siendo el nick el identificador único de usuario. Es decir, no pueden existir dos 
		// usuarios en el sistema con el mismo nick.
		public boolean registrar(String nick, String password) throws RemoteException {	
			
			return Servidor.addNuevo(nick, password);			
		}
		
		//Cuando se autentica un nombre, devuelve una sesion de usuario
		@Override
		public boolean autenticar(String nick, String password) throws RemoteException {
			
			return Servidor.addConectado(nick, password);
		}
		
		public void desconectar(String nick) throws RemoteException {	
			
			Servidor.deleteConectado(nick);		
		}
			
				
			
			
			
			// dos mapas inversos porque a veces necesitamos utilizar
			// si tenemos el id y queremos el nombre usamos el primero
			// si tenemos el nombre y queremos obtener el id, el segundo
			//sesion_nombre.put(sesionUsuario, nombre);
			//nombre_sesion.put(nombre, sesionUsuario);
			
		
		
		private static int sesion = new Random().nextInt();
		
		// le pedimos una sesion y devuelve un entero
		public static int getSesion() {
			return ++sesion;
		}
	
	
	
	@Override
	public String decirHola(String nombre) throws RemoteException {
		// TODO Auto-generated method stub
		return "Soy el metodo de ServicioAutenticacionImpl " + nombre;
	}

	

}
