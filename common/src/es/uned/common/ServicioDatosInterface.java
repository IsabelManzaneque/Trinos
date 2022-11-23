package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

// contiene la interfaz remota del servicio Datos que depende de la entidad Base de Datos
public interface ServicioDatosInterface extends Remote{
	
		
	public boolean agregarUsuario(String nick, String password) throws RemoteException;  
	
	public void borrarUsuario(String nick) throws RemoteException;
	
	public boolean agregarConectado(String nick, String password) throws RemoteException;
	
	public void borrarConectado(String nick) throws RemoteException;
	
	public boolean agregarContacto(String miNick, String suNick) throws RemoteException;
	
	public boolean borrarContacto(String miNick, String suNick) throws RemoteException;
	
	public void banearUsuario() throws RemoteException;
	
	public void agregarTrino(Trino trino) throws RemoteException;
	
	public void borrarTrino(Trino trino) throws RemoteException;
	
	public void limpiarBuffer(String name) throws RemoteException; 
	
	public HashMap<String, String> getUsuariosRegistrados() throws RemoteException;
	
	public HashMap<String, String> getUsuariosConectados() throws RemoteException;
	
	
	
//	// Enviar mensaje. Cuerpo del mensaje, la sesion de quien lo envia y la sesion de a quien lo envia
//	public void enviar(String cuerpoMensaje, int sesionDe, int sesionA) throws RemoteException;  
//	
//	// El cliente pregunta al servidor si hay mensajes disponibles pasandole su sesio
//	// si los hay, recibe una lista de mensajes. Esta clase mensaje puede ser la clase TRINO???
//	public void recibir(int sesion) throws RemoteException;  
//	
	// limpia buffer de mensajes
	

}
