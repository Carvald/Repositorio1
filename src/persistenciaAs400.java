/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import sun.security.util.Password;

/**
 *
 * @author carlos.valderrama
 */
public class persistenciaAs400 {

    boolean doAS400Connection = true;
    String url, driverClass;
    PreparedStatement createStmt, selectStmt, updateStmt, readStmt;
    Connection connection;
    private String[][] rows;
    String system;
    String library;
    String dbName;

    public void conexionInicial(boolean alojado, String i, String l, String u, String p) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        doAS400Connection = alojado;
        system = i;
        library = l;
        dbName = l;

        if (doAS400Connection) {
            url = "jdbc:as400://" + system + "/" + library;
            driverClass = "com.ibm.as400.access.AS400JDBCDriver";
        } else {
            url = "jdbc:db2:" + dbName;
            driverClass = "COM.ibm.db2.jdbc.app.DB2Driver";
        }

        Properties properties = new Properties();        
        properties.put("user", u);
        properties.put("password",p);        
        properties.put("prompt", "false");

        Class.forName(driverClass).newInstance();
        try {
            connection = DriverManager.getConnection(url,properties);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.out.print(e);
        }
        
        if (!doAS400Connection) {
            connection.setCatalog(u);
            System.out.print("\nconnectDb connected to: " + dbName);
        } else {
            connection.setTransactionIsolation(Connection.TRANSACTION_NONE);
            System.out.print("\nconnectDb connected to: " + system + "/" + library);
        }

        String tableName = "PARAMETROS1";

        try {

            createStmt = connection.prepareStatement("DROP TABLE " + tableName);
            createStmt.execute();
            connection.commit();

        } catch (Exception e) {
            System.out.print("\ndrop table " + tableName + " La tabla no existe " + e);
        }

        createStmt = connection.prepareStatement("CREATE TABLE " + tableName
                + " (id INTEGER NOT NULL PRIMARY KEY, system VARCHAR(30), library VARCHAR(16), dbname VARCHAR(30), "
                + " user VARCHAR(20), pass VARCHAR(25), alojado VARCHAR(5))");

        createStmt.executeUpdate();
        connection.commit();

        System.out.print("\ntable " + tableName + " created in " + dbName);

        createStmt = connection.prepareStatement("INSERT INTO "
                + tableName + " (id, system, library, dbname, "
                + " user, pass,alojado)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)");

        createStmt.setInt(1, Integer.parseInt("1"));

        createStmt.setString(2, i);

        createStmt.setString(3, l);

        createStmt.setString(4, l);

        createStmt.setString(5, u);

        createStmt.setString(6, p);

        createStmt.setString(7, "" + alojado);

        createStmt.execute();

        connection.commit();

        System.out.print("\nData de parametrización registrada exitosamente...");


        System.out.print("\nEliminando tabla de parametrización de programas...");

        try {
            createStmt = connection.prepareStatement("DROP TABLE programas");
            createStmt.execute();
            connection.commit();
        } catch (Exception e) {
            System.out.print("\ndrop table programas no existe " + e);
        }

        System.out.print("\nCreando tabla de parametrización de programas...");
        createStmt = connection.prepareStatement("CREATE TABLE programas "
                + " (port INTEGER NOT NULL PRIMARY KEY, program VARCHAR(30), ext VARCHAR(16), lib VARCHAR(30), "
                + "  des VARCHAR(20), inipool VARCHAR(5), maxpool VARCHAR(5), timekaliv VARCHAR(5), sizeq VARCHAR(5),estatus VARCHAR(5))");

        createStmt.executeUpdate();
        connection.commit();

        if (createStmt != null) {
            createStmt.close();
            createStmt = null;
        }

        if (selectStmt != null) {
            selectStmt.close();
            selectStmt = null;
        }

        if (updateStmt != null) {
            updateStmt.close();
            updateStmt = null;
        }

        if (readStmt != null) {
            readStmt.close();
            readStmt = null;
        }
        if (connection != null) {
            connection.commit();
            connection.close();
            connection = null;

        }

        System.out.print("\nFinalizado.. regresando al menú anterior..\n");

    }

    public void listadoProgramas() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        doAS400Connection = true;
        system = "10.112.2.11";
        library = "BANFOSIFOB";
        dbName = "BANFOSIFOB";

        if (doAS400Connection) {
            url = "jdbc:as400://" + system + "/" + library;
            driverClass = "com.ibm.as400.access.AS400JDBCDriver";
        } else {
            url = "jdbc:db2:" + dbName;
            driverClass = "COM.ibm.db2.jdbc.app.DB2Driver";
        }

        Properties properties = new Properties();        
        properties.put("user", "BANMMA00");
        properties.put("password","GUARENAS2");        
        properties.put("prompt", "false");

        Class.forName(driverClass).newInstance();
        try {
            connection = DriverManager.getConnection(url,properties);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.out.print(e);
        }
        
        if (!doAS400Connection) {
            connection.setCatalog("BANMMA00");
            System.out.print("\nconnectDb connected to: " + dbName);
        } else {
            connection.setTransactionIsolation(Connection.TRANSACTION_NONE);
            System.out.print("\nconnectDb connected to: " + system + "/" + library);
        }
                

        System.out.print("\nFinalizado.. regresando al menú anterior..\n");                        
        StringBuffer sb = new StringBuffer();

 sb.append("********* PROGRAMAS *****************  \n\n");       
 //    create a statement to read table

 readStmt = connection.prepareStatement("SELECT * FROM PROGRAMAS");

 ResultSet rs = readStmt.executeQuery();
 //    get resultTable and define headings

 ResultSetMetaData rsm = rs.getMetaData();

 int count = rsm.getColumnCount();

 sb.append("");

 for (int j = 1; j <= count; j++) {

     sb.append(rsm.getColumnLabel(j)).append("\t");

 }
 //    read table data by row

 while (rs.next()) {                               

     sb.append(" ");

     for (int k = 1; k <= count; k++) {

         sb.append(rs.getObject(k)).append("\t");

     }

 }             

 System.out.println(sb.toString() + " ");   
 
 if (selectStmt != null) {
            selectStmt.close();
            selectStmt = null;
        }

        if (updateStmt != null) {
            updateStmt.close();
            updateStmt = null;
        }

        if (readStmt != null) {
            readStmt.close();
            readStmt = null;
        }
        if (connection != null) {
            connection.commit();
            connection.close();
            connection = null;

        }

    }
    
}
