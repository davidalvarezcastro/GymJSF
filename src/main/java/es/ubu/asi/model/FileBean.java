package es.ubu.asi.model;


/**
 * @author david {dac1005@alu.ubu.es}
 */
public class FileBean {
	// attributes
	private long id;
	private String title;
	private String route;
	private long activity; // se podría poner la clase completa, pero por ahora no parece que vaya a hacer falta y simplifica de cierto modo la gestión
	private long user; // lo mismo que con la actividad

	// constructors
	public FileBean() {
		super();
	}

	public FileBean(String title, String route, long activity, long user) {
		super();
		this.title = title;
		this.route = route;
		this.activity = activity;
		this.user = user;
	}
	
	public FileBean(String title, String route, long activity) {
		super();
		this.title = title;
		this.route = route;
		this.activity = activity;
	}

	public FileBean(long id, String title, String route, long activity, long user) {
		super();
		this.id = id;
		this.title = title;
		this.route = route;
		this.activity = activity;
		this.user = user;
	}

	// getters & setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
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

	// methods
	@Override
    public String toString() {
        return "File{" + "id=" + id + ", title=" + title + ", route=" + route + ", activity=" + activity + ", user=" + user + '}';
    }
}

