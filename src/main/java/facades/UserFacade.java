package facades;

import entity.Currency;
import entity.ExchangeRates;
import entity.Role;
import security.IUserFacade;
import entity.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade {

    EntityManagerFactory emf;

    public UserFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public IUser getUserByUserId(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    /*
  Return the Roles if users could be authenticated, otherwise null
     */
    @Override
    public List<String> authenticateUser(String userName, String password) {
        IUser user = getUserByUserId(userName);
        try {
            return user != null && PasswordStorage.verifyPassword(password, user.getPassword()) ? user.getRolesAsStrings() : null;
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void addUser(String username, String password) {
        EntityManager em = getEntityManager();
        try {
            User newUser = new User(username, password);
            newUser.addRole(new Role("User"));
            em.getTransaction().begin();
            em.persist(newUser);
            em.getTransaction().commit();
        } catch (PasswordStorage.CannotPerformOperationException ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = getEntityManager();
        try{
            TypedQuery<User> query = em.createQuery("SELECT u FROM SEED_USER u", User.class);
            return query.getResultList();
            } finally {
            em.close();
        }
    }

    public void addExchangeRates(ExchangeRates exRate) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(exRate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
    }

    @Override

    public User deleteUser(String id) {
        EntityManager em = getEntityManager();
        try{
            User user = em.find(User.class, id);
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return user;
            } finally {
            em.close();
        }
    }

    public ExchangeRates getExhangeRates(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExchangeRates.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public String getRateByCode(String code) {
        EntityManager em = getEntityManager();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = df.format(cal.getTime());
        try {
            ExchangeRates rates = em.find(ExchangeRates.class, date);
            for (Currency currency : rates.getCurrency()) {
                if (currency.getCode().equalsIgnoreCase(code)) {
                    return currency.getRate();
                }
            }
        } finally {
            em.close();
        }
        return null;
    }

}
