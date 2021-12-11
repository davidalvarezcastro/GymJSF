package es.ubu.asi.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.asi.Globals;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class Database {
	
	private static Logger logger = LoggerFactory.getLogger(Database.class);
	private static Database database;
	private static DataSource ds;
	
	private Database() {
		try{
			Context contextoInicial = new InitialContext();
			ds = (DataSource) contextoInicial.lookup(Globals.JDBC_DATABASE);
			if (ds == null) {
				logger.error("No jdbc resource created...");
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			logger.error("No resource created");
		}		
	}
	
	public static Database getInstance() {
		if (database==null){
			database = new Database();
			return database;
		}
		else{
			return database;
		}		
	}

	public Connection getConnection() throws SQLException{
		return ds.getConnection();		
	}
	
	public ResultSet executeSQL(Connection conn, String sql) throws SQLException{
		Statement stmt = null;
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 0);
		ResultSet resultSet = stmt.executeQuery(sql);
		return resultSet;		
	}
	
	public void close(Connection con) throws SQLException{
		if (con!=null){
				System.err.println("Closing connection!");
				con.close();
		}
	}
	
}