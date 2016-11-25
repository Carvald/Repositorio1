

import java.lang.Thread.*;
import java.rmi.RemoteException;


import com.ibm.as400.access.*;

public class CoreBancarioCall {

    private AS400 system;

    /**
     * Método que envia una Cadena de Datos al Programa Manejador Devuelve una
     * cadena con el resultado
     *
     * @param pLibreria Nombre de la Libreria o Biblioteca
     * @param pTipoProg Tipo de Programa Nativo iSeires
     * @param pPrograma Nombre del Programa
     * @param pTrama Cadena de Caracteres
     * @throws RemoteException
     * @return String
     * @throws Throwable
     */
    public String enviarDatos(/*String pLibreria,
             String pTipoProg, String pPrograma,*/
             String pTrama
            
          ) throws Throwable {

        AS400 as400 = new AS400("10.112.2.11","BANMMA00","GUARENAS2");

        String sRpta = ""; 

        try {
            String c = "/QSYS.LIB/BANUSRLIB.LIB/TRCFGSOC.PGM";
            AS400Text txtCuenta = new AS400Text(4000);
            ProgramParameter[] parmList = new ProgramParameter[1];
            parmList[0] = new ProgramParameter(txtCuenta.toBytes(pTrama), 20);
            
            ProgramCall cmd = new ProgramCall(as400, c, parmList);
            
            try {

                if (cmd.run() != true) {                
                    AS400Message[] messagelist = cmd.getMessageList();
                    for (int i = 0; i < messagelist.length; ++i) {
                        System.out.println(messagelist[i]);
                        throw new Throwable("Error programa: " + messagelist[i]);
                    }
                } else {
                    AS400Text text = new AS400Text(350);
                    sRpta = (String) text.toObject(parmList[0].getOutputData());
                    sRpta=sRpta.trim();               
                    parmList = null;
                    cmd = null;
                }
                as400.disconnectService(AS400.COMMAND);

            } catch (Exception ex) {
                sRpta = ex.getMessage();
                ex.printStackTrace();
                throw ex;
            }
        } catch (Exception ex1) {
            sRpta = ex1.getMessage();
            ex1.printStackTrace();
            throw ex1;
        }

        return sRpta;
    }

    public void cerrarConexion() {
        if (this.system.isConnected()) {
            this.system.disconnectAllServices();
        }
    }
}