package paqueteConcesionario;

import java.io.Serializable;

/**
 * @author Patricia Aguayo Escudero
 */

public class Vehiculo implements Serializable, IVehiculo {

	private static final long serialVersionUID = 1L;
	
	// Atributos
	
	private String Matricula;
	private String NumBastidor;
	private String Marca;
	private String Modelo;
	private int AnoProduccion;
	private Tipo_Vehiculo TIPO;
	private String Tipo;
	private Motorizacion MOTORIZACION;
	private String Motorizacion;
	private int Potencia;
	private int tamDeposito;
	private int numPuertas;
	private double Consumo;
	private String FechaMatriculacion;
	private String Nive; // Número de identificación del vehículo
	private Etiqueta_Eco ETIQUETA;
	private String Etiqueta;
	
	// Enum por defecto
	
	Tipo_Vehiculo otros = TIPO.OTROS;	
	private final String Tipo_def = otros.name();
	
	Motorizacion glp = MOTORIZACION.GLP;
	private final String Motorizacion_def = glp.name();
	
	Etiqueta_Eco o = ETIQUETA.O;
	private final String Etiqueta_def = o.name();
	
	// Constructor
	
	public Vehiculo(String matricula, String numBastidor, String marca, String modelo, int anoProduccion, String tipo,
			String motorizacion, int potencia, int tamDeposito, int numPuertas, double consumo,
			String fechaMatriculacion, String nive, String etiqueta) {
		
		Matricula = matricula;
		NumBastidor = numBastidor;
		Marca = marca;
		Modelo = modelo;
		AnoProduccion = anoProduccion;
		Tipo = tipo;
		Motorizacion = motorizacion;
		Potencia = potencia;
		this.tamDeposito = tamDeposito;
		this.numPuertas = numPuertas;
		Consumo = consumo;
		FechaMatriculacion = fechaMatriculacion;
		Nive = nive;
		Etiqueta = etiqueta;
	}
	
	// SETTERS Y GETTERS
	
	public void setMatricula(String matricula) {
		Matricula = matricula;
	}

	public String getMatricula() {
		return Matricula;
	}

	public void setNumBastidor(String numBastidor) {
		NumBastidor = numBastidor;
	}

	public String getNumBastidor() {
		return NumBastidor;
	}

	public void setMarca(String marca) {
		Marca = marca;
	}

	public String getMarca() {
		return Marca;
	}

	public void setModelo(String modelo) {
		Modelo = modelo;
	}

	public String getModelo() {
		return Modelo;
	}

	public void setAnoProduccion(int anoProduccion) {
		AnoProduccion = anoProduccion;
	}

	public int getAnoProduccion() {
		return AnoProduccion;
	}

	@SuppressWarnings("unlikely-arg-type")
	
	public void setTipo(String tipo) {
		
		String aux = tipo.toUpperCase();
		
		switch (aux) {
	    
	        case "AUTOMOVIL":
	        case "MOTOCICLETA":
	        case "CICLOMOTOR":
	        case "FURGONETA":
	        case "CAMION":
	        case "OTROS":
        	
	            this.Tipo = aux;
	            break;
            
        	default:
        	
	            this.Tipo = this.Tipo_def;
	            break;
		}

	}

	public String getTipo() {
		return Tipo;
	}

	@SuppressWarnings("unlikely-arg-type")
	
	public void setMotorizacion(String motorizacion) {
		
		String aux = motorizacion.toUpperCase();
		
		if(aux.equals("HIBRIDO ENCHUFABLE")) {
			aux = "HIBRIDO_ENCHUFABLE";
		}
		
		switch (aux) {
	    
	        case "GASOLINA":
	        case "DIESEL":
	        case "HIBRIDO":
	        case "HIBRIDO_ENCHUFABLE":
	        case "ELECTRICO":
	        case "GLP":
	        case "HIDROGENO":
        	
	            this.Motorizacion = aux;
	            break;
            
        	default:
        	
	            this.Motorizacion = this.Motorizacion_def;
	            break;
		}

	}

	public String getMotorizacion() {
		return Motorizacion;
	}

	public void setPotencia(int potencia) {
		Potencia = potencia;
	}

	public int getPotencia() {
		return Potencia;
	}

	public void setTamDeposito(int tamDeposito) {
		this.tamDeposito = tamDeposito;
	}

	public int getTamDeposito() {
		return tamDeposito;
	}

	public void setNumPuertas(int numPuertas) {
		this.numPuertas = numPuertas;
	}

	public int getNumPuertas() {
		return numPuertas;
	}

	public void setConsumo(double consumo) {
		Consumo = consumo;
	}

	public double getConsumo() {
		return Consumo;
	}

	public void setFechaMatriculacion(String fechaMatriculacion) {
		FechaMatriculacion = fechaMatriculacion;
	}

	public String getFechaMatriculacion() {
		return FechaMatriculacion;
	}

	public void setNive(String nive) {
		Nive = nive;
	}

	public String getNive() {
		return Nive;
	}

	@SuppressWarnings("unlikely-arg-type")
	
	public void setEtiqueta(String etiqueta) {
		
		String aux = etiqueta.toUpperCase();
		
		if(aux.equals("0")) {
			aux = "O";
		}
		
		switch (aux) {
	    
	        case "O":
	        case "ECO":
	        case "C":
	        case "B":
	        case "A":
        	
	            this.Etiqueta = aux;
	            break;
            
        	default:
        	
	            this.Etiqueta = this.Etiqueta_def;
	            break;
		}
		
	}

	public String getEtiqueta() {
		return Etiqueta;
	}	

}
