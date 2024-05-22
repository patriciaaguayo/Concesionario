package paqueteConcesionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Patricia Aguayo Escudero
 */

public class Gestion_Vehiculos{
	
	
	// Constructor vacío
	
	public Gestion_Vehiculos() {
		
	}

	
	// MÉTODOS 
	
	/**
	 * 
	 * @param leer Scanner para leer caracteres
	 * @param leer2 Scanner para leer numero enteros
	 * @param leer3 Scanner para leer decimales
	 * @return devuelve un objeto de tipo vehiculo
	 */
	
	public Vehiculo nuevoVehiculo(Connection conex, Scanner leer, Scanner leer2, Scanner leer3) throws Exception {
	    Vehiculo V = null;
	    try {
	        System.out.println("\n Insertar nuevo vehiculo:");
	        String matricula = obtenerMatricula(leer, leer2);
	        if (comprobarMatriculaRepetida(conex, matricula)) {
	            throw new ConcesionarioException(matricula, "Matricula repetida. Ya existe en el sistema");
	        }
	        String bastidor = obtenerBastidor(leer);
	        String marca = obtenerMarca(leer);
	        String modelo = obtenerModelo(leer);
	        int ano = obtenerAno(leer, leer2);
	        String tipo = obtenerTipoVehiculo(leer);
	        String motorizacion = obtenerMotorizacion(leer);
	        int potencia = obtenerPotencia(leer, leer2);
	        int deposito = obtenerTamanoDeposito(leer, leer2);
	        int puertas = obtenerNumPuertas(leer, leer2);
	        double consumo = obtenerConsumo(leer, leer3);
	        String etiqueta = obtenerEtiqueta(leer);
	        String fecha = obtenerFecha(leer);
	        String nive = obtenerNIVE(leer);

	        V = new Vehiculo(matricula, bastidor, marca, modelo, ano, tipo, motorizacion, 
	                potencia, deposito, puertas, consumo, fecha, nive, etiqueta);

	    } catch (ConcesionarioException e) {
	        System.out.println("\n Error: " + e.getMatricula() + " " + e.getMensaje());
	        return null;  // Salir del método si la matrícula está repetida
	        
	    } catch (Exception e) {
	        System.out.println("\n Error: Los datos del vehiculo son incorrectos.");
	        throw new Exception("Error en nuevoVehiculo: " + e.getMessage(), e);
	    }
	    
	    return V;
	}
	
	/**
	 * 
	 * @param conex parametro que permite la conexión con la base de datos
	 * @throws Exception para lanzar al excepción ante cualquier error en el sistema
	 */

