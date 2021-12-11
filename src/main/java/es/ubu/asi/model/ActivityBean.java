/**
 * 
 */
package es.ubu.asi.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.ubu.asi.Globals;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class ActivityBean {
	// attributes
	private long id;
	private String title;
	private String description;
	private String suggestions;
	private String teachers;
	private String days;
	private String schedule;
	private long participations; // variable utilizada para simplificar las comprobaciones de participacion
	private Date dateStart;
	private Date dateEnd;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	// constructors
	public ActivityBean() {
		super();
	}

	public ActivityBean(String title, String description, String suggestions, String teachers, String days, String schedule,
			Date dateStart, Date dateEnd) {
		super();
		this.title = title;
		this.description = description;
		this.suggestions = suggestions;
		this.teachers = teachers;
		this.days = days;
		this.schedule = schedule;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public ActivityBean(long id, String title, String description, String suggestions, String teachers, String days,
			String schedule, Date dateStart, Date dateEnd) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.suggestions = suggestions;
		this.teachers = teachers;
		this.days = days;
		this.schedule = schedule;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	// getters & setters
	public long getId() {
		return id;
	}
	public String getIdStr() {
		return String.valueOf(id);
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	public String getTeachers() {
		return teachers;
	}
	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDateStartString() {
		return dateFormat.format(dateStart);
	}
	public String getDateEndString() {
		return dateFormat.format(dateEnd);
	}
	
	public long getParticipations() {
		return participations;
	}

	public void setParticipations(long participations) {
		this.participations = participations;
	}
	
	// methods
	public String getScheduleStart () {
		String[] aux;		
		try {
			aux = this.schedule.split("-");
		} catch (Exception e) {
			aux = null;
		}
		return (aux != null && aux.length > 0) ? aux[0] : "";
	}
	
	public String getScheduleEnd () {
		String[] aux;		
		try {
			aux = this.schedule.split("-");
		} catch (Exception e) {
			aux = null;
		}
		return (aux != null && aux.length > 1) ? aux[1] : "";
	}
	
	/**
	 * Función para saber si se permiten más participaciones
	 */
	public boolean allowParticipate() {
		return this.participations < Globals.MAX_PARTICIPANTES && !this.activityEnded();
	}
	

	/**
	 * Función para indicar si una actividad ha finalizado
	 */
	public boolean activityEnded() {
		return this.getDateEnd().before(new Date());
	}

	@Override
    public String toString() {
		return "Activity{" + "id=" + id + ", titulo=" + title + ", descripcion=" + description + ", docentes=" + teachers + ", días=" + days + ", horario=" + schedule + ", inicio=" + dateFormat.format(dateStart) + ", fin=" + dateFormat.format(dateEnd) + '}';
    }
}

