package es.uned.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import es.uned.common.IServidor;
import es.uned.common.Utils;


public class MainServidor {

	public static void main(String[] args) throws Exception {
		Utils.setCodeBase(IServidor.class);
		
		//Creamos un objeto servidor y lo exportamos en un puerto cualquiera, nos devuelve on objeto remoto
		Servidor servidor = new Servidor();
		//UnicastRemoteObject es el middleware. Remote sera el proxy que escucha en el puerto 8888
		//C:\Program Files\Java\jdk-18\bin>start rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false
		IServidor remote = (IServidor)UnicastRemoteObject.exportObject(servidor, 8888);
		
		//Ubicamos el registro. esa funcion nos devuelve un objeto registro que lo guardamos en registry
		Registry registry = LocateRegistry.getRegistry();
		//enviamos el objeto remoto al registro. Pepito es el nombre con el que los clientes van a pedir la 
		// instancia. Puede ser cualquier nombre, es el punto de entrada. Aqui empieza la comunicacion cliente-servidor
		registry.rebind("Pepito", remote);
		
		System.out.println("Servidor listo, presione enter para terminar");
		System.in.read();
		
		
		// una vez que se presiona enter, para el servidor
		registry.unbind("Pepito");
		UnicastRemoteObject.unexportObject(servidor, true);
		
		System.out.println("Servidor Terminado");
	}
}
