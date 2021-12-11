package es.ubu.asi.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import es.ubu.asi.dao.ParticipationDAO;
import es.ubu.asi.model.ParticipationBean;
import es.ubu.asi.utils.Properties;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * Vote Controller
 *
 * 	se podría disponer de un controlador de Participaciones y gestionar
 *  todo de manera centralizada, pero se ha optado por esta opción para
 *  simplificar y separar las distintas "acciones".
 */
@ManagedBean(name="voteController")
@RequestScoped
public class VoteController implements Serializable{

	private static final long serialVersionUID = 1L;

	private String msg;
	private String error;
	private Properties p = new Properties();
	
	private Integer vote;
	private String comments;
	
	private ParticipationBean participation = null; // es mejor tener la instancia completa recuperada de la base de datos

	@ManagedProperty(value="#{activityController}")
    private ActivityController activityController;  // propiedad inyectada para acceder al controlador de la actividad
	@ManagedProperty(value="#{loginController}")
    private LoginController loginController;		// propiedad inyectada para acceder al controlador de login (información del usuario logueado)

	// contructor
	public VoteController() {
		super();
	}
	
	@PostConstruct
    public void init(){
         // obtenemos la información de la participacion
		try {
			this.participation = ParticipationDAO.getParticipationByUserActivity(
					this.activityController.getActivity().getId(),
					this.loginController.getId()
			);

			// actualizamos los valores de la votación con los de la base de datos
			this.vote = this.participation.getVote();
			this.comments = this.participation.getComments();
		} catch (Exception e) {}
    }

	// getter & setters
	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSuccess() {
		return msg;
	}
	
	public String getErrors() {
		return error;
	}
	
	public ActivityController getActivityController() {
		return activityController;
	}

	public void setActivityController(ActivityController activityController) {
		this.activityController = activityController;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	// methods
	/**
	 * comprueba si se permite al usuario votar en una actividad (ActivityController)
	 * @param informacion de actividad
	 * @param usuario
	 * @return
	 */
	public boolean allowVoting() {
		try {
			return this.activityController.allowVoting(
					this.activityController.getActivity(),
					this.loginController.getId()
			);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Permite puntuar una actividad por parte de un usuario
	 *
	 * @return jsf
	 */
	public String voting() {
		if(vote < 0) {
			error = p.getMsg("error_votar_voto");
		} else {
			try {
				this.participation.setVote(vote);
				if (!comments.isEmpty()) {
					this.participation.setComments(comments);					
				}
				ParticipationDAO.update(this.participation);
			} catch (Exception e) {
				error = p.getMsg("error_votar");
			}
		}

		if (error != null && !error.isEmpty()) {
			return "main?faces-redirect";
		} else {
			msg = p.getMsg("success_puntuar");
		}
		return "activity?faces-redirect";
    }
}
