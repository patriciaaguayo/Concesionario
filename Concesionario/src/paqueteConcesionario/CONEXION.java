package paqueteConcesionario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private static final String usuario = "root";
	private static final String clave = "";
	private static final String BD = "concesionario";
	private static final String servidor = "127.0.0.1:3306/"+BD; // Propio
	private static final String servidor2 = "127.0.0.1:3307/"+BD; // Clase
	private static final String url = "jdbc:mysql://"+servidor; //Propio
	private static final String url2 = "jdbc:mysql://"+servidor2; // Clase
	
	private static final String controlador = "com.mysql.cj.jdbc.Driver";
	
	public static Connection Conectar() {
		
		Connection conex = null;
		
		try {
			Class.forName(controlador);
			conex = DriverManager.getConnection(url,usuario,clave); // Propio
			//conex = DriverManager.getConnection(url2,usuario,clave); // Clase
			//conex = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/BDVehiculo","root","");
		
		}catch (Exception e) {
			System.out.println("Error al conectar a la BD \n" + e.getMessage().toString());
		}
		
		return conex;
	}
	
	public static void cerrarConex(Connection conex) {
		
		try {
			conex.close();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage().toString());
		}
	}
	
}
