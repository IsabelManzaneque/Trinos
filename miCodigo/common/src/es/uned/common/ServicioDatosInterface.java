package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

// contiene la interfaz remota del servicio Datos que depende de la entidad Base de Datos
public interface ServicioDatosInterface extends Remote{
	
		
	public void agregarUsuario(String name, String password) throws RemoteException;  
	
	public void borrarUsuario() throws RemoteException;
	
	
	public void banearUsuario() throws RemoteException;
	
	public void agregarTrino(Trino trino) throws RemoteException;
	
	public void borrarTrino(Trino trino) throws RemoteException;
	
	public void limpiarBuffer(String name) throws RemoteException; 
	public HashMap<String, String> getUsuarios() throws RemoteException;
	public HashMap<String, String> getUsuariosConectados() throws RemoteException;
	
	public String decirHola(String nombre) throws RemoteException;
	
//	// Enviar mensaje. Cuerpo del mensaje, la sesion de quien lo envia y la sesion de a quien lo envia
//	public void enviar(String cuerpoMensaje, int sesionDe, int sesionA) throws RemoteException;  
//	
//	// El cliente pregunta al servidor si hay mensajes disponibles pasandole su sesio
//	// si los hay, recibe una lista de mensajes. Esta clase mensaje puede ser la clase TRINO???
//	public void recibir(int sesion) throws RemoteException;  
//	
	// limpia buffer de mensajes
	

}
