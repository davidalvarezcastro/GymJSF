package es.ubu.asi.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import es.ubu.asi.dao.ActivityDAO;
import es.ubu.asi.dao.FileDAO;
import es.ubu.asi.dao.ParticipationDAO;
import es.ubu.asi.model.ActivityBean;
import es.ubu.asi.model.FileBean;
import es.ubu.asi.utils.Properties;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * Activity Controller
 */
@Named
@SessionScoped
public class ActivityController implements Serializable{

	private static final long serialVersionUID = 1L;

	private String msg;
	private String error;
	private ActivityBean activity; // actividad seleccionada por el usuario para visualizar, votar y/o participar
	private Properties p = new Properties();
	
	// contructor
	public ActivityController() {
		super();
	}

	// getter & setters
	public ActivityBean getActivity() {
		return activity;
	}

	public void setActivity(ActivityBean activity) {
		this.activity = activity;
	}
	
	public String getErrors() {
		return error;
	}

	public String getSuccess() {
		return msg;
	}
	
	// methods
	/**
	 * obtiene la información de una actividad
	 *
	 * @param identificador de actividad
	 * @return
	 */
	public void obtenerInfoActividad(String a) {
		// se obtiene la informacion de la actividad
		try {
			activity = ActivityDAO.getActivity(a);
		} catch (Exception e) {
			activity = null;
			error = String.format(p.getMsg("error_detalle_actividad_actividad"), a);
		}
	}
	
	/**
	 * Obtiene el listado de los ficheros de la actividad
	 *
	 * @return listado de ficheros
	 */
	public List<FileBean> getFilesActivity() {
		try {
			return FileDAO.getFiles(this.activity.getId());
		} catch (Exception e) {
			msg = p.getMsg("error_fallo_conexion_db");
			return null;
		}
	}

	/**
	 * Obtiene el listado de todas las actividades almacenadas
	 *
	 * @return listado de actividades
	 */
	public List<ActivityBean> getActivities() {
		try {
			return ActivityDAO.getActivities();
		} catch (Exception e) {
			msg = p.getMsg("error_fallo_conexion_db");
			return null;
		}
	}
	
	/**
	 * Obtiene el listado de todas las actividades en las que está apuntado un usuario
	 *
	 * @param usuario
	 * @return listado de actividades
	 */
	public List<ActivityBean> getActivitiesParticipate(long usuario) {
		try {
			return ActivityDAO.getActivitiesUser(usuario);
		} catch (Exception e) {
			msg = p.getMsg("error_fallo_conexion_db");
			return null;
		}
	}
	
	/**
	 * Obtiene el listado de todas las actividades en las que no está apuntado un usuario
	 *
	 * @param usuario
	 * @return listado de actividades
	 */
	public List<ActivityBean> getActivitiesNoParticipate(long usuario) {
		try {
			return ActivityDAO.getActivitiesNoParticipating(usuario);
		} catch (Exception e) {
			msg = p.getMsg("error_fallo_conexion_db");
			return null;
		}
	}

	/**
	 * comprueba si se permite al usuario participar en una actividad
	 * 	- no está/ha participado todavía
	 * 	- hay plazas disponibles
	 * 	- fecha de finalización anteriora hoy
	 *
	 * @param informacion de actividad
	 * @param usuario
	 * @return
	 */
	public boolean allowParticipate(ActivityBean activity, long user) {
		try {
			return !this.checkUserIsParticipating(activity.getId(), user) && activity.allowParticipate();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * comprueba si se permite al usuario votar en una actividad
	 * 	- ha participado todavía
	 * 	- actividad ya finalizada (restricción? dejar votar durante la actividad?)
	 *
	 * @param informacion de actividad
	 * @param usuario
	 * @return
	 */
	public boolean allowVoting(ActivityBean activity, long user) {
		try {
			return this.checkUserIsParticipating(activity.getId(), user) && activity.activityEnded();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * comprueba si el usuario ya se ha apuntado a la actividad
	 *
	 * @param identificador de actividad
	 * @param usuario
	 * @return
	 */
	public boolean checkUserIsParticipating(long activity, long user) {
		try {
			return ParticipationDAO.getParticipationByUserActivity(activity, user) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Redirige al usuario a la vista de participación
	 *
	 ** @param identificador de actividad
	 * @return jsf
	 */
	public String goToParticipateView(String a) {
		try {
			this.obtenerInfoActividad(a);
			// FacesContext.getCurrentInstance().getExternalContext().redirect("participate.xhtml?activity=" + a);
			return "participate";
		} catch (Exception e) {
			return "main";
		}
	}

	/**
	 * Redirige al usuario a la vista de detalle de actividad
	 *
	 * @param identificador de actividad
	 * @return jsf
	 */
	public String goToActivityView(String a) {
		try {
			this.obtenerInfoActividad(a);
			// FacesContext.getCurrentInstance().getExternalContext().redirect("activity.xhtml?activity=" + a);
			return "activity";
		} catch (Exception e) {
			return "main";
		}
	}
}
