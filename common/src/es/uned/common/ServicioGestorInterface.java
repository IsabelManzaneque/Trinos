package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz remota del servicio Gestor que depende de la entidad Servidor 
 */
public interface ServicioGestorInterface extends Remote{	
	
	/**
	 * Permite que follower comience a seguir a followed
     * @param follower Nick del usuario que va a seguir
	 * @param followed Nick del usuario a seguir
	 * @return true si la operacion se ha realizado con exito
	 */
	public boolean seguir(String follower, String followed) throws RemoteException; 
	
	/**
	 * Permite que follower deje de seguir a followed
     * @param follower Nick del usuario que va a dejar de seguir a otro
	 * @param followed Nick del usuario al que se va a dejar de seguir
	 * @return true si la operacion se ha realizado con exito
	 */
	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException; 
	
	/**
	 * Devuelve los usuarios registrados de la base de datos
	 * @return Map con los usuarios registradis en el sistema
	 */
	public HashMap<String, User> mostrarUsuarios() throws RemoteException;
	
	/**
	 * Envia los trinos de los usuarios a la base de datos y a los
	 * usuarios seguidores del emisor
     * @param trino Mensaje de un usuario 
	 * @return true si la operacion se ha realizado con exito
	 */
	public boolean enviarTrino(Trino trino) throws RemoteException;
	
	/**
	 * Invoca la funcion limpiarBuffer de la base de datos para eliminar
	 * todos los trinos pendientes de usuario 
     * @param nick Nick del usuario del que se quieren borrar los trinos pendientes 
	 */
	public void limpiarBuffer(String nick) throws RemoteException;
	
	/**
	 * Itera por los seguidores de un usuario y si no están conectados
     * llama a la funcion borrarTrinosPendientes de la base de datos	 
     * @param  nick Nick del usuario del que se quieren borrar los trinos pendientes 
	 */
	public void borrarPendientes(String nick) throws RemoteException;
	
	/**
	 * Bloquea un usuario registrado 
	 */
	public void bloquearUsuario() throws RemoteException;
	
	/**
	 * Desbloquea un usuario bloqueado 
	 */
	public void desbloquearUsuario() throws RemoteException;
	
		
}
