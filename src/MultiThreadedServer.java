/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadedServer implements Runnable {

    public int serverPort=-1;
    public ServerSocket serverSocket = null;
    public boolean isStopped = false;
    public Thread runningThread = null;
    public RejectedExecutionHandlerImpl rejectionHandler = null;
    public ThreadFactory threadFactory = null;
    public ThreadPoolExecutor executorPool = null;
    public MyMonitorThread monitor = null;

    
    public MultiThreadedServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        rejectionHandler =new RejectedExecutionHandlerImpl();
        threadFactory = Executors.defaultThreadFactory();   
        
        executorPool = new ThreadPoolExecutor(160, 320, 180, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(320), threadFactory, rejectionHandler);
        
        executorPool.allowCoreThreadTimeOut(true);
               
        while (!isStopped()) {
            
           Socket clientSocket = null;  
           
            try {
  
           /* while(true){*/
                //en espera de conexion, si existe la acepta
                clientSocket = serverSocket.accept();
                //Para leer lo que envie el cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //para imprimir datos de salida                
               // PrintStream output = new PrintStream(clientSocket.getOutputStream());
                //se lee peticion del cliente
                String request = input.readLine();  
                                                
                // System.out.println("Cliente> petición recibida [" + request +  "]");
                //se procesa la peticion y se espera resultado                
                this.executorPool.execute(new WorkerThread(clientSocket,request));  
                
               /* String strOutput ="recibido";                
                //Se imprime en consola "servidor"
                * 
                * 
                * 
                System.out.println("Servidor> Resultado de petición");                    
                System.out.println("Servidor> \"" + strOutput + "\"");
                //se imprime en cliente
                output.flush();//vacia contenido
                output.println(strOutput);                
                //cierra conexion
                clientSocket.close();*/
            //}    

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("\n Servidor dejó de Escuchar(detenido) \n");
                    return;
                }
                throw new RuntimeException(
                        "Error aceptando conexión cliente", e);
            }
            
             //Get the ThreadFactory implementation to use
             
             //creating the ThreadPoolExecutor
             
             //start the monitoring thread
             /*MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
             Thread monitorThread = new Thread(monitor);
             monitorThread.start();*/
             //submit work to the thread pool
             /*for (int i = 0; i < 10; i++) {
             executorPool.execute(new WorkerThread("cmd" + i));                
             }*/
             /*try {
             Thread.sleep(30000);
             } catch (InterruptedException ex) {
             Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
             }*/
             //shut down the pool
             
            /* try {
             //shut down the monitor thread
             Thread.sleep(5000);
             } catch (InterruptedException ex) {
             Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
             }*/
             //monitor.shutdown();
        }
        System.out.println("\n Servidor Detenido .\n");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.executorPool.shutdown();
            this.serverSocket.close();
            System.out.println("\n Servidor Detenido \n");
        } catch (IOException e) {
            throw new RuntimeException("\n Error Deteniendo Servidor \n ", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            
            System.out.println("\n Servidor Iniciado en puerto: " + this.serverPort+" \n ");
        } catch (IOException e) {
            throw new RuntimeException("\n Verifique que el puerto "+this.serverPort+" no esté siendo usado \n", e);
        }
    }
}