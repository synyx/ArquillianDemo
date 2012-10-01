
package org.synyx.rand.arquilliandronedemo.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    /**
     * Finder via User email.
     * 
     * @param email
     * @return User
     */
    public User findByEmail(String email) {
        Query finderQuery = em.createNamedQuery(User.FIND_BY_EMAIL);
        finderQuery.setParameter("email", email);
        User user = (User) finderQuery.getSingleResult();
        return user;
    }

    public UserFacade() {
        super(User.class);
    }

}
