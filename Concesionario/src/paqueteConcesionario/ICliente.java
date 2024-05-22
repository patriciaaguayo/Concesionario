package paqueteConcesionario;

/**
 * @author Patricia Aguayo Escudero
 */

public interface ICliente {

	public void setFechaAlta(String fechaAlta);
	public String getFechaAlta();
	public void setMayorEdad(boolean mayorEdad);
	public boolean getMayorEdad();
	public void setP(Persona p);
	public Persona getP();
	public void setCuenta(Cuenta_Bancaria cuenta);
	public Cuenta_Bancaria getCuenta();
	public void setMenor(String menor);
	public String getMenor();
	public void setNCuenta(String cuenta);
	public String getNCuenta();
}
