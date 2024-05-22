package paqueteConcesionario;

import java.sql.Connection;
import java.util.Scanner;

/**
 * @author Patricia Aguayo Escudero
 */

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner leer = new Scanner(System.in);
	    Scanner leer2 = new Scanner(System.in);
	    Scanner leer3 = new Scanner(System.in);
	    
	    Connection conex = null;
	     
	     Gestion_Vehiculos GV = new Gestion_Vehiculos();
	     Gestion_Vendedores GVendedores = new Gestion_Vendedores();
	     Gestion_Clientes GC = new Gestion_Clientes();
	     Gestion_Ventas GVentas = new Gestion_Ventas();

	     try {
	            conex = Conexion.Conectar();

	            if (conex != null) {
	                System.out.println("\n CONECTADO A LA BASE DE DATOS");

	            } else {
	                System.out.println("\n NO CONECTADO ..... ");
	            }

	            String opcion = "";
	            int n = 30;

	            do {
	                MenuPrincipal();
	                System.out.print("\n Opcion seleccionada: ");
	                opcion = leer.nextLine();

	                try {

	                    n = Integer.parseInt(opcion);

	                    switch (n) {

	                    case 1:
		                	
							gestionarVehiculos(leer, leer2, GV, conex);
		                    break;
		                    
		                case 2: 
		                	
		                	try {
								gestionarClientes(leer, leer2, GC, conex);
							} catch (ConcesionarioException e) {
								
								e.printStackTrace();
							}
		                    break;
		                    
		                case 3:
		                    
		                	gestionarVendedores(leer, GVendedores, conex);
		                    break;
		                    
		                case 4:
		                	
		                    gestionarVentas(leer, leer2, leer3, GVentas, conex);
		                    break;
		                    
		                case 5:
		                	
		                    System.out.println("\n Gracias por usar nuestros servicios.");
		                    Conexion.cerrarConex(conex);
		                    break;
		                    
		                default:
		                    System.out.println("\n Opcion no valida.");
		            }

	                } catch (NumberFormatException e) {
	                    System.out.println("\n Introduce un numero y no letras");
	                }


	            } while (n != 5);

	        } finally {

	            leer.close();
	            leer2.close();
	            leer3.close();
	        }
	}
	
	/**
	 * Menus visibles para el usuario
	 */
	
	private static void MenuPrincipal() {
		
        System.out.println("\n -- Menu Principal: --");
        System.out.println(" 1. Gestion Vehiculos");
        System.out.println(" 2. Gestion Clientes");
        System.out.println(" 3. Gestion Vendedores");
        System.out.println(" 4. Gestion Ventas");
        System.out.println(" 5. Salir");
    }
	
	private static void MenuVehiculos() {
		
        System.out.println("\n -- Menu Vehiculos: --");
        System.out.println(" 1. Alta vehiculo");
        System.out.println(" 2. Consulta datos del vehiculo");
        System.out.println(" 3. Baja vehiculo");
        System.out.println(" 4. Modificar datos del vehiculo");
        System.out.println(" 5. Listado de vehiculos");
        System.out.println(" 6. Volver al menu principal");
        System.out.println(" 7. Salir");
    }
	
	private static void MenuClientes() {
		
        System.out.println("\n -- Menu Clientes: --");
        System.out.println(" 1. Alta cliente");
        System.out.println(" 2. Consulta datos cliente");
        System.out.println(" 3. Modificar datos del cliente");
        System.out.println(" 4. Asignar nueva cuenta bancaria");
        System.out.println(" 5. Listado de clientes");
        System.out.println(" 6. Listado de clientes menores de edad");
        System.out.println(" 7. Listado de clientes dado de alta en ano concreto");
        System.out.println(" 8. Volver al menu principal");
        System.out.println(" 9. Salir");
    }
	
	private static void MenuVendedores() {
		
        System.out.println("\n -- Menu Vendedores: --");
        System.out.println(" 1. Alta vendedor");
        System.out.println(" 2. Baja vendedor");
        System.out.println(" 3. Modificar sueldo");
        System.out.println(" 4. Modificar datos del vendedor");
        System.out.println(" 5. Listado de vendedores");
        System.out.println(" 6. Vendedores del departamento");
        System.out.println(" 7. Vendedores de la categoria");
        System.out.println(" 8. Volver al menu principal");
        System.out.println(" 9. Salir");
    }
	
	private static void MenuVentas() {
		
        System.out.println("\n -- Menu Ventas: --");
        System.out.println(" 1. Realizar venta");
        System.out.println(" 2. Consultar ventas del ano");
        System.out.println(" 3. Consultar ventas del vendedor");
        System.out.println(" 4. Consultar ventas por tipo de operacion");
        System.out.println(" 5. Consultar ventas por tipo de vehiculo");
        System.out.println(" 6. Volver al menu principal");
        System.out.println(" 7. Salir");
    }
	
	/**
	 * 
	 * @param leer Scanner para cadenas
	 * @param leer2 Scanner para numeros enteros
	 * @param GV Objeto de tipo gestion de los vehiculos para usar sus métodos
	 * @param conex parametro que permite la conexión con la base de datos
	 */
	
	private static void gestionarVehiculos(Scanner leer, Scanner leer2, Gestion_Vehiculos GV, Connection conex) {
        
		int opcionVehiculo = 0;
		String matricula;
      
		do {
			
            MenuVehiculos();
            System.out.print("\n Opcion seleccionada: ");
            opcionVehiculo = Integer.parseInt(leer.nextLine());

            switch (opcionVehiculo) {
            
                case 1: 
                   
				try {
					
					GV.insertarVehiculo(conex);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
                    break;
                    
                case 2: 
                	
                    matricula = GV.obtenerMatricula(leer, leer2);
                	GV.verVehiculo(conex, matricula);
                    break;
                    
                case 3: 
                   
                	GV.borrarVehiculo(conex);
                    break;
                    
                case 4: 
                   
                	GV.modificarVehiculo(conex);
                    break;
                    
                case 5: 
                   
                	GV.listarVehiculos(conex);
                    break;
                    
                case 6:
                    System.out.println("\n Volviendo al menu principal.");
                    break;
                    
                case 7:
                    System.out.println("\n Gracias por usar nuestros servicios."); 
                    System.exit(0);
                    Conexion.cerrarConex(conex);
                    
                default:
                    System.out.println("\n Opcion no valida.");
            }
        } while (opcionVehiculo != 6);
    }
	
	/**
	 * 
	 * @param leer Scanner para cadenas
	 * @param leer2 Scanner para numeros enteros
	 * @param GC Objeto de tipo gestion de los Clientes para usar sus métodos
	 * @param conex parametro que permite la conexión con la base de datos
	 * @throws ConcesionarioException excepcion propia
	 */

    private static void gestionarClientes(Scanner leer, Scanner leer2, Gestion_Clientes GC, Connection conex) throws ConcesionarioException {
       
    	int opcionCliente = 0;
    	String dni;
    	
        do {
            MenuClientes();
            System.out.print("\n Opcion seleccionada: ");
            opcionCliente = Integer.parseInt(leer.nextLine());

            switch (opcionCliente) {
            
                case 1: 
                    
					try {
						
						GC.nuevoCliente(leer, leer2, conex);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
                    break;
                    
                case 2: 
                   
                	dni = GC.obtenerDNI(leer);
                	GC.verCliente(conex, dni);
                    break;
                    
                case 3: 
                   
                	GC.modificarCliente(conex);
                    break;
                    
                case 4: 
                   
                	GC.modificarCuenta(conex);
                    break;
                    
                case 5: 
                   
                	GC.listarClientes(conex);
                    break;
                    
                case 6: 
                   
                	GC.listarClientesMenores(conex);
                    break;
                    
                case 7: 
                    
                	GC.listarClientesAnyo(conex);
                    break;
                    
                case 8:
                    System.out.println("\n Volviendo al menu principal.");
                    break;
                    
                case 9:
                    System.out.println("\n Gracias por usar nuestros servicios."); 
                    System.exit(0);
                    Conexion.cerrarConex(conex);
                    
                default:
                    System.out.println("\n Opcion no valida.");
            }
        } while (opcionCliente != 8);
    }
    
    /**
	 * 
	 * @param leer Scanner para cadenas
	 * @param GVendedores Objeto de tipo gestion de los vendedores para usar sus métodos
	 * @param conex parametro que permite la conexión con la base de datos
	 */

    private static void gestionarVendedores(Scanner leer, Gestion_Vendedores GVendedores, Connection conex) {
        
    	int opcionVendedor = 0;
        do {
            MenuVendedores();
            System.out.print("\n Opcion seleccionada: ");
            opcionVendedor = Integer.parseInt(leer.nextLine());

            switch (opcionVendedor) {
            
                case 1: 
                    
					try {
						
						GVendedores.insertarVendedor(conex);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
                    break;
                    
                case 2: 
                   
                	GVendedores.borrarVendedor(conex);
                    break;
                    
                case 3: 
                    
                	GVendedores.modificarSueldo(conex);
                    break;
                    
                case 4: 
                  
                	GVendedores.modificarVendedor(conex);
                    break;
                    
                case 5: 
                   
                	GVendedores.listarVendedores(conex);
                    break;
                    
                case 6: 
                    
                	GVendedores.listarVendedoresDepartamento(conex, leer);
                    break;
                    
                case 7: 
                    
                	GVendedores.listarVendedoresCategoria(conex, leer);
                    break;
                    
                case 8:
                    System.out.println("\n Volviendo al menu principal.");
                    break;
                    
                case 9:
                    System.out.println("\n Gracias por usar nuestros servicios."); 
                    System.exit(0);
                    Conexion.cerrarConex(conex);
                    
                default:
                    System.out.println("\n Opcion no valida.");
            }
        } while (opcionVendedor != 8);
    }
    
    /**
	 * 
	 * @param leer Scanner para cadenas
	 * @param leer2 Scanner para numeros enteros
	 * @param leer3 Scanner para numeros decimales
	 * @param GV Objeto de tipo gestion de los vehiculos para usar sus métodos
	 * @param conex parametro que permite la conexión con la base de datos
	 */

    private static void gestionarVentas(Scanner leer, Scanner leer2, Scanner leer3, Gestion_Ventas GVentas, Connection conex) {
        int opcionVenta = 0;
        do {
            MenuVentas();
            System.out.print("\n Opcion seleccionada: ");
            opcionVenta = Integer.parseInt(leer.nextLine());

            switch (opcionVenta) {
            
                case 1: // Lógica para realizar venta
                   
                	GVentas.crearNuevaVenta(conex);
                    break;
                    
                case 2: 
                   
                	GVentas.listarVentasAnyo(conex);
                    break;
                    
                case 3:
                    
                	GVentas.listarVentasVendedor(conex, leer3);
                    break;
                    
                case 4: 
                   
                	GVentas.listarVentasOperacion(conex, leer3);
                    break;
                    
                case 5: 
                    
                	GVentas.listarVentasVehiculo(conex, leer3, leer2);
                    break;
                    
                case 6:
                	
                    System.out.println("\n Volviendo al menu principal.");
                    break;
                    
                case 7:
                	
                    System.out.println("\n Gracias por usar nuestros servicios.");
                    System.exit(0);
                    Conexion.cerrarConex(conex);
                    
                default:
                    System.out.println("\n Opcion no valida.");
            }
        } while (opcionVenta != 6);
    }

}
