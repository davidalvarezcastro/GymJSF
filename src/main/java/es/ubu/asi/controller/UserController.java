package es.ubu.asi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import es.ubu.asi.dao.UserDAO;
import es.ubu.asi.model.UserBean;
import es.ubu.asi.utils.Properties;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * User Controller
 */
@Named
@RequestScoped
public class UserController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String user;
	private String password;
	private String profile;
	private List<String> profiles;
	private String msg;
	private String error;
	private Properties p = new Properties();
	
	// contructor
	public UserController() {
		super();
		profiles = new ArrayList<>();
		profiles.add(p.getMsg("profile_socio"));
		profiles.add(p.getMsg("profile_admin"));
		profiles.add(p.getMsg("profile_profesor"));
	}

	// getter & setters
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getProfiles() {
		return profiles;
	}
	
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getErrors() {
		return error;
	}
	
	public String getSuccess() {
		return msg;
	}
	
	// methods
	/**
	 * Registrar un nuevo usuario en la app
	 *
	 * @return jsf
	 */
	public String add() {
		if(user.equals("") || password.equals("")) {
			error = p.getMsg("error_registrar_campos");
		} else {
			try {
				UserBean u = new UserBean(user, password, profile);
				UserDAO.add(u);
			} catch (Exception e) {
				error = p.getMsg("error_registrar_usuario");
			}
		}

		if (error != null && !error.isEmpty()) {
            return "registrar";
		} else {
			msg = p.getMsg("success_registrar_usuario");
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml?faces-redirect=true");
			} catch (Exception e) {}
            return "main";
		}	
    }
	
	/**
	 * Resetea los valores del controlador
	 *
	 * @return jsf
	 */
	public String reset() {
		user = null;
		password = null;
		msg = null;
		error = null;

		return "registrar";
	}
}
