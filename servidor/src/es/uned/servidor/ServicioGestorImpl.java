package es.uned.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Vector;

import es.uned.common.CallbackUsuarioInterface;
import es.uned.common.ServicioGestorInterface;
import es.uned.common.Trino;
import es.uned.common.User;

// clase que implementa la interfaz remota ServicioGestorInterface
// Servicio Gestor: Este servicio se encarga de gestionar todas las operaciones de los usuarios en relación 
// con enviar trinos, bloquear y hacerse seguidor de otros  usuarios. Cuando un usuario se hace seguidor de otro 
// usuario, el primero recibe automáticamente los trinos del segundo cuando los publica. 

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface{	


	protected ServicioGestorImpl() throws RemoteException {
		super();		
	}

	public boolean seguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().agregarContacto(miNick, suNick);		

	}

	public boolean dejarDeSeguir(String miNick, String suNick) throws RemoteException {
		
		return Servidor.getDatos().borrarContacto(miNick, suNick);	

	}
	
	public HashMap<String, User> mostrarUsuarios() throws RemoteException {
		
		return Servidor.getDatos().getUsuariosRegistrados();

	}
	
	public boolean enviarTrino(Trino trino) throws RemoteException{
		
		return Servidor.getDatos().agregarTrino(trino);
	}
	
	
	// COSAS CALLBACK
	
	private Vector listaClientes = new Vector();
	
	public String decirHola()throws java.rmi.RemoteException {
		return("Hola Mundo");
	}
	
	public void registrarCallback(CallbackUsuarioInterface objCallbackCliente)throws RemoteException {
		 // almacena el objeto callback en el vector
		 if (!(listaClientes.contains(objCallbackCliente))) {
			 listaClientes.addElement(objCallbackCliente);
			 System.out.println("Nuevo cliente registrado ");
			 hacerCallbacks();

		 }
	}
	
	// Este método remoto permite a un cliente de objeto cancelar su registro para callback
	// @param id es un identificador para el cliente; el servidor lo utiliza únicamente para identificar al cliente	registrado.
	
	public synchronized void eliminarRegistroCallback(CallbackUsuarioInterface objCallbackCliente)throws RemoteException{
		
		if (listaClientes.removeElement(objCallbackCliente)){
			System.out.println("Cliente no registrado ");
		 } else {
			 System.out.println("eliminarRegistro: el cliente no fue registrado.");
					
		}		
	}
	
	 private synchronized void hacerCallbacks() throws RemoteException {
		 // realizar callback de un cliente registrado
		 System.out.println("\n**************************************\n" +"Callback iniciado –- ");
		 
		 for (int i=0; i<listaClientes.size(); i++) {
			 System.out.println("haciendo callback número"+ i +"\n");
			 // convertir el objeto vector a un objeto callback
			 CallbackUsuarioInterface proxCliente = (CallbackUsuarioInterface) listaClientes.elementAt(i);
			 // invocar el método de callback
			 proxCliente.notificame("Número de clientes registrados=" + listaClientes.size());
		 }
		 
		 System.out.println("**************************************\n" + "Servidor completo callbacks –-");
	 }
	
	
	
}
