/**
 * 
 */
package es.ubu.asi.dao;

import java.sql.Connection;
import java.sql.Date;
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
import es.ubu.asi.model.ActivityBean;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class ActivityDAO {
	private static Database db;
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	static {
		db = Database.getInstance();
	}

	// methods
	/**
	 * Permite insertar una actividad en la base de datos
	 *
	 * @param e	datos de la actividad
	 * @return identificador de la actividad insertada
	 * @throws Exception
	 */
	public static long add(ActivityBean f) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		long idValue = -1;

		try {
			conn = db.getConnection();
			String query = "INSERT INTO actividades"
					+ "(titulo, descripcion, recomendaciones, docentes, dias, horario, fechaInicio, fechaFin) VALUES"
					+ "(?,?,?,?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // obtener el id de la actividad creada
			preparedStatement.setString(1, f.getTitle());
			preparedStatement.setString(2, f.getDescription());
			preparedStatement.setString(3, f.getSuggestions());
			preparedStatement.setString(4, f.getTeachers());
			preparedStatement.setString(5, f.getDays());
			preparedStatement.setString(6, f.getSchedule());
			preparedStatement.setDate(7, new Date(f.getDateStart().getTime()));
			preparedStatement.setDate(8, new Date(f.getDateEnd().getTime()));
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
	 * Permite actualizar la información de una actividad en la base de datos
	 *
	 * @param e	datos de la actividad
	 * @throws Exception
	 */
	public static void update(ActivityBean f) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = db.getConnection();
			String query = "UPDATE actividades"
					+ " SET titulo = ?, descripcion = ?, recomendaciones = ?, docentes = ?, dias = ?, horario = ?, fechaInicio = ?, fechaFin = ?"
					+ " WHERE id = ?";
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, f.getTitle());
			preparedStatement.setString(2, f.getDescription());
			preparedStatement.setString(3, f.getSuggestions());
			preparedStatement.setString(4, f.getTeachers());
			preparedStatement.setString(5, f.getDays());
			preparedStatement.setString(6, f.getSchedule());
			preparedStatement.setDate(7, new Date(f.getDateStart().getTime()));
			preparedStatement.setDate(8, new Date(f.getDateStart().getTime()));
			preparedStatement.setLong(9, f.getId());
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
	 * Permite conectarse a la db y obtener el listado de actividades almacenadas
	 *
	 * @return listado de las actividades
	 * @throws Exception
	 */
	public static List<ActivityBean> getActivities() throws Exception {
		String query = "SELECT * FROM actividades";
		List<ActivityBean> list = new ArrayList<ActivityBean>();

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String description = resultSet.getString("descripcion");
                String suggestions = resultSet.getString("recomendaciones");
                String teachers = resultSet.getString("docentes");
                String days = resultSet.getString("dias");
                String schedule = resultSet.getString("horario");
                java.util.Date dateS = resultSet.getDate("fechaInicio");
                java.util.Date dateE = resultSet.getDate("fechaFin");
                ActivityBean a = new ActivityBean(id, title, description, suggestions, teachers, days, schedule, dateS, dateE);
                // obtenemos el número de participaciones (se podría haber hecho con un INNER JOIN o una subquery con COUNT)
                try {
                	a.setParticipations(ParticipationDAO.getParticipationsByActivity(id).size());
				} catch (Exception e) {}
                list.add(a);
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
	 * Permite conectarse a la db y obtener el listado de actividades
	 * en las que un usuario ha/está participando.
	 *
	 * @param identificador de usuario
	 * @return listado de las actividades
	 * @throws Exception
	 */
	public static List<ActivityBean> getActivitiesUser(long user) throws Exception {
		String query = "SELECT * FROM actividades AS a "
				+ " INNER JOIN participaciones AS p ON p.idActividad = a.id "
				+ " WHERE idUsuario= ?";
		List<ActivityBean> list = new ArrayList<ActivityBean>();

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, user);;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String description = resultSet.getString("descripcion");
                String suggestions = resultSet.getString("recomendaciones");
                String teachers = resultSet.getString("docentes");
                String days = resultSet.getString("dias");
                String schedule = resultSet.getString("horario");
                java.util.Date dateS = resultSet.getDate("fechaInicio");
                java.util.Date dateE = resultSet.getDate("fechaFin");
                ActivityBean a = new ActivityBean(id, title, description, suggestions, teachers, days, schedule, dateS, dateE);
                // obtenemos el número de participaciones (se podría haber hecho con un INNER JOIN o una subquery con COUNT)
                try {
                	a.setParticipations(ParticipationDAO.getParticipationsByActivity(id).size());
				} catch (Exception e) {}
                list.add(a);
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
	 * Permite conectarse a la db y obtener el listado de actividades
	 * en las que un usuario no está participando.
	 *
	 * @param identificador de usuario
	 * @return listado de las actividades
	 * @throws Exception
	 */
	public static List<ActivityBean> getActivitiesNoParticipating(long user) throws Exception {
		String query = "SELECT * FROM actividades "
				+ " WHERE id NOT IN "
				+ "  (SELECT idActividad FROM participaciones WHERE idUsuario = ?);";
		List<ActivityBean> list = new ArrayList<ActivityBean>();

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setLong(1, user);;
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String description = resultSet.getString("descripcion");
                String suggestions = resultSet.getString("recomendaciones");
                String teachers = resultSet.getString("docentes");
                String days = resultSet.getString("dias");
                String schedule = resultSet.getString("horario");
                java.util.Date dateS = resultSet.getDate("fechaInicio");
                java.util.Date dateE = resultSet.getDate("fechaFin");
                ActivityBean a = new ActivityBean(id, title, description, suggestions, teachers, days, schedule, dateS, dateE);
                // obtenemos el número de participaciones (se podría haber hecho con un INNER JOIN o una subquery con COUNT)
                try {
                	a.setParticipations(ParticipationDAO.getParticipationsByActivity(id).size());
				} catch (Exception e) {}
                list.add(a);
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
	 * Permite conectarse a la db y obtener información de la actividad
	 *
	 * @param activity identificador de la actividad
	 * @return información de la actividad
	 * @throws Exception
	 */
	public static ActivityBean getActivity(String activity) throws Exception {
		String query = "SELECT * FROM actividades WHERE id = ? LIMIT 1";
		ActivityBean a = null;

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, activity);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("titulo");
                String description = resultSet.getString("descripcion");
                String suggestions = resultSet.getString("recomendaciones");
                String teachers = resultSet.getString("docentes");
                String days = resultSet.getString("dias");
                String schedule = resultSet.getString("horario");
                java.util.Date dateS = resultSet.getDate("fechaInicio");
                java.util.Date dateE = resultSet.getDate("fechaFin");
                a = new ActivityBean(id, title, description, suggestions, teachers, days, schedule, dateS, dateE);
            }
        
            return a;
        } catch (SQLException e) {
        	logger.error(e.getMessage());
        	throw new Exception("General SQL error", e);
        } catch (Exception e) {
        	throw new Exception(e);
        }
	}

	/**
	 * Permite eliminar una actividad de la base de datos
	 *
	 * @param activity identificador de la actividad
	 * @return indica si se ha borrado el registro
	 * @throws Exception
	 */
	public static boolean deleteActivity(String activity) throws Exception {
		String query = "DELETE FROM actividades WHERE id = ?";

		try (Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, activity);

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
