package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.ServicioGestorInterface;

// clase que implementa la interfaz remota ServicioGestorInterface
// Servicio Gestor: Este servicio se encarga de gestionar todas las operaciones de los usuarios en relación 
// con enviar trinos, bloquear y hacerse seguidor de otros  usuarios. Cuando un usuario se hace seguidor de otro 
// usuario, el primero recibe automáticamente los trinos del segundo cuando los publica. 

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{	


	protected ServicioGestorImpl() throws RemoteException {
		super();		
	}

	@Override
	public String decirHola(String nombre) throws RemoteException {
		// TODO Auto-generated method stub
		return "Soy el metodo de ServicioGestorImpl " + nombre;
	}

}
