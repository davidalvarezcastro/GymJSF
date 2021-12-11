package es.ubu.asi.model;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class UserBean {
	// attributes
	private long id;
	private String login;
	private String password;
	private String profile;
	
	// constructors	
	public UserBean() {
		this.login = "guest";
		this.password = "";
		this.profile = "guest";
	}
	
	public UserBean(String login, String password, String profile) {
		super();
		this.login = login;
		this.password = password;
		this.profile = profile;
	}

	public UserBean(long id, String login, String password, String profile) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.profile = profile;
	}

	// getters & setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}

	// methods
	@Override
    public String toString() {
        return "User{" + "id=" + id + ", login=" + login + ", password=" + password + ", perfil_acceso=" + profile + '}';
    }
}
