
package org.synyx.rand.arquilliandronedemo.controller;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.synyx.rand.arquilliandronedemo.domain.Credentials;
import org.synyx.rand.arquilliandronedemo.domain.User;

/**
 *
 * @author Joachim Arrasz <arrasz@synyx.de>
 */
@Named
@SessionScoped
public class LoginController implements Serializable {


    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_MESSAGE = "Welcome";
    private static final String FAILURE_MESSAGE =
        "Incorrect username and password combination";

    private User currentUser;
    
    @Inject
    private Credentials credentials;
    
    public String login() {
        if ("demo".equals(credentials.getUsername()) && "demo".equals(credentials.getPassword())) {
            currentUser = new User();
            currentUser.setName("demo");
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(SUCCESS_MESSAGE));
            return "home.xhtml";
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, FAILURE_MESSAGE, FAILURE_MESSAGE));
        return null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    @Produces
    public User getCurrentUser() {
        return currentUser;
    }
}