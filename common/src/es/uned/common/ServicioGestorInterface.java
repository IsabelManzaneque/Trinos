package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


//contiene la interfaz remota del servicio Gestor que depende de la entidad Servidor
public interface ServicioGestorInterface extends Remote{	
	
	public boolean seguir(String miNick, String suNick) throws RemoteException; 
	
	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException; 	
	
}
