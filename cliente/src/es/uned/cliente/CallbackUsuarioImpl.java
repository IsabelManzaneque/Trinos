package es.uned.cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackUsuarioInterface;

// clase que implementa la interfaz remota CallbackUsuarioInterface
// CallbackUsuario. Se arranca desde el cliente y tiene un único método que se encarga de hacerle llegar los trinos que 
// publican los usuarios a los que sigue el usuario logueado de forma automática. 

public class CallbackUsuarioImpl extends UnicastRemoteObject implements CallbackUsuarioInterface{
	
	
	public CallbackUsuarioImpl() throws RemoteException {
		super();
	}
	
	public String notificame(String mensaje) {
		
		String mensajeRet = "Callback recibido: " + mensaje;
		System.out.println(mensajeRet);
		return mensajeRet;
	}

}
