package ma.gestionreparation.metier.impl.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RepositoryUtil {

    public static <T> T findById(EntityManager em, Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    public static <T> List<T> findAll(EntityManager em, Class<T> clazz) {
        String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e";
        TypedQuery<T> q = em.createQuery(jpql, clazz);
        return q.getResultList();
    }

    public static void persist(EntityManager em, Object entity) {
        em.persist(entity);
    }

    public static <T> T merge(EntityManager em, T entity) {
        return em.merge(entity);
    }

    public static void remove(EntityManager em, Object entity) {
        em.remove(entity);
    }
}
