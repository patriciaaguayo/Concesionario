package paqueteConcesionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Patricia Aguayo Escudero
 * 
 */

public class Gestion_Clientes {

	// Constructor vacío para llamarlo desde el principal
	
	public Gestion_Clientes() {
		
	}
	
	// MÉTODOS
	
	/**
	 * 
	 * @param leer para leer caracteres
	 * @param leer2 para leer numeros enteros
	 * @param leer3 para leer decimales
	 * @return devuelve un objeto de tipo vendedor
	 * @throws Exception
	 */
	
	// Crea un objeto nuevo de tipo vendedor
	
	public Cliente nuevoCliente(Scanner leer, Scanner leer2, Connection conex) throws Exception {
        
		Cliente C = null;

	    try {
	        System.out.println("\n Insertar nuevo cliente:");

	        String dni = obtenerDNI(leer);
	         
	        // Verificar si el DNI está repetido
	        if (validarCliente(dni, conex)) {
	            throw new ConcesionarioException(dni, " El DNI del cliente ya esta registrado en el sistema");
	        }
	        
	        String nombre = obtenerNombre(leer);
	        String apellidos = obtenerApellidos(leer);
	        String FNac = obtenerFechaNacimiento(leer);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate fechaNacimiento = LocalDate.parse(FNac, formatter);

	        LocalDate fechaActual = LocalDate.now();

	        int edad = Period.between(fechaNacimiento, fechaActual).getYears();
	        
	        String sexo = obtenerSexo(leer);
	        String direccion = obtenerDireccion(leer);
	        String localidad = obtenerLocalidad(leer);
	        String provincia = obtenerProvincia(leer);
	        int cp = obtenerCodigoPostal(leer, leer2);
	        String telefono = obtenerTelefono(leer, leer2);
	        String correo = obtenerCorreo(leer);
	        String fechaAlta = obtenerFechaAlta(leer);

	       

	        Cuenta_Bancaria cuenta = null;
	        boolean cuentaEncontrada = false;

            
            leer.nextLine();

            while (!cuentaEncontrada) {
                System.out.println("\n Tiene una cuenta bancaria? (s/n)");
                String respuesta = leer.nextLine().trim().toLowerCase();

                if (respuesta.equals("s")) {
                    System.out.println("\n Ingrese el numero IBAN de la cuenta bancaria:");
                    String iban = obtenerIBAN(leer);

                    if (validarCuentaBancaria(iban, conex)) {
                        cuenta = obtenerCuentaBancaria(iban, conex);
                        cuentaEncontrada = true;
                    } else {
                        System.out.println("\n Cuenta bancaria no encontrada");
                    }
                } else if (respuesta.equals("n")) {
                    cuenta = crearNuevaCuentaBancaria(leer, conex);
                    cuentaEncontrada = true;
                } else {
                    System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
                }
            }
            
            if(edad < 18) {
            	
            	 boolean clienteEncontrado = false;
            	 Cliente R = null;

                 while (!clienteEncontrado) {
                     System.out.println("\n Tiene un cliente responsable? (s/n)");
                     String respuesta = leer.nextLine().trim().toLowerCase();

                     if (respuesta.equals("s")) {
                         System.out.println("\n Introduce el dni del responsable:");
                         String dni2 = obtenerDNI(leer);

                         if (validarCliente(dni2, conex)) {
                             R = obtenerCliente(dni2, conex);
                             C = new Cliente(nombre, apellidos, dni, FNac, edad, sexo, direccion, localidad, provincia, cp, telefono, correo, fechaAlta, R, cuenta, false, "N");
                        	 insertarCliente(conex, C);
                             clienteEncontrado = true;
                             
                         } else {
                             System.out.println("\n Cliente no encontrado");
                             
                         }
                         
                     } else if (respuesta.equals("n")) {
                    	 
                    	 R = nuevoResponsable(leer, leer2, conex);
                    	 C = new Cliente(nombre, apellidos, dni, FNac, edad, sexo, direccion, localidad, provincia, cp, telefono, correo, fechaAlta, R, cuenta, false, "N");
                    	 insertarCliente(conex, C);
                         clienteEncontrado = true;
                         
                     } else {
                         System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
                     }
                 }

            	
            }else {
            	
            	C = new Cliente(nombre, apellidos, dni, FNac, edad, sexo, direccion, localidad, provincia, cp, telefono, correo, fechaAlta, cuenta);
            	insertarClienteMayor(conex, C);
            }  
          
	    } catch (ConcesionarioException e) {
	        // Aquí solo imprimimos el mensaje personalizado, no el stack trace completo
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        // Si ocurre otra excepción, imprimimos un mensaje genérico de error
	        System.out.println("\n Error: " + e.getMessage());
	    }
       /* if (C == null) {
            throw new Exception("\n Error: Los datos del cliente son incorrectos.");
        }
        */
        return C;
    }
	
