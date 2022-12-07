package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interfaz remota del servicio de autenticación que depende de la entidad Servidor 
 */
public interface ServicioAutenticacionInterface extends Remote{
	
	/**
	 * Verifica la informacion de un usuario con la guardada en la base de datos
     * @param nick Nick del usuario a autenticar
	 * @param password Contrasena del usuario a autenticar
	 * @param objCallback objeto callback que se asigna al usuario
	 * @return true si el usuario esta en la base de datos
	 */
	public boolean autenticar(String nick, String password, CallbackUsuarioInterface objCallback) throws RemoteException; 
	
	/**
	 * Envia la informacion de un nuevo usuario a la base de datos 
     * @param nick Nick del usuario a registrar
	 * @param user Objeto de tipo User con toda la informacion del usuario
	 * @return true si la operacion se ha realizado con exito
	 */
	public boolean registrar(String nick, User user) throws RemoteException; 
	
	/**
	 * Envia la informacion de un usuario a desconectar a la base de datos  
     * @param nick Nick del usuario a desconectar
	 */
	public void desconectar(String nick) throws RemoteException; 
	

}
