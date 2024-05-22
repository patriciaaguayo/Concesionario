package paqueteConcesionario;

public class ConcesionarioException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String Iban;
    private String Mensaje;
    private String Dni;
    private String Matricula;

    public ConcesionarioException(String dni, String mensaje) {
        super(mensaje);
        this.Dni = dni;
        this.Mensaje = mensaje;
    }

    public ConcesionarioException(String matricula, String mensaje, boolean isMatricula) {
        super(mensaje);
        this.Matricula = matricula;
        this.Mensaje = mensaje;
    }

    public ConcesionarioException(String iban, String mensaje, int i) {
        super(mensaje);
        this.Iban = iban;
        this.Mensaje = mensaje;
    }

    public String getIban() {
        return Iban;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getDni() {
        return Dni;
    }

    public String getMatricula() {
        return Matricula;
    }
}