package es.uned.common;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String nick;
	private String password;
	private CallbackUsuarioInterface objCallback;
	
	public User(String name, String nick, String password) {
		this.name = name;
		this.nick = nick;
		this.password = password;			
	}

	public String getNick() {
		return nick;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	
	public void setObjCallback(CallbackUsuarioInterface objCallback) {
		this.objCallback = objCallback;
	}
	
	public CallbackUsuarioInterface getObjCallback() {
		return objCallback;
	}

	@Override
	public String toString() {		
		return "Nombre: " + name + " - Nick: " + nick + " - Password: " + password + " - ObjCallback: " + objCallback;
	}  
	
	
	

}
