package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;

public class UserMetierImpl {

    public User login(String email, String motDePasse) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM User u " +
                            "LEFT JOIN FETCH u.boutique b " +         // ✅ force boutique
                            "LEFT JOIN FETCH b.proprietaire p " +     // ✅ force proprietaire si tu en as besoin ailleurs
                            "WHERE u.email = :e AND u.motDePasse = :m",
                    User.class
            ).setParameter("e", email)
             .setParameter("m", motDePasse)
             .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}
