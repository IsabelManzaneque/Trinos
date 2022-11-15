package es.uned.basededatos;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uned.common.ServicioDatosInterface;
import es.uned.common.Trino;

// Clase que contiene el main de la entidad Base de Datos.
//Base de datos: Esta entidad es la encargada de almacenar todos los datos del sistema:
//Usuarios, Seguidores, Trinos, ...; Sólo la entidad Servidor puede consumir el servicio
//que suministra esta entidad
public class Basededatos {
	
	
	
	
	final static int PUERTO = 5555;
	final static String BBDD_URL = "rmi://localhost:" + PUERTO + "/BBDD/BBDD";
	
	public static void main(String[] args) {
		
		try {
			ServicioDatosInterface sdi = (ServicioDatosInterface) Naming.lookup(BBDD_URL);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	

}
