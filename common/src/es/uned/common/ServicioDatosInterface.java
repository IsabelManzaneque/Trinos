package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Interfaz remota del servicio de datos que depende de la entidad Basededatos 
 * 
 * @author Isabel Manzaneque, imanzaneq3@alumno.uned.es
 */
public interface ServicioDatosInterface extends Remote{
	
	/**
	 * Anade un usuario al HashMap usuariosRegistrados. Asocia al nuevo usuario
	 * estructuras para guardar sus trinos pendientes, seguidores y contactos
     * @param nick Nick del nuevo usuario
	 * @param user Objeto User con toda la información de usuario
	 * @return true Si el nick no esta ya asociado a un usuario
	 */
	public boolean agregarUsuario(String nick, User user) throws RemoteException;  
	
	/**
	 * Borra un usuario del HashMap usuariosRegistrados. Borra las estructuras
	 * asociadas para guardar sus trinos pendientes, seguidores y contactos
     * @param nick Nick del usuario a borrar
	 */
	public void borrarUsuario(String nick) throws RemoteException;
	
	/**
	 * Anade un usuario al HashMap usuariosConectados. Comprueba si el usuario
	 * tiene trinos pendientes y si se cumple con las condiciones, los muestra.
	 * Se asocia al usuario un objeto que implementa CallbackUsuarioInterface
     * @param nick Nick del usuario
	 * @param password Contrasena del usuario 
	 * @param objCallback Objeto callback asociado al usuario
	 * @return true Si el usuario no esta ya conectado y el nick y contrasena son correctos
	 */
	public boolean agregarConectado(String nick, String password, CallbackUsuarioInterface objCallback) throws RemoteException;
	
	/**
	 * Borra un usuario del HashMap usuariosConectados. Al hacerlo, evalua 
	 * su Objeto CallbackUsuarioInterface a null.
     * @param nick Nick del usuario a borrar
	 */
	public void borrarConectado(String nick) throws RemoteException;
	
	/**
	 * Anade un usuario al HashMap de contactos de otro usuario. 
     * @param nickFollower Nick del usuario que agrega a otro
	 * @param nickFollowed Nick del usuario que es agregado por otro 
	 * @return true Si el usuario es agregado correctamente
	 */
	public boolean agregarContacto(String nickFollower, String nickFollowed) throws RemoteException;
	
	/**
	 * Borra un usuario del HashMap de contactos de otro usuario. 
     * @param nickFollower Nick del usuario que borra a otro
	 * @param nickFollowed Nick del usuario que es borrado por otro	
	 * @return true Si el usuario es borrado correctamente
	 */
	public boolean borrarContacto(String nickFollower, String nickFollowed) throws RemoteException;
	
	/**
	 * Anade un usuario al HashMap de usuarios bloqueados. 
     * @param nick Nick del usuario a bloquear
	 * @return true Si el usuario es bloquedo correctamente
	 */
	public boolean agregarBloqueado(String nick) throws RemoteException;
	
	
	/**
	 * Elimina un usuario al HashMap de usuarios bloqueados. Si tenia trinos
	 * pendientes de usuarios, los recibe. Si otros usuarios tenian trinos
	 * pendientes del usuario desbloqueado, los reciben.
     * @param nick Nick del usuario a desbloquear
	 * @return true Si el usuario es desbloquedo correctamente
	 */
	public boolean borrarBloqueado(String nick) throws RemoteException;
	
	/**
	 * Muestra los trinos guardados en el ArrayList de trinos si los hay. Si no
	 * los hay, se informa al usuario
	 */
	public void mostrarTrinos() throws RemoteException;
	
	/**
	 * Anade un nuevo trino al ArrayList de trinos
	 * @param trino Nuevo trino a anadir
	 */
	public void agregarTrino(Trino trino) throws RemoteException;
	
	/**
	 * Anade un nuevo trino al HashMap de trinos pendientes vinculado a
	 * un usuario 
	 * @param nickReceptor Nick del usuario que recibe el trino pendiente
	 * @param trino Nuevo trino a anadir
	 */
	public void agregarTrinoPendiente(String nickReceptor, Trino trino) throws RemoteException;
	
	/**
	 * Itera por la lista de trinos pendientes de un usuario y si el nick del emisor
	 * del trino es el especificado, se borra el trino
	 * @param nickEmisor nick del usuario cuyo trino se borra
	 * @param nickReceptor Nick del usuario con trinos pendientes
	 */
	public void borrarTrinosPendientes(String nickEmisor, String nickReceptor) throws RemoteException;
	
	/**
	 * Elimina todos los trinos pendientes asociados a un usuario
	 * @param nick Nick del usuario cuyos trinos se borran
	 */
	public void limpiarBuffer(String nick) throws RemoteException; 
	
	/**
	 * Getter de HashMap usuariosRegistrados
	 * @return usuariosRegistrados
	 */
	public HashMap<String, User> getUsuariosRegistrados() throws RemoteException;
	
	/**
	 * Getter de HashMap usuariosConectados
	 * @return usuariosConectados
	 */
	public HashMap<String, User> getUsuariosConectados() throws RemoteException;
	
	/**
	 * Getter de HashMap usuariosBloqueados
	 * @return usuariosBloqueados
	 */
	public HashMap<String, User> getUsuariosBloqueados() throws RemoteException;
	
	/**
	 * Getter de HashMap de contactos asociado a un usuario
	 * @return contactos
	 */
	public HashMap<String, ArrayList<User>> getContactos() throws RemoteException;	
	
	/**
	 * Getter de HashMap de seguidores asociado a un usuario
	 * @return seguidores
	 */
	public HashMap<String, ArrayList<User>> getSeguidores() throws RemoteException;	
	
	/**
	 * Getter de HashMap de trinos pendientes asociado a un usuario
	 * @return trinosPendientes
	 */
	public HashMap<String, ArrayList<Trino>> getTrinosPendientes() throws RemoteException;	
	


}
