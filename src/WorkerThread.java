/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos.valderrama
 */
public class WorkerThread implements Runnable {

    private String command;
    private Socket socketCliente;

    public WorkerThread(Socket socketCliente, String s) {
        this.socketCliente = socketCliente;
        this.command = s;
    }

    @Override
    public void run() {
              
       // System.out.println("Inicio "+Thread.currentThread().getName()+" Puerto " + socketCliente.getLocalPort() + " rafaga entrada " + this.command+" "+System.currentTimeMillis());
        //t.setName(var + " puerto:" + socketCliente.getLocalPort() + " petición " + this.command);
        try {
            try {
                processCommand(this.command);
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
       // System.out.println("Fin "+Thread.currentThread().getName()+" Puerto " + socketCliente.getLocalPort() + " rafaga entrada " + this.command+" "+System.currentTimeMillis());
    }

    private void processCommand(String rafaga) throws IOException, InterruptedException {

        PrintStream output = new PrintStream(socketCliente.getOutputStream());
        String strOutput = "recibido desde " + this.command;                      
        CoreBancarioCall ob = new CoreBancarioCall();
        try {
           strOutput= ob.enviarDatos(rafaga);
        } catch (Throwable ex) {
            Logger.getLogger(GestorProcesos.class.getName()).log(Level.SEVERE, null, ex);
        }

        output.flush();//vacia contenido
        output.println(" respuesta al cliente " + strOutput);
        output.close();
        //cierra conexion
        socketCliente.close();

    }

    @Override
    public String toString() {
        return this.command;
    }
}