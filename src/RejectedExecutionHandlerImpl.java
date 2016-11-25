/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Rechazada Ejecución de tarea el pool tiene activos "+executor.getActiveCount()+" procesos simultaneos y La cola de espera de tamaño "+executor.getQueue().size()+" fue excedida" );
    }

}