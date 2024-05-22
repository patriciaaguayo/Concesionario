package paqueteConcesionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author Patricia Aguayo Escudero
 * 
 */

	public class Gestion_Ventas {

	Gestion_Clientes miCliente = new Gestion_Clientes();
	Gestion_Vehiculos miCoche = new Gestion_Vehiculos();
	Gestion_Vendedores miVendedor = new Gestion_Vendedores();
		
	// Constructor vacío
	
	public Gestion_Ventas(){
		
	}
	
	// MÉTODOS
	
	/**
	 * 
	 * @param leer para leer caracteres
	 * @param leer2 para leer numeros enteros
	 * @param leer3 para leer decimales
	 * @return devuelve un objeto de tipo cliente
	 * @throws Exception
	 */
	
	// Crea un objeto nuevo de tipo cliente mayor de edad
	
	public void crearNuevaVenta(Connection conex) {
        Scanner leer = new Scanner(System.in);
        Scanner leer2 = new Scanner(System.in);
        Scanner leer3 = new Scanner(System.in);
        
        int idVenta = 0;
        
        try {
            
            String maxIdQuery = "SELECT MAX(idventa) + 1 FROM venta";
            Statement maxIdStatement = conex.createStatement();
            ResultSet rs = maxIdStatement.executeQuery(maxIdQuery);

            if (rs.next()) {
                idVenta = rs.getInt(1);
            }
            
            if (idVenta == 0) { // Si no hay ventas, el primer ID será 1
                idVenta = 1;
            }

            rs.close();
            maxIdStatement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        
         try {
        
        String vehiculo = miCoche.obtenerMatricula(leer, leer2);
        
        if (!validarExistencia(conex, "vehiculo", "matricula", vehiculo)) {
                System.out.println("\n Error: Vehiculo no existe.");
                return;
            }
        
        System.out.println("\n Informacion del cliente");
        String cliente = miCliente.obtenerDNI(leer);
        
         if (!validarExistencia(conex, "cliente", "dni", cliente)) {
                System.out.println("\n Error: Cliente no existe.");
                return;
            }
        
        System.out.println("\n Informacion del vendedor");
        String vendedor = miVendedor.obtenerDNI(leer);
        
        if (!validarExistencia(conex, "vendedor", "dni", vendedor)) {
                System.out.println("\n Error: Vendedor no existe.");
                return;
            }
        
        String cuentaBancaria = miCliente.obtenerIBAN(leer3);
        
         if (!validarExistencia(conex, "cuentabancaria", "cuenta_iban", cuentaBancaria)) {
                System.out.println("\n Error: Cuenta bancaria no existe.");
                return;
            }
        
        Date fechaOperacion = obtenerFechaVenta(leer2);
        String tipoOperacion = obtenerTipoOperacion(leer);
        String tipoVenta = obtenerTipoVenta(leer);


            String query = "INSERT INTO venta (idventa, vehiculo, cliente, vendedor, cuenta_bancaria, fecha_opera, tipo_operacion, tipo_venta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conex.prepareStatement(query);
            statement.setInt(1, idVenta);
            statement.setString(2, vehiculo);
            statement.setString(3, cliente);
            statement.setString(4, vendedor);
            statement.setString(5, cuentaBancaria);
            statement.setDate(6, new java.sql.Date(fechaOperacion.getTime()));
            statement.setString(7, tipoOperacion);
            statement.setString(8, tipoVenta);
            statement.executeUpdate();

            System.out.println("\n Venta creada exitosamente.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	private boolean validarExistencia(Connection conex, String tabla, String campo, String valor) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tabla + " WHERE " + campo + " = ?";
        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.setString(1, valor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
	
	public static boolean validarCliente(String dni, Connection conex) {
		
        String query = "SELECT COUNT(*) FROM cliente WHERE dni = ?";
        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	/**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     */
    
 // Para mostrar listado de ventas por año
	 
 	 public void listarVentasAnyo(Connection conex) {
 	        
 	        int ano = obtenerAno();
 	    	
 	       try {
 	            
 	            String query = "SELECT * FROM venta WHERE YEAR(fecha_opera) = ?";
 	            PreparedStatement statement = conex.prepareStatement(query);
 	            statement.setInt(1, ano);
 	            ResultSet resultSet = statement.executeQuery();

 	            while (resultSet.next()) {
 	            	
 	            	int id = resultSet.getInt("idventa");
 	            	String vehiculo = resultSet.getString("vehiculo");
 	            	String cliente = resultSet.getString("cliente");
 	            	String vendedor = resultSet.getString("vendedor");
 	            	String cuenta = resultSet.getString("cuenta_bancaria");
 	            	Date fechaVenta= resultSet.getDate("fecha_opera");
 	            	String operacion = resultSet.getString("tipo_operacion");
 	            	String venta = resultSet.getString("tipo_venta");        	

 	                System.out.println("\n id: " + id + ", Vehiculo: " + vehiculo + ", Cliente: " + cliente +
 	                        ", vendedor: " + vendedor + ", Cuenta: " + cuenta + ", Fecha de la venta: "+ fechaVenta
 	                        + ", Tipo de operacion: " + operacion + ", Tipo de venta: " + venta);
 	           
 	            }
 	            
 	        } catch (SQLException e) {
 	            e.printStackTrace();
 	        }
 	 }
 	 
 	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      */
     
  // Para mostrar listado de ventas por vendedor
 	 
  	 public void listarVentasVendedor(Connection conex, Scanner leer) {
  	        
  	        String dni = miVendedor.obtenerDNI(leer);
  	    	
  	       try {
  	    	   
  	    	 if (!validarExistencia(conex, "vendedor", "dni", dni)) {
                 System.out.println("\n Error: Vendedor no existe.");
                 return;
             }
  	            
  	    	 String query = "SELECT * FROM venta WHERE vendedor = ?";
             PreparedStatement statement = conex.prepareStatement(query);
             statement.setString(1, dni);
             ResultSet resultSet = statement.executeQuery();

  	            while (resultSet.next()) {
  	            	
  	            	int id = resultSet.getInt("idventa");
  	            	String vehiculo = resultSet.getString("vehiculo");
  	            	String cliente = resultSet.getString("cliente");
  	            	String vendedor = resultSet.getString("vendedor");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");
  	            	Date fechaVenta= resultSet.getDate("fecha_opera");
  	            	String operacion2 = resultSet.getString("tipo_operacion");
  	            	String venta = resultSet.getString("tipo_venta");        	

  	                System.out.println("\n id: " + id + ", Vehiculo: " + vehiculo + ", Cliente: " + cliente +
  	                        ", vendedor: " + vendedor + ", Cuenta: " + cuenta + ", Fecha de la venta: "+ fechaVenta
  	                        + ", Tipo de operacion: " + operacion2 + ", Tipo de venta: " + venta);
  	           
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
 	 
 	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      */
     
  // Para mostrar listado de ventas por tipo de operacion
 	 
  	 public void listarVentasOperacion(Connection conex, Scanner leer) {
  	        
  	        String operacion = obtenerTipoOperacion(leer);
  	    	
  	       try {
  	            
  	    	 String query = "SELECT * FROM venta WHERE tipo_operacion = ?";
             PreparedStatement statement = conex.prepareStatement(query);
             statement.setString(1, operacion);
             ResultSet resultSet = statement.executeQuery();

  	            while (resultSet.next()) {
  	            	
  	            	int id = resultSet.getInt("idventa");
  	            	String vehiculo = resultSet.getString("vehiculo");
  	            	String cliente = resultSet.getString("cliente");
  	            	String vendedor = resultSet.getString("vendedor");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");
  	            	Date fechaVenta= resultSet.getDate("fecha_opera");
  	            	String operacion2 = resultSet.getString("tipo_operacion");
  	            	String venta = resultSet.getString("tipo_venta");        	

  	                System.out.println("\n id: " + id + ", Vehiculo: " + vehiculo + ", Cliente: " + cliente +
  	                        ", vendedor: " + vendedor + ", Cuenta: " + cuenta + ", Fecha de la venta: "+ fechaVenta
  	                        + ", Tipo de operacion: " + operacion2 + ", Tipo de venta: " + venta);
  	           
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
  	 
  	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      */
     
  // Para mostrar listado de ventas por vehiculo
 	 
  	 public void listarVentasVehiculo(Connection conex, Scanner leer, Scanner leer2) {
  	        
  	        String matricula = miCoche.obtenerMatricula(leer, leer2);
  	    	
  	       try {
  	    	   
  	    	 if (!validarExistencia(conex, "vehiculo", "matricula", matricula)) {
                 System.out.println("\n Error: Vehiculo no existe.");
                 return;
             }
  	            
  	    	 String query = "SELECT * FROM venta WHERE vehiculo = ?";
             PreparedStatement statement = conex.prepareStatement(query);
             statement.setString(1, matricula);
             ResultSet resultSet = statement.executeQuery();

  	            while (resultSet.next()) {
  	            	
  	            	int id = resultSet.getInt("idventa");
  	            	String vehiculo = resultSet.getString("vehiculo");
  	            	String cliente = resultSet.getString("cliente");
  	            	String vendedor = resultSet.getString("vendedor");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");
  	            	Date fechaVenta= resultSet.getDate("fecha_opera");
  	            	String operacion2 = resultSet.getString("tipo_operacion");
  	            	String venta = resultSet.getString("tipo_venta");        	

  	                System.out.println("\n id: " + id + ", Vehiculo: " + vehiculo + ", Cliente: " + cliente +
  	                        ", vendedor: " + vendedor + ", Cuenta: " + cuenta + ", Fecha de la venta: "+ fechaVenta
  	                        + ", Tipo de operacion: " + operacion2 + ", Tipo de venta: " + venta);
  	           
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
  	 
  	 // Métodos para obtener datos
  	 
  	private java.sql.Date obtenerFechaVenta(Scanner leer) {
        boolean fechaValida = false;
        java.sql.Date fecha = null;

        do {
            System.out.print("\n Introduce la fecha (DD/MM/AAAA): ");
            String fechaStr = leer.next();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false); // Esto asegura que solo se acepten fechas válidas
                java.util.Date parsedDate = dateFormat.parse(fechaStr);
                fecha = new java.sql.Date(parsedDate.getTime());
                fechaValida = true;

            } catch (ParseException e) {
                System.out.println("\n Formato de fecha incorrecto o fecha no válida. Debe ser DD/MM/AAAA.");
            }

        } while (!fechaValida);

        return fecha;
    }
  	
  	 
  	 public String obtenerTipoOperacion(Scanner leer) {
    	
        String operacion;
        
        do {
        	
            System.out.println("\n Introduce el tipo de operacion (VENTA, RENTING, LEASING, ALQUILER, OTRO): ");
            operacion = leer.nextLine().toUpperCase();
            
        } while (!operacion.equals("VENTA") && !operacion.equals("RENTING") && !operacion.equals("LEASING") && !operacion.equals("ALQUILER") && !operacion.equals("OTRO"));
       
        return operacion;
    }
  	 
  	 public String obtenerTipoVenta(Scanner leer) {
     	
         String venta;
         
         do {
         	
             System.out.println("\n Introduce el tipo de venta (NUEVO, SEMINUEVO, USADO, DEMO, OTRO): ");
             venta = leer.nextLine().toUpperCase();
             
         } while (!venta.equals("NUEVO") && !venta.equals("SEMINUEVO") && !venta.equals("USADO") && !venta.equals("DEMO") && !venta.equals("OTRO"));
        
         return venta;
     }
 	 
 	public int obtenerAno() {
     	
 		Scanner leer = new Scanner (System.in);
 		Scanner leer2 = new Scanner (System.in);
 		
         int ano = 0;;
         boolean entradaValida = false;
         
         do {
         
         try {
         	
         	do {
 	            System.out.print("\n Introduce el ano a buscar: ");
 	            ano = leer2.nextInt();
 	            entradaValida = true;
 	            if (String.valueOf(ano).length() != 4) {
 	                System.out.println("\n Debe tener 4 digitos.");
 	            }
         	} while (String.valueOf(ano).length() != 4);
         	
         } catch (InputMismatchException e) {
     		 
             System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
             leer.nextLine();
        
         } catch (Exception e) {
             System.out.println("\n Error: " + e.getMessage());
         } 
         
         }while(!entradaValida);
         
         return ano;
     }
}
