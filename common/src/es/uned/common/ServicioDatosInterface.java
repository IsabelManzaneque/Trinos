package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

// contiene la interfaz remota del servicio Datos que depende de la entidad Base de Datos
public interface ServicioDatosInterface extends Remote{
	
		
	public boolean agregarUsuario(String nick, User user) throws RemoteException;  
	
	public void borrarUsuario(String nick) throws RemoteException;
	
	public boolean agregarConectado(String nick, String password) throws RemoteException;
	
	public void borrarConectado(String nick) throws RemoteException;
	
	public boolean agregarContacto(String miNick, String suNick) throws RemoteException;
	
	public boolean borrarContacto(String miNick, String suNick) throws RemoteException;
	
	public void banearUsuario() throws RemoteException;
	
	public void agregarTrino(Trino trino) throws RemoteException;
	
	public void borrarTrino(Trino trino) throws RemoteException;
	
	public void limpiarBuffer(String name) throws RemoteException; 
	
	public HashMap<String, User> getUsuariosRegistrados() throws RemoteException;
	
	public HashMap<String, User> getUsuariosConectados() throws RemoteException;
	
	public HashMap<String, ArrayList<User>> getContactos() throws RemoteException;	
	


}
