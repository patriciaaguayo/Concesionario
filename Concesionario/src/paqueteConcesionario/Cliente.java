package paqueteConcesionario;

import java.io.Serializable;

/**
 * @author Patricia Aguayo Escudero
 */

public class Cliente extends Persona implements Serializable, ICliente  {

	private static final long serialVersionUID = 1L;
	
	// Constantes
	
	private final Persona Persona_def = new Persona("Paco", "Porros", "12345678Z", "12/04/1980", 44, "Hombre", "Polvo Blanco", "Selva", "Bosque", 41020, "954896578", "muchoporro@gmail.com");
	private final Cuenta_Bancaria Cuenta_def = new Cuenta_Bancaria("ES7101698468334054893464", "Paco Porros", "Bankinter", "Sevilla");
		
	// Atributos
	

	private String FechaAlta;
	private boolean MayorEdad;
	private String Menor;
	private Persona P;
	private Cuenta_Bancaria Cuenta;
	private String NCuenta;
	
	// Constructor
	
	public Cliente(String nombre, String apellidos, String dNI, String fechaNacimiento, int edad, String sexo, String direccion,
			String localidad, String provincia, int codigoPostal, String telefono, String correo, String fechaAlta, Persona persona, 
			Cuenta_Bancaria cuenta, boolean mayor, String menor) {
		
		super(nombre, apellidos, dNI, fechaNacimiento, edad, sexo, direccion, localidad, provincia, codigoPostal, telefono, correo);
		
		FechaAlta = fechaAlta;
		MayorEdad = mayor;
		P = persona;
		Cuenta = cuenta;
		Menor = menor;
	}
	
	// Constructor para clientes mayores de edad sin representante ni responsable
	
    public Cliente(String nombre, String apellidos, String dNI, String fechaNacimiento, int edad, String sexo, String direccion,
                   String localidad, String provincia, int codigoPostal, String telefono, String correo, String fechaAlta, Cuenta_Bancaria cuenta) {
        
        super(nombre, apellidos, dNI, fechaNacimiento, edad, sexo, direccion, localidad, provincia, codigoPostal, telefono, correo);
        
        this.FechaAlta = fechaAlta;
        this.MayorEdad = true;  
        this.P = null; 
        this.Cuenta = cuenta;
        this.Menor = "S"; 
    }
    
// Constructor para clientes mayores de edad sin representante ni responsable
	
    public Cliente(String nombre, String apellidos, String dNI, String fechaNacimiento, int edad, String sexo, String direccion,
                   String localidad, String provincia, int codigoPostal, String telefono, String correo, String fechaAlta, String cuenta) {
        
        super(nombre, apellidos, dNI, fechaNacimiento, edad, sexo, direccion, localidad, provincia, codigoPostal, telefono, correo);
        
        this.FechaAlta = fechaAlta;
        this.MayorEdad = true;  
        this.P = null; 
        this.NCuenta = cuenta;
        this.Menor = "S"; 
    }
	
	// SETTERS Y GETTERS

	
	public void setFechaAlta(String fechaAlta) {
		FechaAlta = fechaAlta;
	}

	public String getFechaAlta() {
		return FechaAlta;
	}

	public void setMayorEdad(boolean mayorEdad) {
		
		if(mayorEdad == true) {
			MayorEdad = mayorEdad;
			
		}else if(mayorEdad == false) {
			MayorEdad = false;
			
		}else {
			MayorEdad = false;
		}	
	}

	public boolean getMayorEdad() {
		return MayorEdad;
	}

	public void setP(Persona p) {
		P = p;
	}

	public Persona getP() {
		return P;
	}

	public void setCuenta(Cuenta_Bancaria cuenta) {
		Cuenta = cuenta;
	}

	public Cuenta_Bancaria getCuenta() {
		return Cuenta;
	}
	
	public void setMenor(String menor) {
		
		String m = menor.toUpperCase();
		
		if(m.equalsIgnoreCase("S")) {
			this.Menor = m;
			
		}else if(m.equalsIgnoreCase("N")){
			this.Menor = m;
			
		}else {
			this.Menor = "N";
		}
	}
	
	public String getMenor() {
		
		return this.Menor;
	}
	
	public void setNCuenta(String cuenta) {
		
		this.NCuenta = cuenta;
	}
	
	public String getNCuenta() {
		
		return this.NCuenta;
	}

}
