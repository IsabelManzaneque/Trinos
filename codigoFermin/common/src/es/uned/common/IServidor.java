package es.uned.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//Remote envia la referencia al objeto. Cuando el objeto es demasiado grande o 
//queremos afectar el estado del objeto original
public interface IServidor extends Remote{
	
	// Recibimos su nombre y devolvemos una sesion
	// tambien tendria que recibir la contrasena como param	(fermin no lo hace por facilitar explicacion)
	public int autenticar(String nombre) throws RemoteException;  
	
	// Agregar contacto. El cliente con sesion tal tiene al cliente con nombre cual agregado. Devuelve la sesion del otro cliente
	public int agregar(String nombre, int sesion) throws RemoteException;  
	
	// Enviar mensaje. Cuerpo del mensaje, la sesion de quien lo envia y la sesion de a quien lo envia
	public void enviar(String cuerpoMensaje, int sesionDe, int sesionA) throws RemoteException;  
	
	// El cliente pregunta al servidor si hay mensajes disponibles pasandole su sesio
	// si los hay, recibe una lista de mensajes. Esta clase mensaje puede ser la clase TRINO???
	public List<Mensaje> recibir(int sesion) throws RemoteException;  
	
	// limpia buffer de mensajes
	public void limpiarBuffer(int sesion) throws RemoteException; 

}
