package modelo;

public class Sesion {
	//Atributos de la clase:
	private Perfil perfilUsuario;
	
	//Constructores de la clase:
	public Sesion() {
		
	}
	
	public Sesion(Perfil perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	//Métodos Getter y Setter de la clase:
	public Perfil getPerfilUsuario() {
		return perfilUsuario;
	}

	public void setPerfilUsuario(Perfil perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
}