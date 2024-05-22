package paqueteConcesionario;

public interface IVehiculo {

	public void setMatricula(String matricula);
	public String getMatricula();
	public void setNumBastidor(String numBastidor);
	public String getNumBastidor();
	public void setMarca(String marca);
	public String getMarca();
	public void setModelo(String modelo);
	public String getModelo();
	public void setAnoProduccion(int anoProduccion);
	public int getAnoProduccion();
	public void setTipo(String tipo);
	public String getTipo();
	public void setMotorizacion(String motorizacion);
	public String getMotorizacion();
	public void setPotencia(int potencia);
	public int getPotencia();
	public void setTamDeposito(int tamDeposito);
	public int getTamDeposito();
	public void setNumPuertas(int numPuertas);
	public int getNumPuertas();
	public void setConsumo(double consumo);
	public double getConsumo();
	public void setFechaMatriculacion(String fechaMatriculacion);
	public String getFechaMatriculacion();
	public void setNive(String nive);
	public String getNive();
	public void setEtiqueta(String etiqueta);
	public String getEtiqueta();
}
