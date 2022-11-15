package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
// contiene la interfaz remota del servicio de autenticación que depende de la entidad Servidor
public interface ServicioAutenticacionInterface extends Remote{
	
	public int autenticar(String nombre) throws RemoteException; 
	
	public String decirHola(String nombre) throws RemoteException;

}
