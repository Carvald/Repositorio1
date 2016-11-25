/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author carlos.valderrama
 */
import java.util.concurrent.ThreadPoolExecutor;

public class MyMonitorThread implements Runnable
{
    private ThreadPoolExecutor executor;
    private int seconds;
    private int port;
    private boolean run=true;

    public MyMonitorThread(ThreadPoolExecutor executor, int delay,int port)
    {
        this.executor = executor;
        this.seconds=delay;
        this.port=port;
    }
    public void shutdown(){
        this.run=false;
    }
    @Override
    public void run()
    {
        while(run){
                System.out.println(
                    String.format("[Monitor procesos puerto:"+this.port+" [%d/%d] Activos: %d, Completados: %d, Tarea: %d, isShutdown: %s, estánTerminados: %s",
                        
                        this.executor.getPoolSize(),
                        this.executor.getCorePoolSize(),
                        this.executor.getActiveCount(),
                        this.executor.getCompletedTaskCount(),
                        this.executor.getTaskCount(),
                        this.executor.isShutdown(),
                        this.executor.isTerminated()
                        ));
                try {
                    Thread.sleep(seconds*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
            
    }
}
