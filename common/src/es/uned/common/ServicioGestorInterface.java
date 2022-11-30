package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


//contiene la interfaz remota del servicio Gestor que depende de la entidad Servidor
public interface ServicioGestorInterface extends Remote{	
	
	public boolean seguir(String miNick, String suNick) throws RemoteException; 
	
	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException; 
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException;
	
	public boolean enviarTrino(Trino trino) throws RemoteException;
	
	
	// COSAS CALLBACK
	
	// Este método remoto permite a un cliente de objeto registrarse para callback. @param objClienteCallback es 
	// una referencia al cliente de objeto; el servidor lo utiliza para realizar los callbacks
	public void registrarCallback(CallbackUsuarioInterface objCallbackCliente)  throws RemoteException;
	
	// Este método remoto permite a un cliente de objeto cancelar su registro para callback
	public void eliminarRegistroCallback(CallbackUsuarioInterface objCallbackCliente) throws RemoteException;
			
	
	public String decirHola() throws java.rmi.RemoteException;
	
}