	public void insertarVehiculo(Connection conex) throws Exception {
	    Scanner leer = new Scanner(System.in);
	    Scanner leer2 = new Scanner(System.in);
	    Scanner leer3 = new Scanner(System.in);

	    try {
	        Vehiculo vehiculo = nuevoVehiculo(conex, leer, leer2, leer3);
	        if (vehiculo == null) {
	            return; // Salir del método si no se pudo crear el vehículo
	        }

	        String query = "INSERT INTO vehiculo VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement statement = conex.prepareStatement(query);
	        statement.setString(1, vehiculo.getMatricula());
	        statement.setString(2, vehiculo.getNumBastidor());
	        statement.setString(3, vehiculo.getMarca());
	        statement.setString(4, vehiculo.getModelo());
	        statement.setInt(5, vehiculo.getAnoProduccion());
	        statement.setInt(6, vehiculo.getTamDeposito());
	        statement.setInt(7, vehiculo.getNumPuertas());
	        statement.setInt(8, vehiculo.getPotencia());
	        statement.setString(9, vehiculo.getMotorizacion());
	        statement.setString(10, vehiculo.getTipo());
	        statement.setDouble(11, vehiculo.getConsumo());
	        statement.setString(12, vehiculo.getFechaMatriculacion());
	        statement.setString(13, vehiculo.getNive());
	        statement.setString(14, vehiculo.getEtiqueta());
	        statement.executeUpdate();

	        System.out.println("\n Nuevo vehiculo creado y anadido a la base de datos.");

	    } catch (ConcesionarioException e) {
	        // Esta excepción ya se maneja en el método nuevoVehiculo
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
    
    /**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     * @param matricula variable que identifica al vehículo
     */
    
 // Para mostrar un vehículo concreto
	 
 	 public void verVehiculo(Connection conex, String matricula) {
 		 
 		 String Matricula = matricula;
 	        
 	        System.out.println();
 	    	
 	        try {
 	        	
 	            String query = "SELECT * FROM vehiculo WHERE matricula = '" + matricula + "'";
 	            Statement statement = conex.createStatement();
 	            ResultSet resultSet = statement.executeQuery(query);

 	            while (resultSet.next()) {
 	            	
 	            	Matricula = resultSet.getString("matricula");
 	                String bastidor = resultSet.getString("bastidor");
 	                String marca = resultSet.getString("marca");
 	                String modelo = resultSet.getString("modelo");
 	                int anoProduccion = resultSet.getInt("anyo");
 	                int tamDeposito = resultSet.getInt("deposito");
 	                int numPuertas = resultSet.getInt("n_puertas");
 	                int potencia = resultSet.getInt("potencia");
 	                String motorizacion = resultSet.getString("motorizacion");
 	                String tipo = resultSet.getString("tipo_vehiculo");
 	                double consumo = resultSet.getDouble("consumo");
 	                String fechaMatriculacion = resultSet.getString("fecha_matricula");
 	                String nive = resultSet.getString("nive");
 	                String etiqueta = resultSet.getString("etiqueta_eco");

 	                System.out.println("\n Matricula: " + Matricula + ", Bastidor: " + bastidor + ", Marca: " + marca +
 	                        ", Modelo: " + modelo + ", Ano de produccion: " + anoProduccion + ", Deposito: "+ tamDeposito
 	                        + ", Numero de puertas: " + numPuertas + ", Potencia: " + potencia + ", Motorizacion: " + motorizacion 
 	                        + ", Tipo: " + tipo +  ", Consumo: " + consumo + ", Fecha de matriculacion: " + fechaMatriculacion + 
 	                        ", NIVE: " + nive + ", Etiqueta: " + etiqueta);
 	            }
 	            
 	        } catch (SQLException e) {
 	            e.printStackTrace();
 	        }
 	 }
 	 
 	 /**
 	  * 
 	  * @param conex parametro que permite la conexión con la base de datos
 	  */
    
    public void borrarVehiculo(Connection conex) {
	       
    	Scanner leer = new Scanner(System.in);
        Scanner leer2 = new Scanner(System.in);
    	
    	String matricula = obtenerMatricula(leer, leer2);
    	
    	try {
            String query = "DELETE FROM vehiculo WHERE matricula = ?";
            PreparedStatement statement = conex.prepareStatement(query);
            statement.setString(1, matricula);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("\n Vehiculo con matricula " + matricula + " borrado correctamente.");
            } else {
                System.out.println("\n No se encontro ningun vehiculo con la matricula " + matricula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     */
    
    public void modificarVehiculo(Connection conex) {
        
    	Scanner leer = new Scanner(System.in);
        Scanner leer2 = new Scanner(System.in);
        Scanner leer3 = new Scanner(System.in);
        
        String matricula = obtenerMatricula(leer, leer2);
        verVehiculo(conex, matricula);
        
        System.out.println();
    	
        try {
        	
            String query = "SELECT * FROM vehiculo WHERE matricula = ?";
            PreparedStatement statement = conex.prepareStatement(query);
            statement.setString(1, matricula);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("\n Vehiculo encontrado. Introduce los nuevos datos:");

                 String bastidor = obtenerBastidor(leer);
                 String marca = obtenerMarca(leer);
                 String modelo = obtenerModelo(leer);
                 int ano = obtenerAno(leer, leer2);
                 String tipo = obtenerTipoVehiculo(leer);
                 String motorizacion = obtenerMotorizacion(leer);
                 int potencia = obtenerPotencia(leer, leer2);
                 int deposito = obtenerTamanoDeposito(leer, leer2);
                 int numPuertas = obtenerNumPuertas(leer, leer2);
                 double consumo = obtenerConsumo(leer, leer3);
                 String etiqueta = obtenerEtiqueta(leer);
                 String nive = obtenerNIVE(leer);
                 String fecha = obtenerFecha(leer3);

                String updateQuery = "UPDATE vehiculo SET bastidor = ?, marca = ?, modelo = ?, anyo = ?, deposito = ?, n_puertas = ?, potencia = ?, motorizacion = ?, tipo_vehiculo = ?, consumo = ?, fecha_matricula = ?, nive = ?, etiqueta_eco = ? WHERE matricula = ?";
                PreparedStatement updateStatement = conex.prepareStatement(updateQuery);
                updateStatement.setString(1, bastidor);
                updateStatement.setString(2, marca);
                updateStatement.setString(3, modelo);
                updateStatement.setInt(4, ano);
                updateStatement.setInt(5, deposito);
                updateStatement.setInt(6, numPuertas);
                updateStatement.setInt(7, potencia);
                updateStatement.setString(8, motorizacion);
                updateStatement.setString(9, tipo);
                updateStatement.setDouble(10, consumo);
                updateStatement.setString(11, fecha);
                updateStatement.setString(12, nive);
                updateStatement.setString(13, etiqueta);
                updateStatement.setString(14, matricula);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("\n Vehiculo actualizado correctamente.");
                    verVehiculo(conex, matricula);
                } else {
                    System.out.println("\n No se pudo actualizar el vehiculo.");
                }
            } else {
                System.out.println("\n No se encontro ningun vehiculo con la matricula " + matricula);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param conex parametro que permite la conexión con la base de datos
     */
    
    public void listarVehiculos(Connection conex) {
        try {
            String query = "SELECT * FROM vehiculo";
            Statement statement = conex.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String matricula = resultSet.getString("matricula");
                String bastidor = resultSet.getString("bastidor");
                String marca = resultSet.getString("marca");
                String modelo = resultSet.getString("modelo");
                int anoProduccion = resultSet.getInt("anyo");
                int tamDeposito = resultSet.getInt("deposito");
                int numPuertas = resultSet.getInt("n_puertas");
                int potencia = resultSet.getInt("potencia");
                String motorizacion = resultSet.getString("motorizacion");
                String tipo = resultSet.getString("tipo_vehiculo");
                double consumo = resultSet.getDouble("consumo");
                String fechaMatriculacion = resultSet.getString("fecha_matricula");
                String nive = resultSet.getString("nive");
                String etiqueta = resultSet.getString("etiqueta_eco");

                System.out.println("\n Matricula: " + matricula + ", Bastidor: " + bastidor + ", Marca: " + marca +
                        ", Modelo: " + modelo + ", Ano de produccion: " + anoProduccion + ", Deposito: "+ tamDeposito
                        + ", Numero de puertas: " + numPuertas + ", Potencia: " + potencia + ", Motorizacion: " + motorizacion 
                        + ", Tipo: " + tipo +  ", Consumo: " + consumo + ", Fecha de matriculacion: " + fechaMatriculacion + 
                        ", NIVE: " + nive + ", Etiqueta: " + etiqueta);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param cadena variable que se le pasa para comprobar que solo contiene letras
     * @return devuelve una cadena permitida de letras
     */
    
    public static boolean esLetras(String cadena) {
        return cadena.matches("^[a-zA-Z\\sáéíóúÁÉÍÓÚñÑ]+$");
    }
    
    public String obtenerMatricula(Scanner leer, Scanner leer2) {
    	int num;
        String letras;

        do {
            try {
                System.out.print("\n Introduce los numeros de la matricula (4 digitos): ");
                num = leer2.nextInt();
                
                if (String.valueOf(num).length() != 4) {
                    System.out.println("\n Debe tener 4 digitos.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n Error: Debes introducir un valor numerico.");
                leer2.nextLine(); 
                num = -1; 
                
            } catch (Exception e) {
                System.out.println("\n Error: " + e.getMessage());
                num = -1;
            }
            
        } while (String.valueOf(num).length() != 4);

        do {
            System.out.print("\n Introduce las letras de la matricula: ");
            letras = leer.nextLine().toUpperCase();
            if (letras.length() != 3) {
                System.out.println("\n Debe tener 3 letras.");
            }
        } while (letras.length() != 3);

        return num + letras;
    }
    
    public String obtenerEtiqueta(Scanner leer) {
        
    	String etiqueta;
    	
    	do {
            System.out.println("\n Introduce la etiqueta eco (O, ECO, C, B Y A): ");
            etiqueta = leer.nextLine().toUpperCase();
        } while (!etiqueta.equals("O") && !etiqueta.equals("ECO") && !etiqueta.equals("C") && !etiqueta.equals("B") && !etiqueta.equals("A"));
       
    	return etiqueta;
    }
    
    public static String obtenerNIVE(Scanner leer) {
        boolean niveValido = false;
        String nive = null;

        do {
            System.out.print("\n Introduce el Número de Identificación del Vehículo (NIVE) de 32 caracteres: ");
            nive = leer.nextLine().toUpperCase();

            if (nive.length() == 32 && nive.matches("[A-HJ-NPR-Z0-9]{32}")) {
                niveValido = true;
            } else {
                System.out.println("\n El NIVE debe tener 32 caracteres y no debe contener las letras I, O, Q.");
            }

        } while (!niveValido);

        return nive;
    }
    
    /**
     * 
     * @param leer se le pasa el scanner para no repetir código
     * @return se devuelve una variable de tipo String formateada para la fecha
     */
    
    public static String obtenerFecha(Scanner leer) {
        boolean fechaValida = false;
        String fecha = null;

        do {
            System.out.print("\n Introduce la fecha (DD/MM/AAAA): ");
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
    
    public double obtenerConsumo(Scanner leer2, Scanner leer3) {
    	
    	double consumo = 0;
    	boolean entradaValida = false;
    	
    	do {
    	
	    	try {
	    		
		        System.out.print("\n Introduce el consumo cada 100 km: ");
		       consumo = leer3.nextDouble();
		       entradaValida = true;
	        
	    	 } catch (InputMismatchException e) {
	    		 
		            System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
		            leer3.nextLine();
		       
		        } catch (Exception e) {
		            System.out.println("\n Error: " + e.getMessage());
		        } 
    	
    	}while(!entradaValida);
    	
    	return consumo;
    }
    
    public int obtenerNumPuertas(Scanner leer, Scanner leer2) {
    	
    	int puertas = 0;
    	boolean entradaValida = false;
    	
    	do {
    	
	    	try {
	    		 System.out.print("\n Introduce el numero de puertas: ");
	    		 puertas = leer2.nextInt();
	    		 entradaValida = true;
	    	 } catch (InputMismatchException e) {
	    		 
		            System.out.println("\n Error: Debes introducir un valor numerico.");
		            leer2.nextLine();
		       
		    } catch (Exception e) {
		          System.out.println("\n Error: " + e.getMessage());
		    } 
    	}while(!entradaValida);
       
        return puertas;
    }
    
    public int obtenerTamanoDeposito(Scanner leer, Scanner leer2) {
    	
    	int deposito = 0;
    	 boolean entradaValida = false;
    	 
    	do {
    	 
	    	try {
	        
	    		System.out.print("\n Introduce el tamano del deposito: ");
	    		deposito = leer2.nextInt();
	    		entradaValida = true;
	    		
	    	} catch (InputMismatchException e) {
	    		 
	    		  System.out.println("\n Error: Debes introducir un valor numerico");
	            leer2.nextLine();
	       
	        } catch (Exception e) {
	            System.out.println("\n Error: " + e.getMessage());
	        } 
    	
    	}while(!entradaValida);
    	
        return deposito;
    }
    
    public int obtenerPotencia(Scanner leer, Scanner leer2) {
    	
    	int potencia = 0;
        boolean entradaValida = false;

        do {
            try {
                System.out.print("\n Introduce la potencia: ");
                potencia = leer2.nextInt();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.println("\n Error: Debes introducir un valor numerico");
                leer2.nextLine();
            } catch (Exception e) {
                System.out.println("\n Error: " + e.getMessage());
            }
        } while (!entradaValida);

        return potencia;
    }
    
    public String obtenerTipoVehiculo(Scanner leer) {
       
    	String tipo;
    	
        do {
        	
            System.out.println("\n Introduce el tipo de vehiculo (AUTOMOVIL, MOTOCICLETA, CICLOMOTOR, FURGONETA, CAMION U OTROS): ");
            tipo = leer.nextLine().toUpperCase();
            
        } while (!tipo.equals("AUTOMOVIL") && !tipo.equals("MOTOCICLETA") && !tipo.equals("CICLOMOTOR") && !tipo.equals("FURGONETA") && !tipo.equals("CAMION") && !tipo.equals("OTROS"));
       
        return tipo;
    }

    public String obtenerMotorizacion(Scanner leer) {
    	
        String motorizacion;
        
        do {
        	
            System.out.println("\n Introduce el tipo de motorizacion (GASOLINA, DIESEL, HIBRIDO, HIBRIDO ENCHUFABLE, ELECTRICO, GLP E HIDROGENO): ");
            motorizacion = leer.nextLine().toUpperCase();
            
        } while (!motorizacion.equals("GASOLINA") && !motorizacion.equals("DIESEL") && !motorizacion.equals("HIBRIDO") && !motorizacion.equals("HIBRIDO ENCHUFABLE") && !motorizacion.equals("ELECTRICO") && !motorizacion.equals("GLP") && !motorizacion.equals("HIDROGENO"));
       
        return motorizacion;
    }
    
    public int obtenerAno(Scanner leer, Scanner leer2) {
    	
        int ano = 0;;
        boolean entradaValida = false;
        
        do {
        
        try {
        	
        	do {
	            System.out.print("\n Introduce el ano de produccion: ");
	            ano = leer2.nextInt();
	            entradaValida = true;
	            if (String.valueOf(ano).length() != 4) {
	                System.out.println("\n Debe tener 4 digitos.");
	            }
        	} while (String.valueOf(ano).length() != 4);
        	
        } catch (InputMismatchException e) {
    		 
            System.out.println("\n Error: " + (e.getMessage() != null ? e.getMessage() : "Debes introducir un valor numerico."));
            leer2.nextLine();
       
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        } 
        
        }while(!entradaValida);
        
        return ano;
    }
    
    public String obtenerMarca(Scanner leer) {
        
    	String marca;
    	
        do {
            System.out.print("\n Introduce la marca: ");
            marca = leer.nextLine().toUpperCase();
        } while (marca.isEmpty() || marca.length() > 20);
        
        return marca;
    }

    public String obtenerModelo(Scanner leer) {
        
    	String modelo;
        
        do {
            System.out.print("\n Introduce el modelo: ");
            modelo = leer.nextLine().toUpperCase();
        } while (modelo.isEmpty() || modelo.length() > 20);
        
        return modelo;
    }
    
    public String obtenerBastidor(Scanner leer) {
        
    	String bastidor;
    	
        do {
            System.out.print("\n Introduce el numero de bastidor: ");
            bastidor = leer.nextLine().toUpperCase();
        } while (bastidor.length() != 17);
        
        return bastidor;
    }
    
    /**
     * Métodos de la excepción propia
     */
    
    private boolean comprobarMatriculaRepetida(Connection conex, String matricula) throws SQLException {
        String query = "SELECT COUNT(*) FROM vehiculo WHERE matricula = ?";
        try (PreparedStatement statement = conex.prepareStatement(query)) {
            statement.setString(1, matricula);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }
	
}
