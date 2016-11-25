/*
 * To change this template, choose Tools | Templates
 * and opem
 * n the template in the editor.
 */


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Menú Principal
 */
public class GestorProcesos {

    public enum opcionesMenu {

        I, C, M, A, D, R, S
    };

    public enum opcionesSubMenuConfiguracion {

        I, R, L, M, P
    };

    public enum opcionesSubMenuArrancar {

        I, M, R
    };

    public enum opcionesSubMenuMonitor {

        I, M, R
    };
    private static Scanner kbd;

    /**
     * The main program handles the main menu.
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {


        String input;
        kbd = new Scanner(System.in);
        System.out.println("*****Menú Principal Gestor de Procesos *****");
        MultiThreadedServer[] server = new MultiThreadedServer[64000];
        MyMonitorThread[] monitoresPool = new MyMonitorThread[64000];
        Thread[] monitorHilos = new Thread[64000];

        do {
            System.out.print("\n Indique una opción para comenzar:\n"
                    + " Inicio (I)\n Configuración (C)\n Monitor (M)\n Arrancar (A)\n Detener (D)\n Reiniciar (R)\n Salir (S): ");

            input = kbd.next();
            input = input.toUpperCase();
            try {
                opcionesMenu opcion = opcionesMenu.valueOf(input);

                switch (opcion) {
                    case I:
                        System.out.println("\n.. ESTATUS DEL SISTEMA ... \n");

                        final StringBuilder dump = new StringBuilder();
                        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                        final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
                        for (ThreadInfo threadInfo : threadInfos) {
                            dump.append('"');
                            dump.append(threadInfo.getThreadName());
                            dump.append("\" ");
                            final Thread.State state = threadInfo.getThreadState();
                            dump.append("\n   java.lang.Thread.State: ");
                            dump.append(state);

                            final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
                            for (final StackTraceElement stackTraceElement : stackTraceElements) {
                                dump.append("\n        at ");
                                dump.append(stackTraceElement);
                            }
                            dump.append("\n\n");
                        }
                        System.out.println(dump);

                        break;
                    case C:
                        System.out.println("Iniciando Sub Menú de Configuración " + input);
                        displaySubmenuConfiguracion();
                        break;
                    case M:

                        displaySubmenuMonitor(server, monitoresPool, monitorHilos);

                        /*RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
                         //Get the ThreadFactory implementation to use
                         ThreadFactory threadFactory = Executors.defaultThreadFactory();
                         //creating the ThreadPoolExecutor
                         ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
                         //start the monitoring thread
                         MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
                         Thread monitorThread = new Thread(monitor);
                         monitorThread.start();
                         //submit work to the thread pool
                         for (int i = 0; i < 10; i++) {
                         executorPool.execute(new WorkerThread("cmd" + i));
                         }                        
                         Thread.sleep(30000);
                         //shut down the pool                        
                         //shut down the monitor thread
                         Thread.sleep(5000);
                         monitor.shutdown(); importante apagar el monitor y el pool */
                        break;
                    case A:
                        displaySubmenuArrancar(server);
                        break;
                    case D:
                        // System.out.println("Iniciando Sub Menú " + input);
                        //displaySubmenu();
                        break;
                    case R:
                        System.out.println("Iniciando Sub Menú " + input);
                        //displaySubmenu();
                        break;
                    case S:
                        System.out.println("Saliendo ... ");
                        System.exit(0);

                }
            } catch (IllegalArgumentException e) {
                System.out.println(input + " No es una opcíon válida.");
            }
        } while (!input.equals("S"));

    }

    /**
     * Displays and processes the submenu
     */
    public static void displaySubmenuConfiguracion() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        String input = "";
        String u;
        String p;
        String i;
        String l;

        do {
            try {
                
                System.out.print("\n Elija una Opción \n Configuración Inicial (I)\n Registrar (R)\n Modificar(M)\n Listar (L)\n Menú Principal (P):");
                input = kbd.next();
                input = input.toUpperCase();
                opcionesSubMenuConfiguracion opcion = opcionesSubMenuConfiguracion.valueOf(input);
                persistenciaAs400 ob = new persistenciaAs400();
                switch (opcion) {

                    case I:
                        System.out.print("\n**** Parámetros de Configuración Base  **** \n\n ADVERTENCIA:  Debe conocer interfaces, programas y usuarios de ejecución antes de continuar\n pues se ejecutará un reinicio de entidades., está seguro?? S/N: ");
                        input = kbd.next();
                        input = input.toUpperCase();
                        if (input.equals("S")) {
                            System.out.print("\n Ejecución en servidor remoto ? S/N: ");
                            input = kbd.next();
                            input = input.toUpperCase();                                                        
                            if (input.equals("S")) {                            
                            System.out.print("\n Ingrese IP o DNS de Interfaz: ");
                            i = kbd.next();
                            System.out.print("\n Librería para registro de parámetros: ");
                            l = kbd.next();
                            System.out.print("\n Ingrese usuario de Ejecución de Procesos: ");
                            u = kbd.next();
                            System.out.print("\n Password: ");
                            p = kbd.next();                            
                            System.out.print("\n Confirma la siguiente información IP/DNS: "+i.toUpperCase()+" libreria: "+l.toUpperCase()+" Usuario: "+u.toUpperCase()+" S/N: ");                            
                            input = kbd.next();
                            input = input.toUpperCase();                            
                            if (input.equals("S")) { 
                                try {
                                    ob.conexionInicial(true, i.toUpperCase(), l.toUpperCase(), u.toUpperCase(), p.toUpperCase());
                                } catch (Exception e) {
                                    System.out.print("\n Excepción en configuración : "+e+" \n retornando al menú anterior..");
                                }                                
                            }
                            else {
                                    System.out.print("\n Actualización Cancelada... retornando al menú anterior...");
                                    input="X";
                                }                                                                                       
                            }                       
                        if (input.equals("N")) {
                            
                            System.out.print("\n Ingrese IP o DNS de Interfaz: ");
                            i = kbd.next();
                            System.out.print("\n Base de datos para registro de parámetros: ");
                            l = kbd.next();
                            System.out.print("\n Ingrese usuario de Ejecución de Procesos: ");
                            u = kbd.next();
                            System.out.print("\n Password: ");
                            p = kbd.next();                                                                                   
                            System.out.print("\n Confirma la siguiente información IP/DNS: "+i.toUpperCase()+" Base de datos: "+l.toUpperCase()+" Usuario: "+u.toUpperCase()+" S/N: ");                            
                            input = kbd.next();
                            input = input.toUpperCase();                            
                            if (input.equals("S")) {                                                                                    
                             try {
                                        ob.conexionInicial(false, i.toUpperCase(), l.toUpperCase(), u.toUpperCase(), p.toUpperCase());
                                } catch (Exception e) {
                                    System.out.print("\n Excepción en configuración : "+e+" \n retornando al menú anterior..");
                                }                                                                                     
                            }
                            else {
                                    System.out.print("\n Actualización Cancelada... retornando al menú anterior...");
                                    input="X";
                                }    
                            
                        }
                         }
                        else {
                    System.out.print("\n Regresando al menú anterior...");
                }
                        break;
                    case R:
                        System.out.println("Esta opción es para manejar la persistencia en el Core utilizado " + input);
                        break;
                    case M:
                        System.out.println("Esta Opción es " + input);
                        break;
                    case L:
                        System.out.print("\n**** Parámetros de Configuración Base  ");
                                                 
                                try {
                                    ob.listadoProgramas();
                                } catch (Exception e) {
                                    System.out.print("\n Excepción en configuración : "+e+" \n retornando al menú anterior..");
                                }                                
                                
                        System.out.print("\n Regresando al menú anterior...");
                                        
                        break;
                    case P:
                        System.out.println("Regresando al Menú Principal\n ");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.print(input + "\n No es una opción válida.");
            }
        } while (!input.equals("P"));
    }

    public static void displaySubmenuArrancar(MultiThreadedServer[] server) {
        String input = "";
        do {
            try {

                System.out.print("\n **** Sub Menú de Arranque de Procesos *****  \n **** Elija una opción *****\n "
                        + "Individual (I)\n Masivo (M)\n Regresar (R) : ");
                input = kbd.next();
                input = input.toUpperCase();
                opcionesSubMenuArrancar opciones = opcionesSubMenuArrancar.valueOf(input);
                switch (opciones) {
                    case I:
                        System.out.print("\nIndíque el número de puerto que desea iniciar:");
                        input = kbd.next();
                        int port = Integer.parseInt(input.toUpperCase());
                        server[port] = new MultiThreadedServer(port);
                        new Thread(server[port]).start();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GestorProcesos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case M:
                        System.out.print("Esta opción es " + input);
                        break;
                    case R:
                        System.out.print("Regresando al Menú Principal... ");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.print(input + " No es una opción válida.");
            }
        } while (!input.equals("R"));
    }

    public static void displaySubmenuMonitor(MultiThreadedServer[] server, MyMonitorThread[] monitoresPool, Thread[] monitoresHilos) {
        String input = "";
        do {
            try {

                System.out.print("\n\n\n  **** Sub Menú de Monitoreo de Procesos *****  \n **** Elija una opción *****\n "
                        + "Individual (I)\n Masivo (M)\n Regresar (R) : ");

                input = kbd.next();

                input = input.toUpperCase();
                opcionesSubMenuMonitor opciones = opcionesSubMenuMonitor.valueOf(input);
                switch (opciones) {
                    case I:
                        System.out.print("\nIndíque el número de puerto que desea monitorear:");
                        input = kbd.next();
                        int port = Integer.parseInt(input.toUpperCase());
                        if (monitoresPool[port] == null) {
                            try {
                                monitoresPool[port] = new MyMonitorThread(server[port].executorPool, 3, port);
                                monitoresHilos[port] = new Thread(monitoresPool[port]);
                                monitoresHilos[port].start();
                            } catch (Exception e) {
                                System.out.print("\n No existe un servicio/pool de procesos activo para monitorear en este puerto...");
                            }


                        } else {
                            System.out.print("\n Ya existe un monitor de procesos iniciado/activo para este puerto...");
                        }
                        break;
                    case M:
                        for (int i = 0; i < 64000; i++) {
                            if (server[i] != null) {
                                monitoresPool[i] = new MyMonitorThread(server[i].executorPool, 3, i);
                                monitoresHilos[i] = new Thread(monitoresPool[i]);
                                monitoresHilos[i].start();
                            }
                        }
                        break;
                    case R:
                        System.out.print("\n Regresando al Menú Principal... ");
                        for (int i = 0; i < 64000; i++) {
                            if (server[i] != null) {
                                if (monitoresHilos[i] != null) {
                                    monitoresPool[i].shutdown();
                                    monitoresPool[i] = null;
                                    monitoresHilos[i] = null;
                                }
                            }
                        }
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.print(input + " No es una opción válida.");
            }
        } while (!input.equals("R"));
    }
}
