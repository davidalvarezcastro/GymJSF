/**
 * 
 */
package es.ubu.asi.model;


/**
 * @author david {dac1005@alu.ubu.es}
 */
public class ParticipationBean {
	// attributes
	private long id;
	private long activity; // se podría poner la clase completa
	private long user; // lo mismo que con la actividad
	private String payMethod;
	private String suggestions;
	private int vote;
	private String comments;

	// constructors
	public ParticipationBean() {
		super();
	}
	
	public ParticipationBean(long activity, long user, String payMethod, String suggestions, int vote, String comments) {
		super();
		this.activity = activity;
		this.user = user;
		this.payMethod = payMethod;
		this.suggestions = suggestions;
		this.vote = vote;
		this.comments = comments;
	}
	
	public ParticipationBean(long activity, long user, String payMethod, String suggestions) {
		super();
		this.activity = activity;
		this.user = user;
		this.payMethod = payMethod;
		this.suggestions = suggestions;
	}

	public ParticipationBean(long id, long activity, long user, String payMethod, String suggestions, int vote,
			String comments) {
		super();
		this.id = id;
		this.activity = activity;
		this.user = user;
		this.payMethod = payMethod;
		this.suggestions = suggestions;
		this.vote = vote;
		this.comments = comments;
	}

	// getters & setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	// methods
	@Override
    public String toString() {
		return "Participation{" + "id=" + id + ", activity=" + activity + ", user=" + user + ", payMethod=" + payMethod + ", suggestions=" + suggestions + ", vote=" + vote + '}';
    }
}

