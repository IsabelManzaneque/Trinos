package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

//contiene la interfaz remota permitiendo recibir al usuario los trinos de aquellos usuarios a los que sigue
public interface CallbackUsuarioInterface extends Remote {
	
	public String notificame(String mensaje) throws RemoteException;

}
