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
import es.ubu.asi.model.ParticipationBean;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class ParticipationDAO {
	private static Database db;
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	static {
		db = Database.getInstance();
	}

	// methods
	/**
	 * Permite añadir una participación en la base de datos
	 *
	 * @param p datos de la participación
	 * @return identificador de la participación insertada
	 * @throws Exception
	 */
	public static long add(ParticipationBean p) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		long idValue = -1;

		try {
			conn = db.getConnection();
			String query = "INSERT INTO participaciones"
					+ "(idActividad, idUsuario, modoPago, sugerencia, votacion, comentarios) VALUES"
					+ "(?,?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // obtener el id de la participación añadida
			preparedStatement.setLong(1, p.getActivity());
			preparedStatement.setLong(2, p.getUser());
			preparedStatement.setString(3, p.getPayMethod());
			preparedStatement.setString(4, p.getSuggestions());
			preparedStatement.setInt(5, p.getVote());
			preparedStatement.setString(6, p.getComments());
			preparedStatement.executeUpdate();
			
			ResultSet rs = preparedStatement.getGeneratedKeys();

			if (rs.next()) {
				idValue = rs.getInt(1);
			}
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
		return idValue;
	}

	/**
	 * Permite actualizar la información de una participacion en la base de datos
	 *
	 * NOTA: no se permite modificar el usuario ni la actividad, no tendría sentido
	 * (serían una participación nueva en tal caso)
	 *
	 * @param p	datos de la participación
	 * @throws Exception
	 */
	public static void update(ParticipationBean p) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = db.getConnection();
			String query = "UPDATE participaciones"
					+ " SET modoPago = ?, sugerencia = ?, votacion = ?, comentarios = ?"
					+ " WHERE id = ?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, p.getPayMethod());
			preparedStatement.setString(2, p.getSuggestions());
			preparedStatement.setInt(3, p.getVote());
			preparedStatement.setString(4, p.getComments());
			preparedStatement.setLong(5, p.getId());
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
	 * Permite conectarse a la db y obtener el listado de participaciones por usuario
	 *
	 * @param user identificador de usuario
	 * @return listado de las participacion
	 * @throws Exception
	 */
	public static List<ParticipationBean> getParticipationsByUser(long user) throws Exception {
		String query = "SELECT * FROM participaciones WHERE idUsuario = ?";
		List<ParticipationBean> list = new ArrayList<ParticipationBean>();

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long activity = resultSet.getLong("idActividad");
                String payMethod = resultSet.getString("modoPago");
                String suggestions = resultSet.getString("sugerencia");
                int vote = resultSet.getInt("votacion");
                String comments = resultSet.getString("comentarios");
                list.add(new ParticipationBean(id, activity, user, payMethod, suggestions, vote, comments));
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
	 * Permite conectarse a la db y obtener el listado de participaciones por actividad
	 *
	 * @param user identificador de actividad
	 * @return listado de las participacion
	 * @throws Exception
	 */
	public static List<ParticipationBean> getParticipationsByActivity(long activity) throws Exception {
		String query = "SELECT * FROM participaciones WHERE idActividad = ?";
		List<ParticipationBean> list = new ArrayList<ParticipationBean>();

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, activity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long user = resultSet.getLong("idUsuario");
                String payMethod = resultSet.getString("modoPago");
                String suggestions = resultSet.getString("sugerencia");
                int vote = resultSet.getInt("votacion");
                String comments = resultSet.getString("comentarios");
                list.add(new ParticipationBean(id, activity, user, payMethod, suggestions, vote, comments));
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
	 * Permite conectarse a la db y obtener información de una participación por id
	 *
	 * @param participacion identificador de la participacion
	 * @return información de la participación
	 * @throws Exception
	 */
	public static ParticipationBean getParticipation(long participacion) throws Exception {
		String query = "SELECT * FROM participaciones WHERE id = ? LIMIT 1";
		ParticipationBean p = null;

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, participacion);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	long id = resultSet.getLong("id");
                long activity = resultSet.getLong("idActividad");
                long user = resultSet.getLong("idUsuario");
                String payMethod = resultSet.getString("modoPago");
                String suggestions = resultSet.getString("sugerencia");
                int vote = resultSet.getInt("votacion");
                String comments = resultSet.getString("comentarios");
                p = new ParticipationBean(id, activity, user, payMethod, suggestions, vote, comments);
            }
        
            return p;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}
	
	/**
	 * Permite conectarse a la db y obtener información de una participación por id de actividad y usuario
	 *
	 * @param acividad
	 * @param usuario
	 * @return información de la participación
	 * @throws Exception
	 */
	public static ParticipationBean getParticipationByUserActivity(long a, long u) throws Exception {
		String query = "SELECT * FROM participaciones WHERE idActividad = ? AND idUsuario = ? LIMIT 1";
		ParticipationBean p = null;

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, a);
			preparedStatement.setLong(2, u);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	long id = resultSet.getLong("id");
                long activity = resultSet.getLong("idActividad");
                long user = resultSet.getLong("idUsuario");
                String payMethod = resultSet.getString("modoPago");
                String suggestions = resultSet.getString("sugerencia");
                int vote = resultSet.getInt("votacion");
                String comments = resultSet.getString("comentarios");
                p = new ParticipationBean(id, activity, user, payMethod, suggestions, vote, comments);
            }
        
            return p;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}
	

	/**
	 * Permite eliminar una participación de la base de datos por id
	 *
	 * @param participacion identificador de la participacion
	 * @return indica si se ha borrado el registro
	 * @throws Exception
	 */
	public static boolean deleteParticipation(long participacion) throws Exception {
		String query = "DELETE FROM participaciones WHERE id = ?";

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, participacion);

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
