package es.ubu.asi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import es.ubu.asi.dao.ParticipationDAO;
import es.ubu.asi.model.ParticipationBean;
import es.ubu.asi.utils.Properties;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * Participate Controller
 */
@ManagedBean(name="participateController")
@RequestScoped
public class ParticipateController implements Serializable{

	private static final long serialVersionUID = 1L;

	private String msg;
	private String error;
	private Properties p = new Properties();
	
	private long activity;
	private long user;
	private String payMethod;
	private String suggestions;
	
	private List<String> payMethods;
	
	@ManagedProperty(value="#{activityController}")
    private ActivityController activityController;  // propiedad inyectada para acceder al controlador de la actividad
	@ManagedProperty(value="#{loginController}")
    private LoginController loginController;

	// contructor
	public ParticipateController() {
		super();
		payMethods = new ArrayList<>();
		payMethods.add(p.getMsg("payment_efectivo"));
		payMethods.add(p.getMsg("payment_domicializacion"));
		payMethods.add(p.getMsg("payment_tarjeta"));
	}

	// getter & setters
	public long getActivity() {
		return activity;
	}

	public void setActivity(long activity) {
		this.activity = activity;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	
	public List<String> getPaymentMethods() {
		return payMethods;
	}
	
	public String getErrors() {
		return error;
	}
	
	public String getSuccess() {
		return msg;
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
	 * Permite inscribirse a un usuario en una actividad
	 *
	 * @return jsf
	 */
	public String participate() {
		if(payMethod.equals("")) {
			error = p.getMsg("error_participar_campos");
		} else {
			try {
				activity = this.activityController.getActivity().getId();
				user = this.loginController.getId();
				ParticipationDAO.add(new ParticipationBean(activity, user, payMethod, suggestions));
			} catch (Exception e) {
				error = p.getMsg("error_participar");
			}
		}

		if (error != null && !error.isEmpty()) {
            return "participate";
		} else {
			msg = String.format(p.getMsg("success_inscribirse"), activity);
            return "main?faces-redirect";
		}
    }

	/**
	 * Resetea los valores del controlador
	 *
	 * @return jsf
	 */
	public String reset() {
		payMethod = null;
		suggestions = null;
		msg = null;
		error = null;

		return "participate";
	}
}
