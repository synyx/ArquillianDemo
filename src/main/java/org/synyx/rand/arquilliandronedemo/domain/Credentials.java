
package org.synyx.rand.arquilliandronedemo.domain;

import java.io.Serializable;
import javax.enterprise.inject.Model;

/**
 *
 * @author Joachim Arrasz <arrasz@synyx.de>
 */
@Model
public class Credentials implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
