package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


//contiene la interfaz remota del servicio Gestor que depende de la entidad Servidor
public interface ServicioGestorInterface extends Remote{
	
	
	// Agregar contacto. El cliente con sesion tal tiene al cliente con nombre cual agregado. Devuelve la sesion del otro cliente
//	public void agregar(String nombre, int sesion) throws RemoteException;  
//	
//	// Enviar mensaje. Cuerpo del mensaje, la sesion de quien lo envia y la sesion de a quien lo envia
//	public void enviar(String cuerpoMensaje, int sesionDe, int sesionA) throws RemoteException;  
//	
//	// El cliente pregunta al servidor si hay mensajes disponibles pasandole su sesio
//	// si los hay, recibe una lista de mensajes. Esta clase mensaje puede ser la clase TRINO???
//	public void recibir(int sesion) throws RemoteException;  
//	
//	// limpia buffer de mensajes
//	public void limpiarBuffer(int sesion) throws RemoteException; 
	
	public String decirHola(String nombre) throws RemoteException;
	

}
