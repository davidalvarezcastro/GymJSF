package es.ubu.asi.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import es.ubu.asi.dao.UserDAO;
import es.ubu.asi.model.UserBean;
import es.ubu.asi.utils.Properties;
import es.ubu.asi.utils.Session;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * Login Controller
 */
@Named
@SessionScoped
public class LoginController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long id;
	private String user;
	private String password;
	private UserBean u;
	private String msg;
	private Properties p = new Properties();
	
	// contructor
	public LoginController() {
		super();
	}

	// getter & setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
	
	public UserBean getU() {
		return u;
	}

	public void setU(UserBean u) {
		this.u = u;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	// methods
	public String getErrors() {
		return msg;
	}
	
	/**
	 * Devuelve si el usuario es de perfil administrador
	 */
	public boolean isAdmin() {
		try {
			return this.u.getProfile().equals(p.getMsg("profile_admin"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Comprueba las credenciales de usuario y redirige al home de la app
	 *
	 * @return jsf
	 */
	public String login() {
		boolean error = false;

		if(user.equals("") || password.equals("")) {
			msg = (user.equals("")) ? p.getMsg("error_login_usuario") : p.getMsg("error_login_password");
			error = true;
		} else {
			try {
				u = UserDAO.getUser(user, password);
				if (u == null){
					msg = p.getMsg("error_login");
					error = true;
				}
				
				this.id = u.getId();
			} catch (Exception e) {
				msg = p.getMsg("error_fallo_conexion_db");
				error = true;
			}
		}

		if (error) {
			// FacesContext.getCurrentInstance().addMessage("error_msg", new FacesMessage(msg));
            return "login";
		} else {
			HttpSession session = Session.createSession();
            session.setAttribute("user", user);
            session.setAttribute("id", this.id);
            return "main";
		}	
    }

	/**
	 * Elimina la sesión de usuario
	 *
	 * @return jsf
	 */
    public String logout() {
        HttpSession session = Session.getSession();
		String username = (String) session.getAttribute("user");
		
		if (!session.isNew() && username != null) {
			session.invalidate();
			return "login";
		} else {
			return "main";
		}
    }
}
