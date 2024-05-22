package paqueteConcesionario;

import java.io.Serializable;

/**
 * @author Patricia Aguayo Escudero
 */

import java.sql.Date;

public class Venta implements Serializable, IVenta {

	private static final long serialVersionUID = 1L;
	
	// Atributos
	
	private Vehiculo Coche;
	private Cliente Cliente;
	private Vendedor Vendedor;
	private Cuenta_Bancaria Cuenta;
	private Date FechaOperacion;
	private Tipo_Operacion OPERACION;
	private String Operacion;
	private Tipo_Venta VENTA;
	private String Venta;
	
	// Enum por defecto
	
	Tipo_Operacion otro = OPERACION.OTRO;	
	private final String Operacion_def = otro.name();
	
	Tipo_Venta demo = VENTA.DEMO;
	private final String Venta_def = demo.name();
	
	// Constructor
	
	public Venta(Vehiculo coche, Cliente cliente, Vendedor vendedor,
			Cuenta_Bancaria cuenta, Date fechaOperacion, String operacion, String venta) {
		
		Coche = coche;
		Cliente = cliente;
		Vendedor = vendedor;
		Cuenta = cuenta;
		FechaOperacion = fechaOperacion;
		Operacion = operacion;
		Venta = venta;
	}
	
	// SETTERS Y GETTERS
	
	public void setCoche(Vehiculo coche) {
		Coche = coche;
	}

	public Vehiculo getCoche() {
		return Coche;
	}

	public void setCliente(Cliente cliente) {
		Cliente = cliente;
	}

	public Cliente getCliente() {
		return Cliente;
	}

	public void setVendedor(Vendedor vendedor) {
		Vendedor = vendedor;
	}

	public Vendedor getVendedor() {
		return Vendedor;
	}

	public void setCuenta(Cuenta_Bancaria cuenta) {
		Cuenta = cuenta;
	}

	public Cuenta_Bancaria getCuenta() {
		return Cuenta;
	}

	public void setFechaOperacion(Date fechaOperacion) {
		FechaOperacion = fechaOperacion;
	}

	public Date getFechaOperacion() {
		return FechaOperacion;
	}

	@SuppressWarnings("unlikely-arg-type")
	
	public void setOperacion(String operacion) {
		
		String aux = operacion.toUpperCase();
		
		switch (aux) {
	    
	        case "VENTA":
	        case "RENTING":
	        case "LEASING":
	        case "ALQUILER":
	        case "OTRO":
        	
	            this.Operacion = aux;
	            break;
            
        	default:
        	
	            this.Operacion = this.Operacion_def;
	            break;
		}

	}

	public String getOperacion() {
		return Operacion;
	}

	@SuppressWarnings("unlikely-arg-type")
	
	public void setVenta(String venta) {
		
		String aux = venta.toUpperCase();
		
		switch (aux) {
	    
	        case "NUEVO":
	        case "SEMINUEVO":
	        case "USADO":
	        case "DEMO":
	        case "OTRO":
        	
	            this.Venta = aux;
	            break;
            
        	default:
        	
	            this.Venta = this.Venta_def;
	            break;
		}
		
	}

	public String getVenta() {
		return Venta;
	}	

}
