package modelo;

public class Sesion {
	private Perfil perfilUsuario;
	
	
	public Sesion() {
		
	}
	
	public Sesion(Perfil perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	
	public Perfil getPerfilUsuario() {
		return perfilUsuario;
	}

	public void setPerfilUsuario(Perfil perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}
}