package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.Appareil;
import ma.gestionreparation.dao.Client;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IAppareilMetier;

import java.util.List;

public class AppareilMetierImpl implements IAppareilMetier {

    @Override
    public List<Appareil> lister() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM Appareil a ORDER BY a.id DESC",
                    Appareil.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Appareil findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Appareil.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void creerPourClient(
            Long clientId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    ) {
        if (clientId == null) throw new ValidationException("Client obligatoire");
        if (modele == null || modele.isBlank())
            throw new ValidationException("Modèle obligatoire");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client client = em.find(Client.class, clientId);
            if (client == null)
                throw new ValidationException("Client introuvable");

            Appareil a = new Appareil();
            a.setType(type);
            a.setMarque(marque);
            a.setModele(modele);
            a.setRam(ram);
            a.setStockage(stockage);
            a.setClient(client);

            em.persist(a);
            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void modifier(
            Long appareilId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    ) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Appareil a = em.find(Appareil.class, appareilId);
            if (a == null)
                throw new ValidationException("Appareil introuvable");

            a.setType(type);
            a.setMarque(marque);
            a.setModele(modele);
            a.setRam(ram);
            a.setStockage(stockage);

            em.merge(a);
            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void supprimer(Long appareilId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Appareil a = em.find(Appareil.class, appareilId);
            if (a != null) em.remove(a);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
