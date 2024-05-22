package paqueteConcesionario;

public class Cuenta_Bancaria {

	// Atributos
	
	private String Iban;
	private String Titular;
	private String Entidad;
	private String Provincia;
	
	// Constructor
	
	public Cuenta_Bancaria(String iban, String titular, String entidad, String provincia) {
		
		Iban = iban;
		Titular = titular;
		Entidad = entidad;
		Provincia = provincia;
	}
	
	// SETTERS Y GETTERS
	
	public void setIban(String iban) {
		Iban = iban;
	}

	public String getIban() {
		return Iban;
	}

	public void setTitular(String titular) {
		Titular = titular;
	}

	public String getTitular() {
		return Titular;
	}

	public void setEntidad(String entidad) {
		Entidad = entidad;
	}

	public String getEntidad() {
		return Entidad;
	}

	public void setProvincia(String provincia) {
		Provincia = provincia;
	}

	public String getProvincia() {
		return Provincia;
	}
	
}
