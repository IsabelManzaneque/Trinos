package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interfaz remota que permite al usuario recibir automaticamente
 * los trinos de aquellos usuarios a los que sigue
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */
public interface CallbackUsuarioInterface extends Remote {
	
	/**
	 * Metodo que se encarga de hacerle llegar a los trinos que publican los 
	 * usuarios a los que sigue el usuario logueado de forma automática. 
     * @param trino Trino que se va a notificar al usuario
	 * @return mensaje String que reciben los seguidores del usuario
	 */
	public String notificame(Trino trino) throws RemoteException;

}
