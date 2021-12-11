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
import es.ubu.asi.model.UserBean;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class UserDAO {

	private static Database db;
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	static {
		db = Database.getInstance();
	}

	// methods
	/**
	 * Permite insertar un usuario en la base de datos
	 *
	 * @param e	datos del usuario
	 * @throws Exception
	 */
	public static void add(UserBean e) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = db.getConnection();
			String query = "INSERT INTO usuarios"
					+ "(login, password, perfilAcceso) VALUES"
					+ "(?,?,?)";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, e.getLogin());
			preparedStatement.setString(2, e.getPassword());
			preparedStatement.setString(3, e.getProfile());
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
	 * Permite conectarse a la db y obtener el listado de usuarios
	 *
	 * @return listado de los usuarios
	 * @throws Exception
	 */
	public static List<UserBean> getUsers() throws Exception {
		String query = "SELECT * FROM usuarios";
		List<UserBean> list = new ArrayList<UserBean>();
		
		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String profile = resultSet.getString("perfilAcceso");
                list.add(new UserBean(id, login, password, profile));
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
	 * Permite obtener un usuario por credenciales
	 *
	 * @return información de un usuario
	 * @throws Exception
	 */
	public static UserBean getUser(String u, String p) throws Exception {
		String query = "SELECT * FROM usuarios WHERE login = ? AND password = ?";
		UserBean user = null;
		
		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, u);
			preparedStatement.setString(2, p);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String profile = resultSet.getString("perfilAcceso");
                user = new UserBean(id, login, password, profile);
            }
        
            return user;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}

	/**
	 * Comprueba las credenciales de login
	 *
	 * @param user
	 * @param password
	 * @return si es correcto o no
	 * @throws Exception
	 */
	public static boolean checkLogin(String user, String password) throws Exception {
		String query = "SELECT * FROM usuarios"
				+ " WHERE login = ? AND password = ?";

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, password);
			preparedStatement.executeQuery();
			ResultSet rs = preparedStatement.executeQuery();
            return rs.next() ? true : false;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error ", e);
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
