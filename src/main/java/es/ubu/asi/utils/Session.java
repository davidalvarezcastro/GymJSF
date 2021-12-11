/**
 * 
 */
package es.ubu.asi.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class Session {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }
    
    public static HttpSession createSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
    }

    public static String getUser() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        return session.getAttribute("user").toString();
    }
}
