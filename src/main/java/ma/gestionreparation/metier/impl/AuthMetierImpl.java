package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.exception.AuthException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IAuthMetier;

import java.util.List;

public class AuthMetierImpl implements IAuthMetier {

    @Override
    public User login(String email, String motDePasse) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<User> users = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

            if (users.isEmpty()) {
                throw new AuthException("Email ou mot de passe incorrect.");
            }

            User u = users.get(0);
            if (!u.getMotDePasse().equals(motDePasse)) {
                throw new AuthException("Email ou mot de passe incorrect.");
            }
            return u;
        } finally {
            em.close();
        }
    }
}
