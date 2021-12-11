/**
 * 
 */
package es.ubu.asi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.asi.controller.LoginController;
import es.ubu.asi.database.Database;
import es.ubu.asi.model.FileBean;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class FileDAO {
	private static Database db;
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	static {
		db = Database.getInstance();
	}

	// methods
	/**
	 * Permite insertar un fichero en la base de datos
	 *
	 * @param e	datos del fichero
	 * @throws Exception
	 */
	public static void add(FileBean f) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = db.getConnection();
			String query = "INSERT INTO ficheros"
					+ "(titulo, ruta, idActividad, idUsuario) VALUES"
					+ "(?,?,?, ?)";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, f.getTitle());
			preparedStatement.setString(2, f.getRoute());
			preparedStatement.setLong(3, f.getActivity());
			preparedStatement.setLong(4, f.getUser());
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			try {
				conn.rollback();
			} catch (SQLException ex2) {
				throw new Exception("Rooling back query error", ex2);
			}
			throw new Exception("General SQL error", ex);
		} finally {
			close(preparedStatement);
			close(conn);
		}
	}
	
	/**
	 * Permite actualizar la información de un fichero
	 * NOTA: no se considera cambiar de actividad un fichero ni
	 * 		 el propietario del mismo.
	 *
	 * @param f	datos del fichero
	 * @throws Exception
	 */
	public static void update(FileBean f) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = db.getConnection();
			String query = "UPDATE ficheros"
					+ " SET titulo = ?, ruta = ?"
					+ " WHERE id = ?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, f.getTitle());
			preparedStatement.setString(2, f.getRoute());
			preparedStatement.setLong(3, f.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			try {
				conn.rollback();
			} catch (SQLException ex2) {
				throw new Exception("Rooling back query error", ex2);
			}
			throw new Exception("General SQL error", ex);
		} finally {
			close(preparedStatement);
			close(conn);
		}
	}
	
	/**
	 * Permite conectarse a la db y obtener un fichero por id
	 *
	 * @param file identificador de fichero
	 * @return información del fichero
	 * @throws Exception
	 */
	public static FileBean getFile(long fileID) throws Exception {
		String query = "SELECT * FROM ficheros"
				+ " WHERE id = ? LIMIT 1";
		FileBean file = null;
		
		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, fileID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("titulo");
                String route = resultSet.getString("ruta");
                long activity = resultSet.getLong("idActividad");
                long user = resultSet.getLong("idUsuario");
                file = new FileBean(fileID, title, route, activity, user);
            }
        
            return file;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}
	
	/**
	 * Permite conectarse a la db y obtener un fichero por ruta
	 *
	 * @param file identificador de fichero
	 * @return información del fichero
	 * @throws Exception
	 */
	public static FileBean getFileByRoute(String path) throws Exception {
		String query = "SELECT * FROM ficheros"
				+ " WHERE ruta = ? LIMIT 1";
		FileBean file = null;
		
		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, path);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String route = resultSet.getString("ruta");
                long activity = resultSet.getLong("idActividad");
                long user = resultSet.getLong("idUsuario");
                file = new FileBean(id, title, route, activity, user);
            }
        
            return file;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}

	/**
	 * Permite conectarse a la db y obtener el listado de ficheros almacenados
	 * para una actividad concreta
	 *
	 * @param actividad asociada a los ficheros
	 * @return listado de los ficheros
	 * @throws Exception
	 */
	public static List<FileBean> getFiles(long actividad) throws Exception {
		String query = "SELECT * FROM ficheros"
				+ " WHERE idActividad = ?";
		List<FileBean> list = new ArrayList<FileBean>();
		
		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, actividad);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String route = resultSet.getString("ruta");
                long user = resultSet.getLong("idUsuario");
                list.add(new FileBean(id, title, route, actividad, user));
            }
        
            return list;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}
	
	/**
	 * Permite eliminar un fichero de la base de datos
	 *
	 * @param fileID identificador del fichero
	 * @return indica si se ha borrado el registro
	 * @throws Exception
	 */
	public static boolean deleteFile(long fileID) throws Exception {
		String query = "DELETE FROM ficheros WHERE id = ?";

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, fileID);

            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}

	// handle connections
	private static void close(Statement st) throws Exception {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException ex) {
				throw new Exception("", ex);
			}
		}
	}

	private static void close(Connection con) throws Exception {
		if (con != null) {
			try {
				db.close(con);
			} catch (SQLException ex) {
				throw new Exception("", ex);
			}
		}
	}
}
