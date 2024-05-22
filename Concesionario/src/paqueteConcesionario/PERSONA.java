package paqueteConcesionario;

public class Persona implements IPersona {

	// Atributos
	
	private String Nombre;
	private String Apellidos;
	private String DNI;
	private String FechaNacimiento;
	private int Edad;
	private String Sexo;
	private String Direccion;
	private String Localidad;
	private String Provincia;
	private int CodigoPostal;
	private String Telefono;
	private String Correo;
	
	// Constructor
	
	public Persona(String nombre, String apellidos, String dNI, String fechaNacimiento, int edad, String sexo, String direccion,
			String localidad, String provincia, int codigoPostal, String telefono, String correo) {
		super();
		Nombre = nombre;
		Apellidos = apellidos;
		DNI = dNI;
		FechaNacimiento = fechaNacimiento;
		Edad = edad;
		Sexo = sexo;
		Direccion = direccion;
		Localidad = localidad;
		Provincia = provincia;
		CodigoPostal = codigoPostal;
		Telefono = telefono;
		Correo = correo;
	}
	
	// SETTERS Y GETTERS

	
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	public String getNombre() {
		return Nombre;
	}
	
	public void setApellidos(String apellidos) {
		Apellidos = apellidos;
	}
	
	public String getApellidos() {
		return Apellidos;
	}

	public void setDNI(String dNI) {
		DNI = dNI;
	}

	public String getDNI() {
		return DNI;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		FechaNacimiento = fechaNacimiento;
	}

	public String getFechaNacimiento() {
		return FechaNacimiento;
	}

	public void setEdad(int edad) {
		Edad = edad;
	}

	public int getEdad() {
		return Edad;
	}

	public void setSexo(String sexo) {
		Sexo = sexo;
	}

	public String getSexo() {
		return Sexo;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setLocalidad(String localidad) {
		Localidad = localidad;
	}

	public String getLocalidad() {
		return Localidad;
	}

	public void setProvincia(String provincia) {
		Provincia = provincia;
	}

	public String getProvincia() {
		return Provincia;
	}

	public void setCodigoPostal(int codigoPostal) {
		CodigoPostal = codigoPostal;
	}

	public int getCodigoPostal() {
		return CodigoPostal;
	}

	public void setTelefono(String telefono) {
		Telefono = telefono;
	}

	public String getTelefono() {
		return Telefono;
	}

	public void setCorreo(String correo) {
		Correo = correo;
	}

	public String getCorreo() {
		return Correo;
	}
	
}
