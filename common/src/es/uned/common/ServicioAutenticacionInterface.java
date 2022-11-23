package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
// contiene la interfaz remota del servicio de autenticación que depende de la entidad Servidor
public interface ServicioAutenticacionInterface extends Remote{
	
	public boolean autenticar(String nick, String password) throws RemoteException; 
	
	public boolean registrar(String nick, User user) throws RemoteException; 
	
	public void desconectar(String nick) throws RemoteException; 
	

}
