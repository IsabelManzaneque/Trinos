package es.uned.cliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.Trino;

/**
 * Clase que implementa la interfaz remota CallbackUsuarioInterface. Se arranca 
 * desde el cliente y tiene un único método que se encarga de hacerle llegar los trinos 
 * que publican los usuarios a los que sigue el usuario logueado de forma automática. 
 */

public class CallbackUsuarioImpl extends UnicastRemoteObject implements CallbackUsuarioInterface{
	
	
	public CallbackUsuarioImpl() throws RemoteException {
		super();
	}
	
	/**
	 * Metodo que se encarga de hacerle llegar a los trinos que publican los 
	 * usuarios a los que sigue el usuario logueado de forma automática. 
	 */
	public String notificame(Trino trino) {
		
		String mensajeRet = "\n> " + trino.GetNickPropietario()+"#  " + trino.GetTrino();
		System.out.println(mensajeRet);
		return mensajeRet;
	}

}
