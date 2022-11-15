package es.uned.servidor;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.uned.common.IServidor;
import es.uned.common.Mensaje;


public class Servidor implements IServidor {
    
	//Estos hashmaps serian la base de datos
	private Map<Integer, String> sesion_nombre = new HashMap<Integer, String>();
	private Map<String, Integer> nombre_sesion = new HashMap<String, Integer>();
	//lista con las sesiones de los usuario conectados
	private Map<Integer, List<Integer>> contactos = new HashMap<Integer, List<Integer>>(); 
	// mapa con la sesion del usuario pendiente de recibir mensajes y una lista de los mensajes pendientes
	private Map<Integer, List<Mensaje>> buffer = new HashMap<Integer, List<Mensaje>>();
	
	
	//Cuando se autentica un nombre, devuelve una sesion de usuario
	@Override
	public int autenticar(String nombre) {
		System.out.println(nombre + " esta intentando autenticarse");
		
		int sesionUsuario = getSesion();
		
		// dos mapas inversos porque a veces necesitamos utilizar
		// si tenemos el id y queremos el nombre usamos el primero
		// si tenemos el nombre y queremos obtener el id, el segundo
		sesion_nombre.put(sesionUsuario, nombre);
		nombre_sesion.put(nombre, sesionUsuario);
		
		return sesionUsuario;
	}
	
	//cuando agregamos un nombre a una sesion
	@Override
	public int agregar(String nombre, int sesion) {
		//nos fijamos que el mapa contenga esa sesion. si no existe, excepcion
		if (!sesion_nombre.containsKey(sesion)) {
			throw new RuntimeException("Sesion invalida");
		}
		
		//si el mapa no contiene el nombre, esa persona no esta conectada
		if (!nombre_sesion.containsKey(nombre)) {
			throw new RuntimeException(nombre + " no esta conectado");
		}
		
		// misContactos sera la lista de contactos de la sesion parametro
		List<Integer> misContactos = contactos.get(sesion);
		//si la lista no existe, la creamos y la ponemos en el mapa 
		if (misContactos == null) {
			misContactos = new LinkedList<Integer>();
			contactos.put(sesion, misContactos);
		}		
		//si existe, agregamos a misContactos la sesion de el nombre parametro
		misContactos.add(nombre_sesion.get(nombre));
		
		System.out.println(sesion + " agrego al contacto " + nombre);
		
		return nombre_sesion.get(nombre);
	}
	
	// Envia un mensaje de una sesion a otra
	@Override
	public void enviar(String mensage, int sesionDe, int sesionA) {
		
		//si la sesion del que envia no esta en el mapa, es invalids
		if (!sesion_nombre.containsKey(sesionDe)) {
			throw new RuntimeException("Sesion invalida");
		}	
		
		//si la sesion del que recibe no esta en el mapa, la persona no esta conectada
		if (!sesion_nombre.containsKey(sesionA)) {
			throw new RuntimeException("Contacto no esta conectado");
		}
		
		// vemos si la sesion del remitente contiene en sus contactos a la del receptor
		// si no existe, el receptor no es parte de los contactos del emisor
		if (!contactos.get(sesionDe).contains(sesionA)) {
			throw new RuntimeException(sesion_nombre.get(sesionA) + 
									   " No es parte de sus contactos");
		}
		
		// dame los mensajes pendientes para el receptor
		List<Mensaje> mensajes = buffer.get(sesionA);
		
		// si esta vacio la creamos y la ponemos en el mapa
		if (mensajes == null) {
			mensajes = new LinkedList<Mensaje>();
			buffer.put(sesionA, mensajes);
		}
		
		// si no, agregamos un nuevo mensaje  con el cuerpo del mensaje y la sesion del emisor
		// los mensajes pendientes se van acumulando
		mensajes.add(new Mensaje(mensage, sesion_nombre.get(sesionDe)));
		
		System.out.println(sesion_nombre.get(sesionDe) + " envio un mensaje a " + 
							sesion_nombre.get(sesionA));
	}
	
	// recibe mensajes. Pasa una sesion y si no existe excepcion
	@Override
	public List<Mensaje> recibir(int sesion) {
		if (!sesion_nombre.containsKey(sesion)) {
			throw new RuntimeException("Sesion invalida");
		}
		// si existe, buffer devuelve la lista de mensajes
		return buffer.get(sesion);
	}
	
	@Override
	public void limpiarBuffer(int sesion) throws RemoteException {
		
		// comprueba que la sesion es valida
		if (!sesion_nombre.containsKey(sesion)) {
			throw new RuntimeException("Sesion invalida");
		}
		// si lo es, borra todos los elementos de la lista
		buffer.get(sesion).clear();
	}
	
	
	private static int sesion = new Random().nextInt();
	
	// le pedimos una sesion y devuelve un entero
	public static int getSesion() {
		return ++sesion;
	}

}