	/**
	 * 
	 * @param leer para leer caracteres
	 * @param leer2 para leer numeros enteros
	 * @param leer3 para leer decimales
	 * @return devuelve un objeto de tipo cliente
	 * @throws Exception
	 */
	
	// Crea un objeto nuevo de tipo cliente mayor de edad
	
	public Cliente nuevoResponsable(Scanner leer, Scanner leer2, Connection conex) throws Exception {
        
        Cliente R = null;

        try {
           
        	System.out.println("\n Insertar cliente responsable ");
            
        	String dni = obtenerDNI(leer);
        	
        	// Verificar si el DNI está repetido
	        if (validarCliente(dni, conex)) {
	            throw new ConcesionarioException(dni, " El DNI del cliente ya esta registrado en el sistema");
	        }
        	
        	String nombre = obtenerNombre(leer);
        	String apellidos = obtenerApellidos(leer);
        	
        	String FNac = obtenerFechaNacimiento(leer);
        	
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate fechaNacimiento = LocalDate.parse(FNac, formatter);

	        LocalDate fechaActual = LocalDate.now();

	        int edad = Period.between(fechaNacimiento, fechaActual).getYears();
        	
        	String sexo = obtenerSexo(leer);
        	String direccion = obtenerDireccion(leer);
        	String localidad = obtenerLocalidad(leer);
        	String provincia = obtenerProvincia(leer);
        	int cp = obtenerCodigoPostal(leer, leer2);
        	String telefono = obtenerTelefono(leer, leer2);
        	String correo = obtenerCorreo(leer);
        	String fechaAlta = obtenerFechaAlta(leer);

            Cuenta_Bancaria cuenta = null;
            boolean cuentaEncontrada = false;
            
            leer.nextLine();

            while (!cuentaEncontrada) {
                System.out.println("\n Tiene una cuenta bancaria? (s/n)");
                String respuesta = leer.nextLine().trim().toLowerCase();

                if (respuesta.equals("s")) {
                    System.out.println("\n Ingrese el numero IBAN de la cuenta bancaria:");
                    String iban = obtenerIBAN(leer);

                    if (validarCuentaBancaria(iban, conex)) {
                        cuenta = obtenerCuentaBancaria(iban, conex);
                        cuentaEncontrada = true;
                    } else {
                        System.out.println("\n Cuenta bancaria no encontrada");
                    }
                } else if (respuesta.equals("n")) {
                    cuenta = crearNuevaCuentaBancaria(leer, conex);
                    cuentaEncontrada = true;
                } else {
                    System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
                }
            }

            R = new Cliente(nombre, apellidos, dni, FNac, edad, sexo, direccion, localidad, provincia, cp, telefono, correo, fechaAlta, cuenta);

            insertarClienteMayor(conex, R);
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }

        if (R == null) {
            throw new Exception("\n Error: Los datos del cliente son incorrectos.");
        }

        return R;
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
	
	public static Cliente obtenerCliente(String dni, Connection conex) {
	    String query = "SELECT * FROM cliente WHERE dni = ?";
	    try (PreparedStatement stmt = conex.prepareStatement(query)) {
	        stmt.setString(1, dni);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new Cliente( 
	                rs.getString("nombre"),
	                rs.getString("apellidos"),
	                rs.getString("dni"),
	                rs.getString("fecha_nacimiento"),
	                rs.getInt("edad"),
	                rs.getString("sexo"),
	                rs.getString("direccion"),
	                rs.getString("localidad"),
	                rs.getString("provincia"),
	                rs.getInt("cod_postal"),
	                rs.getString("telefono"),
	                rs.getString("correo_e"),
	                rs.getString("fecha_alta"),
	                rs.getString("cuenta_bancaria"));
	           
	        }
                
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean validarCuentaBancaria(String iban, Connection conex) {
        String query = "SELECT COUNT(*) FROM cuentabancaria WHERE cuenta_iban = ?";
        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public static Cuenta_Bancaria obtenerCuentaBancaria(String iban, Connection conex) {
       
		String query = "SELECT * FROM cuentabancaria WHERE cuenta_iban = ?";
        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cuenta_Bancaria(
                        rs.getString("cuenta_iban"),
                        rs.getString("titular"),
                        rs.getString("entidad"),
                        rs.getString("provincia")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	static Cuenta_Bancaria crearNuevaCuentaBancaria(Scanner leer, Connection conex) throws ConcesionarioException {
        
		System.out.println("\n Nueva cuenta bancaria:");
		
        String iban = obtenerIBAN(leer);
        
     // Verificar si el IBAN ya existe en la base de datos
        if (validarCuentaBancaria(iban, conex)) {
            throw new ConcesionarioException(iban, " El IBAN de la cuenta bancaria ya existe en la base de datos.");
        }

        
        String titular = obtenerTitular(leer);
        String entidad = obtenerEntidad(leer);
        String provincia = obtenerProvincia(leer);

        String query = "INSERT INTO cuentabancaria (cuenta_iban, titular, entidad, provincia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, titular);
            stmt.setString(3, entidad);
            stmt.setString(4, provincia);
            stmt.executeUpdate();

            return new Cuenta_Bancaria(iban, titular, entidad, provincia);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
	
	/**
	 * 
	 * @param conex para conectar con la base de datos
	 * @throws Exception
	 */
	
	// Para guardar un nuevo cliente en la base de datos
	
	 public void insertarCliente(Connection conex, Cliente cliente) throws Exception {

	        try {
	            String query = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement statement = conex.prepareStatement(query);
	            
	            statement.setString(1, cliente.getDNI());
	            statement.setString(2, cliente.getNombre());
	            statement.setString(3, cliente.getApellidos());
	            statement.setString(4, cliente.getFechaNacimiento());
	            statement.setInt(5, cliente.getEdad());
	            statement.setString(6, cliente.getSexo());
	            statement.setString(7, cliente.getDireccion());
	            statement.setString(8, cliente.getLocalidad());
	            statement.setString(9, cliente.getProvincia());
	            statement.setInt(10, cliente.getCodigoPostal());
	            statement.setString(11, cliente.getTelefono());
	            statement.setString(12, cliente.getCorreo());
	            statement.setString(13, cliente.getCuenta().getIban());
	            statement.setString(14, cliente.getFechaAlta());
	            statement.setString(15, cliente.getMenor());
	            statement.setString(16, cliente.getP().getDNI());
	            statement.executeUpdate();

	            System.out.println("\n Nuevo cliente creado y anadido a la base de datos.");

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	/**
	 * 
	 * @param conex para conectar con la base de datos
	 * @throws Exception
	 */
	
	// Para guardar un nuevo cliente en la base de datos
	
	 public void insertarClienteMayor(Connection conex, Cliente cliente) throws Exception {

		 if (validarCliente(cliente.getDNI(), conex)) {
		        throw new Exception("Error: El cliente con DNI " + cliente.getDNI() + " ya existe.");
		    }
		 
	        try {
	            String query = "INSERT INTO cliente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement statement = conex.prepareStatement(query);
	            
	            statement.setString(1, cliente.getDNI());
	            statement.setString(2, cliente.getNombre());
	            statement.setString(3, cliente.getApellidos());
	            statement.setString(4, cliente.getFechaNacimiento());
	            statement.setInt(5, cliente.getEdad());
	            statement.setString(6, cliente.getSexo());
	            statement.setString(7, cliente.getDireccion());
	            statement.setString(8, cliente.getLocalidad());
	            statement.setString(9, cliente.getProvincia());
	            statement.setInt(10, cliente.getCodigoPostal());
	            statement.setString(11, cliente.getTelefono());
	            statement.setString(12, cliente.getCorreo());
	            statement.setString(13, cliente.getCuenta().getIban());
	            statement.setString(14, cliente.getFechaAlta());
	            statement.setString(15, cliente.getMenor());
	            statement.setNull(16, java.sql.Types.VARCHAR);
	            statement.executeUpdate();

	            System.out.println("\n Nuevo cliente creado y anadido a la base de datos.");

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	/**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     * @param dni variable que identifica al cliente
     */
    
 // Para mostrar un cliente concreto
	 
 	 public void verCliente(Connection conex, String dni) {
 		 
 		 String Dni = dni;
 	        
 	        System.out.println();
 	    	
 	        try {
 	        	
 	            String query = "SELECT * FROM cliente WHERE dni = '" + dni + "'";
 	            Statement statement = conex.createStatement();
 	            ResultSet resultSet = statement.executeQuery(query);

 	            while (resultSet.next()) {
 	            	
 	            	Dni = resultSet.getString("dni");
 	            	String nombre = resultSet.getString("nombre");
 	            	String apellidos = resultSet.getString("apellidos");
 	            	String nac = resultSet.getString("fecha_nacimiento");
 	            	int edad = resultSet.getInt("edad");
 	            	String sexo = resultSet.getString("sexo");
 	            	String direccion = resultSet.getString("direccion");
 	            	String localidad = resultSet.getString("localidad");
 	            	String provincia = resultSet.getString("provincia");
 	            	int cp = resultSet.getInt("cod_postal");
 	            	String telefono = resultSet.getString("telefono");
 	            	String correo = resultSet.getString("correo_e");
 	            	String cuenta = resultSet.getString("cuenta_bancaria");
 	            	String alta = resultSet.getString("fecha_alta");
 	            	String menor = resultSet.getString("mayor_edad");
 	            	String representante = resultSet.getString("representante_dni");

 	                System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
 	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
 	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
 	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
 	                        ", Cuenta bancaria: " + cuenta + ", Fecha de alta: " + alta + ", Menor de edad: " + menor
 	                        + ", Dni del representante: " + representante);
 	            }
 	            
 	        } catch (SQLException e) {
 	            e.printStackTrace();
 	        }
 	 }
 	 
 	/**
      * 
      * @param conex para conectar con la base de datos
 	 * @throws ConcesionarioException clase de la excepcion propia
      */
     
     // Para modificar la cuenta de un cliente
     
     public void modificarCuenta(Connection conex) throws ConcesionarioException {
         
     	Scanner leer = new Scanner(System.in);
     	Scanner leer2 = new Scanner(System.in);
         
         String dni = obtenerDNI(leer);     
         
         System.out.println();
     	
         try {
        	 
        	 int rowsUpdated = 0;
        	 String iban = "";
         	
             String query = "SELECT * FROM cliente WHERE dni= ?";
             PreparedStatement statement = conex.prepareStatement(query);
             statement.setString(1, dni);
             ResultSet resultSet = statement.executeQuery();

             if (resultSet.next()) {
                 System.out.println("\n Cliente encontrado");

                 verCuenta(conex, dni);
                 
                 Cuenta_Bancaria cuenta = null;
                 boolean cuentaEncontrada = false;

                 while (!cuentaEncontrada) {
                     System.out.println("\n Tiene otra cuenta bancaria? (s/n)");
                     String respuesta = leer2.nextLine().trim().toLowerCase();

                     if (respuesta.equals("s")) {
                         System.out.println("\n Ingrese el numero IBAN de la cuenta bancaria:");
                         iban = obtenerIBAN(leer2);

                         if (validarCuentaBancaria(iban, conex)) {
                             cuenta = obtenerCuentaBancaria(iban, conex);
                             cuentaEncontrada = true;
                             
                         } else {
                             System.out.println("\n Cuenta bancaria no encontrada");
                         }
                         
                     } else if (respuesta.equals("n")) {
                         cuenta = crearNuevaCuentaBancaria(leer, conex);
                         iban = cuenta.getIban();
                         cuentaEncontrada = true;
                         
                     } else {
                         System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
                     }
                 }
	  
                	  String updateQuery = "UPDATE cliente SET cuenta_bancaria = ? WHERE dni = ?";
                	  PreparedStatement updateStatement = conex.prepareStatement(updateQuery);
                	  updateStatement.setString(1, iban);
                	  updateStatement.setString(2, dni);
                	  rowsUpdated = updateStatement.executeUpdate();

                 if (rowsUpdated > 0) {
                     System.out.println("\n Cuenta actualizada correctamente.");
                     verCuenta(conex, dni);
                     
                 } else {
                     System.out.println("\n No se pudo actualizar la cuenta.");
                 }
                 
             } else {
                 System.out.println("\n No se encontro ningun cliente con dni" + dni);
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
             
         }
     }
     
     /**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      * @param dni variable que identifica al cliente
      */
     
  // Para mostrar un cliente concreto
 	 
  	 public void verCuenta(Connection conex, String dni) {
  		 
  		 String Dni = dni;
  	        
  	        System.out.println();
  	    	
  	        try {
  	        	
  	        	String query = "SELECT dni, nombre, apellidos, cuenta_bancaria FROM cliente WHERE dni = '" + dni + "'";
  	            Statement statement = conex.createStatement();
  	            ResultSet resultSet = statement.executeQuery(query);

  	            while (resultSet.next()) {
  	            	
  	            	Dni = resultSet.getString("dni");
  	            	String nombre = resultSet.getString("nombre");
  	            	String apellidos = resultSet.getString("apellidos");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");

  	                System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " 
  	                + apellidos + " Cuenta bancaria: " + cuenta);
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
  	 
  	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
  	 * @throws ConcesionarioException excepcion propia 
      */
     
  	public void modificarCliente(Connection conex) throws ConcesionarioException {

  	    Scanner leer = new Scanner(System.in);
  	    Scanner leer2 = new Scanner(System.in);
  	    
  	    String dni = obtenerDNI(leer);
  	    int edad = 0;
  	    String iban = "";
  	    String dniResponsable = null;
  	    
  	    try {
  	        
  	        String query = "SELECT dni, nombre, apellidos, edad, fecha_nacimiento FROM cliente WHERE dni = ?";
  	        PreparedStatement statement = conex.prepareStatement(query);
  	        statement.setString(1, dni);
  	        ResultSet resultSet = statement.executeQuery();

  	        if (resultSet.next()) {
  	            edad = resultSet.getInt("edad");
  	        }
  	        
  	        verCliente(conex, dni);
  	        
  	        System.out.println("\n Cliente encontrado. Introduce los nuevos datos:");
  	        
  	        String nombre = obtenerNombre(leer);
  	        String apellidos = obtenerApellidos(leer);
  	        String sexo = obtenerSexo(leer);
  	        String direccion = obtenerDireccion(leer);
  	        String localidad = obtenerLocalidad(leer);
  	        String provincia = obtenerProvincia(leer);
  	        int cp = obtenerCodigoPostal(leer, leer2);
  	        String telefono = obtenerTelefono(leer, leer2);
  	        String correo = obtenerCorreo(leer);
  	        String alta = obtenerFechaAlta(leer);
  	        
  	        Cuenta_Bancaria cuenta = null;
  	        boolean cuentaEncontrada = false;
  	        
  	        while (!cuentaEncontrada) {
  	            System.out.println("\n Tiene otra cuenta bancaria? (s/n)");
  	            String respuesta = leer2.nextLine().trim().toLowerCase();

  	            if (respuesta.equals("s")) {
  	                System.out.println("\n Ingrese el numero IBAN de la cuenta bancaria:");
  	                iban = obtenerIBAN(leer2);

  	                if (validarCuentaBancaria(iban, conex)) {
  	                    cuenta = obtenerCuentaBancaria(iban, conex);
  	                    cuentaEncontrada = true;
  	                    
  	                } else {
  	                    System.out.println("\n Cuenta bancaria no encontrada");
  	                }
  	                
  	            } else if (respuesta.equals("n")) {
  	                cuenta = crearNuevaCuentaBancaria(leer, conex);
  	                iban = cuenta.getIban();
  	                cuentaEncontrada = true;
  	                
  	            } else {
  	                System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
  	            }
  	        }
  	        
  	        if (edad < 18) {
  	            boolean clienteEncontrado = false;
  	            Cliente R = null;

  	            while (!clienteEncontrado) {
  	                System.out.println("\n Tiene un cliente responsable? (s/n)");
  	                String respuesta = leer.nextLine().trim().toLowerCase();

  	                if (respuesta.equals("s")) {
  	                    System.out.println("\n Introduce el dni del responsable:");
  	                    dniResponsable = obtenerDNI(leer);

  	                    if (validarCliente(dniResponsable, conex)) {
  	                        R = obtenerCliente(dniResponsable, conex);
  	                        clienteEncontrado = true;
  	                        
  	                    } else {
  	                        System.out.println("\n Cliente no encontrado");
  	                    }
  	                    
  	                } else if (respuesta.equals("n")) {
  	                    try {
  	                        R = nuevoResponsable(leer, leer2, conex);
  	                        dniResponsable = R.getDNI();
  	                        clienteEncontrado = true;
  	                        
  	                    } catch (Exception e) {
  	                        e.printStackTrace();
  	                    }
  	                    
  	                } else {
  	                    System.out.println("\n Respuesta no valida. Por favor, responda con 's' o 'n'.");
  	                }
  	            }
  	        }

  	        String updateQuery = "UPDATE cliente SET nombre = ?, apellidos = ?, sexo = ?, direccion = ?, localidad = ?, provincia = ?, cod_postal = ?, telefono = ?, correo_e = ?, cuenta_bancaria = ?, fecha_alta = ?, representante_dni = ? WHERE dni = ?";
  	        PreparedStatement updateStatement = conex.prepareStatement(updateQuery);
  	        
  	        updateStatement.setString(1, nombre);
  	        updateStatement.setString(2, apellidos);
  	        updateStatement.setString(3, sexo);
  	        updateStatement.setString(4, direccion);
  	        updateStatement.setString(5, localidad);
  	        updateStatement.setString(6, provincia);
  	        updateStatement.setInt(7, cp);
  	        updateStatement.setString(8, telefono);
  	        updateStatement.setString(9, correo);
  	        updateStatement.setString(10, iban);
  	        updateStatement.setString(11, alta);
  	        
  	        if (edad < 18) {
  	            updateStatement.setString(12, dniResponsable);
  	        } else {
  	            updateStatement.setNull(12, java.sql.Types.VARCHAR);
  	        }
  	        
  	        updateStatement.setString(13, dni);
  	        
  	        int rowsUpdated = updateStatement.executeUpdate();

  	        if (rowsUpdated > 0) {
  	            System.out.println("\n Cliente actualizado correctamente.");
  	            verCliente(conex, dni);
  	        } else {
  	            System.out.println("\n No se pudo actualizar el cliente.");
  	        }

  	    } catch (SQLException e) {
  	        e.printStackTrace();
  	    }
  	}
 	 
 	/**
      * 
      * @param conex para conectar con la base de datos
      */
     
     // Para lista a todos los clientes
     
     public void listarClientes(Connection conex) {
       
    	 try {
    		 
             String query = "SELECT * FROM cliente";
             Statement statement = conex.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

             while (resultSet.next()) {
            	 
            	String Dni = resultSet.getString("dni");
            	String nombre = resultSet.getString("nombre");
	            String apellidos = resultSet.getString("apellidos");
	            String nac = resultSet.getString("fecha_nacimiento");
	            int edad = resultSet.getInt("edad");
	            String sexo = resultSet.getString("sexo");
	            String direccion = resultSet.getString("direccion");
	            String localidad = resultSet.getString("localidad");
	            String provincia = resultSet.getString("provincia");
	            int cp = resultSet.getInt("cod_postal");
	            String telefono = resultSet.getString("telefono");
	            String correo = resultSet.getString("correo_e");
	            String cuenta = resultSet.getString("cuenta_bancaria");
	            String alta = resultSet.getString("fecha_alta");
	            String menor = resultSet.getString("mayor_edad");
	            String representante = resultSet.getString("representante_dni");


	            System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
	                        ", Cuenta bancaria: " + cuenta + ", Fecha de alta: " + alta + ", Menor de edad: " + menor
	                        + ", Dni del representante: " + representante);
	            
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      * @param dni variable que identifica al cliente
      */
     
  // Para mostrar listado de clientes menores de edad
 	 
  	 public void listarClientesMenores(Connection conex) {
  	        
  	        System.out.println();
  	    	
  	        try {
  	        	
  	            String query = "SELECT * FROM cliente WHERE mayor_edad = 'N'";
  	            Statement statement = conex.createStatement();
  	            ResultSet resultSet = statement.executeQuery(query);

  	            while (resultSet.next()) {
  	            	
  	            	String dni = resultSet.getString("dni");
  	            	String nombre = resultSet.getString("nombre");
  	            	String apellidos = resultSet.getString("apellidos");
  	            	String nac = resultSet.getString("fecha_nacimiento");
  	            	int edad = resultSet.getInt("edad");
  	            	String sexo = resultSet.getString("sexo");
  	            	String direccion = resultSet.getString("direccion");
  	            	String localidad = resultSet.getString("localidad");
  	            	String provincia = resultSet.getString("provincia");
  	            	int cp = resultSet.getInt("cod_postal");
  	            	String telefono = resultSet.getString("telefono");
  	            	String correo = resultSet.getString("correo_e");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");
  	            	String alta = resultSet.getString("fecha_alta");
  	            	String menor = resultSet.getString("mayor_edad");
  	            	String representante = resultSet.getString("representante_dni");

  	                System.out.println("\n Dni: " + dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
  	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
  	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
  	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
  	                        ", Cuenta bancaria: " + cuenta + ", Fecha de alta: " + alta + ", Menor de edad: " + menor
  	                        + ", Dni del representante: " + representante);
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
  	 
  	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      * @param dni variable que identifica al cliente
      */
     
  // Para mostrar listado de clientes por anyo
 	 
  	 public void listarClientesAnyo(Connection conex) {
  	        
  	        int ano = obtenerAno();
  	    	
  	        try {
  	        	
  	            String query = "SELECT * FROM cliente WHERE fecha_alta LIKE '%/" + ano + "'";
  	            Statement statement = conex.createStatement();
  	            ResultSet resultSet = statement.executeQuery(query);

  	            while (resultSet.next()) {
  	            	
  	            	String dni = resultSet.getString("dni");
  	            	String nombre = resultSet.getString("nombre");
  	            	String apellidos = resultSet.getString("apellidos");
  	            	String nac = resultSet.getString("fecha_nacimiento");
  	            	int edad = resultSet.getInt("edad");
  	            	String sexo = resultSet.getString("sexo");
  	            	String direccion = resultSet.getString("direccion");
  	            	String localidad = resultSet.getString("localidad");
  	            	String provincia = resultSet.getString("provincia");
  	            	int cp = resultSet.getInt("cod_postal");
  	            	String telefono = resultSet.getString("telefono");
  	            	String correo = resultSet.getString("correo_e");
  	            	String cuenta = resultSet.getString("cuenta_bancaria");
  	            	String alta = resultSet.getString("fecha_alta");
  	            	String menor = resultSet.getString("mayor_edad");
  	            	String representante = resultSet.getString("representante_dni");

  	                System.out.println("\n Dni: " + dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
  	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
  	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
  	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
  	                        ", Cuenta bancaria: " + cuenta + ", Fecha de alta: " + alta + ", Menor de edad: " + menor
  	                        + ", Dni del representante: " + representante);
  	            }
  	            
  	        } catch (SQLException e) {
  	            e.printStackTrace();
  	        }
  	 }
	
	 // Métodos para obtener la información del cliente
	 
	 /**
	     * 
	     * @param cadena variable que se le pasa para comprobar que solo contiene letras
	     * @return devuelve una cadena permitida de letras
	     */
	    
	 	public static boolean esLetras(String cadena) {
		    return cadena.matches("^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ]+$");
		}
	    
	 	public static String obtenerNombre(Scanner leer) {
	 	   
	 		String nombre;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce el nombre: ");
	 	        nombre = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (nombre.length() <= 45 && esLetras(nombre)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios, y debe tener como máximo 45 caracteres.");
	 	        }
	 	    }
	 	    return nombre;
	 	}
	 	
	 	public static String obtenerApellidos(Scanner leer) {
		 	   
	 		String apellidos;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce los apellidos: ");
	 	        apellidos = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (apellidos.length() <= 45 && esLetras(apellidos)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios, y debe tener como máximo 45 caracteres.");
	 	        }
	 	    }
	 	    return apellidos;
	 	}
	 	
	 	// Método para comprobar el número y letra del dni
	 	
	 	public static char calcularLetraDNI(int numeroDNI) {
	 	    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
	 	    return letras.charAt(numeroDNI % 23);
	 	}
	
	 	public static String obtenerDNI(Scanner leer) {
	 		
	 	    String dniPattern = "^[0-9]{8}[A-Z]$"; // delimita el formato a 8 números y una letra
	 	    String dni;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce el DNI: ");
	 	        dni = leer.nextLine().trim().toUpperCase();
	 	        if (Pattern.matches(dniPattern, dni)) {
	 	     
	 	            int numeroDNI = Integer.parseInt(dni.substring(0, 8)); // Para el número

	 	            char letraDNI = dni.charAt(8); // Para la letra
	 	           
	 	            char letraCalculada = calcularLetraDNI(numeroDNI); // Calcular la letra correspondiente al número del DNI
	 	           
	 	            if (letraCalculada == letraDNI) { // Comparar la letra calculada con la letra proporcionada por el usuario
	 	                break;
	 	                
	 	            } else {
	 	                System.out.println("\n La letra del DNI no coincide con el numero.");
	 	            }
	 	        } else {
	 	            System.out.println("\n DNI incorrecto. Debe tener 8 numeros y 1 letra");
	 	        }
	 	    }
	 	    return dni;
	 	}
	 	
	 	public static String obtenerFechaNacimiento(Scanner leer) {
	        boolean fechaValida = false;
	        String fecha = null;

	        do {
	            System.out.print("\n Introduce la fecha de nacimiento (DD/MM/AAAA): ");
	            fecha = leer.next();

	            try {
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	                dateFormat.setLenient(false); // con esto solo se aceptan fechas válidas, no sirve cualquiera
	                java.util.Date parsedDate = dateFormat.parse(fecha);
	                fechaValida = true;

	            } catch (ParseException e) {
	                System.out.println("\n Formato de fecha incorrecto. Debe ser DD/MM/AAAA y la fecha debe ser valida.");
	            }

	        } while (!fechaValida);

	        return fecha;
	    }
	 	
	 	 public String obtenerSexo(Scanner leer) {
	 	       
	 		Scanner leer3 = new Scanner(System.in);
	     	String sexo;
	     	
	         do {
	         	
	             System.out.print("\n Introduce el sexo H o M: ");
	             sexo = leer3.nextLine().toUpperCase();
	             
	         } while (!sexo.equals("H") && !sexo.equals("M"));
	        
	         return sexo;
	     }
	 	 
	 	public String obtenerDireccion(Scanner leer) {
	        
	    	String direccion;
	    	
	        do {
	            System.out.print("\n Introduce la direccion: ");
	            direccion = leer.nextLine().toUpperCase().trim();
	        } while (direccion.isEmpty() || direccion.length() > 100);
	        
	        return direccion;
	    }
	 	
	 	public static String obtenerLocalidad(Scanner leer) {
		 	   
	 		String localidad;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce la localidad: ");
	 	        localidad = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (localidad.length() <= 45 && esLetras(localidad)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return localidad;
	 	}
	 	
	 	public static String obtenerProvincia(Scanner leer) {
		 	   
	 		String provincia;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce la provincia: ");
	 	        provincia = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (provincia.length() <= 45 && esLetras(provincia)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return provincia;
	 	}
	 	
	 	 public int obtenerCodigoPostal(Scanner leer, Scanner leer2) {
	     	
	         int cp = 0;;
	         boolean entradaValida = false;
	         
	         do {
	         
	         try {
	         	
	         	do {
	 	            System.out.print("\n Introduce el codigo postal: ");
	 	            cp = leer2.nextInt();
	 	            entradaValida = true;
	 	            if (String.valueOf(cp).length() != 5) {
	 	                System.out.println("\n Debe tener 5 digitos.");
	 	            }
	         	} while (String.valueOf(cp).length() != 5);
	         	
	         } catch (InputMismatchException e) {
	     		 
	             System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
	             leer2.nextLine();
	        
	         } catch (Exception e) {
	             System.out.println("\n Error: " + e.getMessage());
	         } 
	         
	         }while(!entradaValida);
	         
	         return cp;
	     }
	 	 
	 	public String obtenerTelefono(Scanner leer, Scanner leer2) {
	     	
	         String telefono = "";
	         int tlf = 0;
	         boolean entradaValida = false;
	         
	         do {
	         
	         try {
	         	
	         	do {
	 	            System.out.print("\n Introduce el telefono: ");
	 	            tlf = leer2.nextInt();
	 	            telefono = String.valueOf(tlf);
	 	            entradaValida = true;
	 	            if (String.valueOf(telefono).length() != 9) {
	 	                System.out.println("\n Debe tener 9 digitos.");
	 	            }
	         	} while (String.valueOf(telefono).length() != 9);
	         	
	         } catch (InputMismatchException e) {
	     		 
	             System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
	             leer2.nextLine();
	        
	         } catch (Exception e) {
	             System.out.println("\n Error: " + e.getMessage());
	         } 
	         
	         }while(!entradaValida);
	         
	         return telefono;
	     }
	 	
	 	public String obtenerCorreo(Scanner leer) {
	        
	    	String correo;
	    	
	        do {
	            System.out.print("\n Introduce el correo: ");
	            correo = leer.nextLine().toUpperCase().trim();
	        } while (correo.isEmpty() || correo.length() > 50);
	        
	        return correo;
	    }
	 	
	 	public static String obtenerFechaAlta(Scanner leer) {
	        boolean fechaValida = false;
	        String fecha = null;

	        do {
	            System.out.print("\n Introduce la fecha de alta (DD/MM/AAAA): ");
	            fecha = leer.next();

	            try {
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	                dateFormat.setLenient(false); // con esto solo se aceptan fechas válidas, no sirve cualquiera
	                java.util.Date parsedDate = dateFormat.parse(fecha);
	                fechaValida = true;

	            } catch (ParseException e) {
	                System.out.println("\n Formato de fecha incorrecto. Debe ser DD/MM/AAAA y la fecha debe ser valida.");
	            }

	        } while (!fechaValida);

	        return fecha;
	    }
	 	
	 	public static String obtenerTitular(Scanner leer) {
		 	   
	 		String nombre;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce el nombre: ");
	 	        nombre = leer.nextLine().trim().toUpperCase();      	        
	 	        
	 	       if (nombre.isEmpty()) {
                   throw new IllegalArgumentException("El nombre del titular no puede estar vacio.");
               }
	 	        
	 	        if (nombre.length() <= 50 && esLetras(nombre)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return nombre;
	 	}
	 	
	 	public static String obtenerEntidad(Scanner leer) {
		 	   
	 		String entidad;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce la entidad bancaria: ");
	 	        entidad = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (entidad.length() <= 45 && esLetras(entidad)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return entidad;
	 	}
	 	
	 	public static String obtenerIBAN(Scanner leer) {
	        
	    	String iban = "ES";
	    	String numero = "";
	    	
	        do {
	            System.out.print("\n Introduce el numero iban de la cuenta sin las letras ES: ");
	            numero = leer.nextLine().toUpperCase();
	            iban = iban + numero;
	        } while (iban.length() != 24);
	        
	        return iban;
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
	 	
	 	/**
	     * Métodos de la excepción propia
	     */
	    
	    private boolean comprobarDniRepetido(Connection conex, String dni) throws SQLException {
	        String query = "SELECT COUNT(*) FROM cliente WHERE dni = ?";
	        try (PreparedStatement statement = conex.prepareStatement(query)) {
	            statement.setString(1, dni);
	            ResultSet resultSet = statement.executeQuery();
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                return count > 0;
	            }
	        }
	        return false;
	    }

}
