
package org.synyx.rand.arquilliandronedemo.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.synyx.rand.arquilliandronedemo.domain.User;

/**
 *
 * @author Joachim Arrasz <arrasz@synyx.de>
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "org.synyx.rand_ArquillianDroneDemo_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

}
