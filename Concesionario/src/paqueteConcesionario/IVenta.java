package paqueteConcesionario;

import java.sql.Date;

/**
 * @author Patricia Aguayo Escudero
 */

public interface IVenta {
	
	public void setCoche(Vehiculo coche);
	public Vehiculo getCoche();
	public void setCliente(Cliente cliente);
	public Cliente getCliente();
	public void setVendedor(Vendedor vendedor);
	public Vendedor getVendedor();
	public void setCuenta(Cuenta_Bancaria cuenta);
	public Cuenta_Bancaria getCuenta();
	public void setFechaOperacion(Date fechaOperacion);
	public Date getFechaOperacion();
	public void setOperacion(String operacion);
	public String getOperacion();
	public void setVenta(String venta);
	public String getVenta();
	
}
