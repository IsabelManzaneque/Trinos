package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


//contiene la interfaz remota del servicio Gestor que depende de la entidad Servidor
public interface ServicioGestorInterface extends Remote{	
	
	public boolean seguir(String miNick, String suNick) throws RemoteException; 
	
	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException; 
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException;
	
	//public HashMap<String, ArrayList<Trino>> trinosPendientes() throws RemoteException;
	
	public boolean enviarTrino(Trino trino) throws RemoteException;
	
	public void limpiarBuffer(String nick) throws RemoteException;
	
	public void borrarPendientes(String nick) throws RemoteException;
	
		
}
