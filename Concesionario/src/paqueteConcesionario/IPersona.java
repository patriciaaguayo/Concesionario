package paqueteConcesionario;

public interface IPersona {

	public void setNombre(String nombre);
	public String getNombre();
	public void setApellidos(String apellidos);
	public String getApellidos();
	public void setDNI(String dNI);
	public String getDNI();
	public void setFechaNacimiento(String fechaNacimiento);
	public String getFechaNacimiento();
	public void setEdad(int edad);
	public int getEdad();
	public void setSexo(String sexo);
	public String getSexo();
	public void setDireccion(String direccion);
	public String getDireccion();
	public void setLocalidad(String localidad);
	public String getLocalidad();
	public void setProvincia(String provincia);
	public String getProvincia();
	public void setCodigoPostal(int codigoPostal);
	public int getCodigoPostal();
	public void setTelefono(String telefono);
	public String getTelefono();
	public void setCorreo(String correo);
	public String getCorreo();
}
