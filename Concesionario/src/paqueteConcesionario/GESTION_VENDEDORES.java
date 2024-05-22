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

public class Gestion_Vendedores {

	// Constructor vacío
	
	public Gestion_Vendedores() {
		
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
	
	public Vendedor nuevoVendedor(Connection conex, Scanner leer, Scanner leer2, Scanner leer3) throws Exception {
	    Vendedor V = null;
	    try {
	        System.out.println("\n Insertar nuevo vendedor:");
	        String dni = obtenerDNI(leer);

	        if (comprobarDniRepetido(conex, dni)) {
	            throw new ConcesionarioException(dni, "Dni repetido. Ya existe en el sistema");
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
	        String categoria = obtenerCategoria(leer);
	        String departamento = obtenerDepartamento(leer);
	        double sueldo = obtenerSueldo(leer2, leer3);

	        V = new Vendedor(nombre, apellidos, dni, FNac, edad, sexo, direccion, 
	                localidad, provincia, cp, telefono, correo, categoria, departamento, sueldo);

	    } catch (ConcesionarioException e) {
	        System.out.println("\n Error: " + e.getDni() + " " + e.getMensaje());
	        return null;  // Salir del método si el DNI está repetido

	    } catch (Exception e) {
	        System.out.println("\n Error: Los datos del vendedor son incorrectos.");
	        throw new Exception("Error en nuevoVendedor: " + e.getMessage(), e);
	    }

	    return V;
	}
	
	/**
	 * 
	 * @param conex para conectar con la base de datos
	 * @throws Exception
	 */
	
	// Para guardar un nuevo vendedor en la base de datos
	
	public void insertarVendedor(Connection conex) throws Exception {
	    Scanner leer = new Scanner(System.in);
	    Scanner leer2 = new Scanner(System.in);
	    Scanner leer3 = new Scanner(System.in);

	    try {
	        Vendedor vendedor = nuevoVendedor(conex, leer, leer2, leer3);
	        if (vendedor == null) {
	            return; // Salir del método si no se pudo crear el vendedor
	        }

	        String query = "INSERT INTO vendedor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement statement = conex.prepareStatement(query);
	        statement.setString(1, vendedor.getDNI());
	        statement.setString(2, vendedor.getNombre());
	        statement.setString(3, vendedor.getApellidos());
	        statement.setString(4, vendedor.getFechaNacimiento());
	        statement.setInt(5, vendedor.getEdad());
	        statement.setString(6, vendedor.getSexo());
	        statement.setString(7, vendedor.getDireccion());
	        statement.setString(8, vendedor.getLocalidad());
	        statement.setString(9, vendedor.getProvincia());
	        statement.setInt(10, vendedor.getCodigoPostal());
	        statement.setString(11, vendedor.getTelefono());
	        statement.setString(12, vendedor.getCorreo());
	        statement.setString(13, vendedor.getCategoria());
	        statement.setString(14, vendedor.getDepartamento());
	        statement.setDouble(15, vendedor.getSueldoBase());
	        statement.executeUpdate();

	        System.out.println("\n Nuevo vendedor creado y añadido a la base de datos.");

	    } catch (ConcesionarioException e) {
	        // Esta excepción ya se maneja en el método nuevoVendedor
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	 
	 /**
 	  * 
 	  * @param conex parametro que permite la conexión con la base de datos
 	  */
    
    public void borrarVendedor(Connection conex) {
	       
    	Scanner leer = new Scanner(System.in);
    	
    	String dni = obtenerDNI(leer);
    	
    	try {
            String query = "DELETE FROM vendedor WHERE dni = ?";
            PreparedStatement statement = conex.prepareStatement(query);
            statement.setString(1, dni);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("\n Vendedor con dni " + dni + " borrado correctamente.");
            } else {
                System.out.println("\n No se encontro ningun vendedor con el dni " + dni);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     * @param conex para conectar con la base de datos
     */
    
    // Para modificar el sueldo de un vendedor
    
    public void modificarSueldo(Connection conex) {
        
    	Scanner leer = new Scanner(System.in);
    	Scanner leer2 = new Scanner(System.in);
        
        String dni = obtenerDNI(leer);     
        
        System.out.println();
    	
        try {
        	
            String query = "SELECT * FROM vendedor WHERE dni = ?";
            PreparedStatement statement = conex.prepareStatement(query);
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("\n Vendedor encontrado");

                verSueldo(conex, dni);
                
                System.out.println("\n Introduce el nuevo sueldo: ");
                
                 double sueldo = obtenerSueldo(leer, leer2);

                String updateQuery = "UPDATE vendedor SET sueldo_base = ? WHERE dni = ?";
                PreparedStatement updateStatement = conex.prepareStatement(updateQuery);
                updateStatement.setDouble(1, sueldo);
                updateStatement.setString(2, dni);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("\n Sueldo actualizado correctamente.");
                    verSueldo(conex, dni);
                } else {
                    System.out.println("\n No se pudo actualizar el sueldo.");
                }
            } else {
                System.out.println("\n No se encontro ningun vendedor con dni" + dni);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
    
    /**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     * @param dni variable que identifica al vendedor
     */
    
 // Para mostrar un vendedor concreto
	 
 	 public void verSueldo(Connection conex, String dni) {
 		 
 		 String Dni = dni;
 	        
 	        System.out.println();
 	    	
 	        try {
 	        	
 	        	String query = "SELECT dni, nombre, apellidos, sueldo_base FROM vendedor WHERE dni = '" + dni + "'";
 	            Statement statement = conex.createStatement();
 	            ResultSet resultSet = statement.executeQuery(query);

 	            while (resultSet.next()) {
 	            	
 	            	Dni = resultSet.getString("dni");
 	            	String nombre = resultSet.getString("nombre");
 	            	String apellidos = resultSet.getString("apellidos");
 	            	double sueldo = resultSet.getDouble("sueldo_base");

 	                System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " 
 	                + apellidos + "Sueldo: " + sueldo);
 	            }
 	            
 	        } catch (SQLException e) {
 	            e.printStackTrace();
 	        }
 	 }
    
    /**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     * @param dni variable que identifica al vendedor
     */
    
 // Para mostrar un vendedor concreto
	 
 	 public void verVendedor(Connection conex, String dni) {
 		 
 		 String Dni = dni;
 	        
 	        System.out.println();
 	    	
 	        try {
 	        	
 	            String query = "SELECT * FROM vendedor WHERE dni = '" + dni + "'";
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
 	            	int telefono = resultSet.getInt("telefono");
 	            	String correo = resultSet.getString("correo_e");
 	            	String categoria = resultSet.getString("categoria");
 	            	String departamento = resultSet.getString("departamento");
 	            	double sueldo = resultSet.getDouble("sueldo_base");

 	                System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
 	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
 	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
 	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
 	                        ", Categoria: " + categoria + ", Departamento: " + departamento + ", Sueldo: " + sueldo);
 	            }
 	            
 	        } catch (SQLException e) {
 	            e.printStackTrace();
 	        }
 	 }
 	 
 	/**
      * 
      * @param conex parametro que permite la conexión con la base de datos
      */
     
     public void modificarVendedor(Connection conex) {
         
     	Scanner leer = new Scanner(System.in);
         Scanner leer2 = new Scanner(System.in);
         Scanner leer3 = new Scanner(System.in);
         
         String dni = obtenerDNI(leer);
         verVendedor(conex, dni);
         
         System.out.println();
     	
         try {
         	
             String query = "SELECT * FROM vendedor WHERE dni = ?";
             PreparedStatement statement = conex.prepareStatement(query);
             statement.setString(1, dni);
             ResultSet resultSet = statement.executeQuery();

             if (resultSet.next()) {
                 System.out.println("\n Vendedor encontrado. Introduce los nuevos datos:");

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
             	String categoria = obtenerCategoria(leer);
             	String departamento = obtenerDepartamento(leer);	
             	double sueldo = obtenerSueldo(leer2, leer3);

                 String updateQuery = "UPDATE vendedor SET nombre = ?, apellidos = ?, fecha_nacimiento = ?, edad = ?, sexo = ?, direccion = ?, localidad = ?, provincia = ?, cod_postal = ?, telefono = ?, correo_e = ?, categoria = ?, departamento = ?, sueldo_base = ? WHERE dni = ?";
                 PreparedStatement updateStatement = conex.prepareStatement(updateQuery);
                
                 updateStatement.setString(1, nombre);
                 updateStatement.setString(2, apellidos);
                 updateStatement.setString(3,FNac);
                 updateStatement.setInt(4, edad);
                 updateStatement.setString(5, sexo);
                 updateStatement.setString(6, direccion);
                 updateStatement.setString(7, localidad);
                 updateStatement.setString(8, provincia);
                 updateStatement.setInt(9, cp);
                 updateStatement.setString(10, telefono);
                 updateStatement.setString(11, correo);
                 updateStatement.setString(12, categoria);
                 updateStatement.setString(13, departamento);
                 updateStatement.setDouble(14, sueldo);   
                 updateStatement.setString(15, dni);

                 int rowsUpdated = updateStatement.executeUpdate();

                 if (rowsUpdated > 0) {
                     System.out.println("\n Vendedor actualizado correctamente.");
                     verVendedor(conex, dni);
                 } else {
                     System.out.println("\n No se pudo actualizar el vendedor.");
                 }
             } else {
                 System.out.println("\n No se encontro ningun vendedor con dni " + dni);
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * 
      * @param conex para conectar con la base de datos
      */
     
     // Para lista a todos los vendedores
     
     public void listarVendedores(Connection conex) {
         try {
             String query = "SELECT * FROM vendedor";
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
	            String categoria = resultSet.getString("categoria");
	            String departamento = resultSet.getString("departamento");
	            double sueldo = resultSet.getDouble("sueldo_base");


	            System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
	                        ", Categoria: " + categoria + ", Departamento: " + departamento + ", Sueldo: " + sueldo);
	            
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * 
      * @param conex para conectar a la base de datos
      * @param leer para leer el departamento
      */
     
     // Para listar a los vendedores por departamento
     
     public void listarVendedoresDepartamento(Connection conex, Scanner leer) {
    	 
    	 try {
    		 String departamento = obtenerDepartamento(leer);
    		 String query = "SELECT * FROM vendedor WHERE departamento = '" + departamento + "'";
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
	            String categoria = resultSet.getString("categoria");
	            String dept = resultSet.getString("departamento");
	            double sueldo = resultSet.getDouble("sueldo_base");


	            System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
	                        ", Categoria: " + categoria + ", Departamento: " + dept + ", Sueldo: " + sueldo);
	            
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
     
     /**
      * 
      * @param conex para conectar con la base de datos
      * @param leer para leer la categoria
      */
     
     // Listar a los vendedores por categoria
     
     public void listarVendedoresCategoria(Connection conex, Scanner leer) {
    	 
    	 try {
    		 String categoria = obtenerCategoria(leer);
    		 String query = "SELECT * FROM vendedor WHERE categoria = '" + categoria + "'";
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
	            String cate = resultSet.getString("categoria");
	            String departamento = resultSet.getString("departamento");
	            double sueldo = resultSet.getDouble("sueldo_base");


	            System.out.println("\n Dni: " + Dni + ", Nombre: " + nombre + ", Apellidos: " + apellidos +
	                        ", Fecha de nacimiento: " + nac + ", Edad: " + edad + ", Sexo: "+ sexo
	                        + ", Direccion: " + direccion + ", Localidad: " + localidad + ", Provincia: " + provincia 
	                        + ", Codigo Postal: " + cp +  ", Telefono: " + telefono + ", Correo: " + correo + 
	                        ", Categoria: " + cate + ", Departamento: " + departamento + ", Sueldo: " + sueldo);
	            
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
	 
	 // Métodos para obtener la información del vendedor
	 
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
	 	
	 	public static String obtenerCategoria(Scanner leer) {
		 	   
	 		String categoria;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce la categoria: ");
	 	        categoria = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (categoria.length() <= 45 && esLetras(categoria)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return categoria;
	 	}
	 	
	 	public static String obtenerDepartamento(Scanner leer) {
		 	   
	 		String departamento;
	 	    
	 	    while (true) {
	 	        System.out.print("\n Introduce el departamento: ");
	 	        departamento = leer.nextLine().trim().toUpperCase();
	 	        
	 	        if (departamento.length() <= 45 && esLetras(departamento)) {
	 	            break;
	 	            
	 	        } else {
	 	            System.out.println("\n Solo se permiten letras y espacios");
	 	        }
	 	    }
	 	    return departamento;
	 	}
	 	
	 	public double obtenerSueldo(Scanner leer2, Scanner leer3) {
	    	
	    	double sueldo = 0;
	    	boolean entradaValida = false;
	    	
	    	do {
	    	
		    	try {
		    		
			        System.out.print("\n Introduce el sueldo: ");
			       sueldo = leer3.nextDouble();
			       entradaValida = true;
		        
		    	 } catch (InputMismatchException e) {
		    		 
			            System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
			            leer3.nextLine();
			       
			        } catch (Exception e) {
			            System.out.println("\n Error: " + e.getMessage());
			        } 
	    	
	    	}while(!entradaValida);
	    	
	    	return sueldo;
	    }
	 	
	 	/**
	     * Métodos de la excepción propia
	     */
	    
	    private boolean comprobarDniRepetido(Connection conex, String dni) throws SQLException {
	        String query = "SELECT COUNT(*) FROM vendedor WHERE dni = ?";
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
