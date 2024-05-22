package paqueteConcesionario;

import java.io.Serializable;

public class Vendedor extends Persona implements Serializable, IVendedor {

	private static final long serialVersionUID = 1L;
	
	// Atributos
	
	private String Categoria;
	private String Departamento;
	private double SueldoBase;
	
	// Constructor
	
	public Vendedor(String nombre, String apellidos, String dNI, String fechaNacimiento, int edad, String sexo, String direccion,
			String localidad, String provincia, int codigoPostal, String telefono, String correo, String categoria,
			String departamento, double sueldo_base) {
		
		super(nombre, apellidos, dNI, fechaNacimiento, edad, sexo, direccion, localidad, provincia, codigoPostal, telefono, correo);
		
		this.Categoria = categoria;
		this.Departamento = departamento;
		this.SueldoBase = sueldo_base;
	}

	// SETTERS Y GETTERS
	
	public void setCategoria(String categoria) {
		Categoria = categoria;
	}
	
	public String getCategoria() {
		return Categoria;
	}

	public void setDepartamento(String departamento) {
		Departamento = departamento;
	}

	public String getDepartamento() {
		return Departamento;
	}

	public void setSueldoBase(double sueldoBase) {
		SueldoBase = sueldoBase;
	}

	public double getSueldoBase() {
		return SueldoBase;
	}

}
