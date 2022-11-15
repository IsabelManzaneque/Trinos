package es.uned.common;

public class Utils {
	
	public static final String CODEBASE = "java.rmi.server.codebase";
	
	public static void setCodeBase(Class<?> c) {
		
		String ruta = c.getProtectionDomain().getCodeSource().getLocation().toString();
		
		String path = System.getProperty(CODEBASE);
		
		if(path != null && !path.isBlank()) {
			ruta = path + " " + ruta;
		}
		
		System.setProperty(CODEBASE, ruta);
	}

}
