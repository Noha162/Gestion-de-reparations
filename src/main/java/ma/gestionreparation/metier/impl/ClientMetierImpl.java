package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.gestionreparation.dao.Client;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IClientMetier;

import java.util.List;

public class ClientMetierImpl implements IClientMetier {

    @Override
    public List<Client> listerClients() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> q = em.createQuery(
                    "SELECT c FROM Client c ORDER BY c.id DESC",
                    Client.class
            );
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Client creerClient(String nom, String prenom, String telephone) {
        if (nom == null || nom.isBlank()) throw new ValidationException("Nom obligatoire.");
        if (prenom == null || prenom.isBlank()) throw new ValidationException("Prénom obligatoire.");
        if (telephone == null || telephone.isBlank()) throw new ValidationException("Téléphone obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client c = new Client();
            c.setNom(nom.trim());
            c.setPrenom(prenom.trim());
            c.setTelephone(telephone.trim());

            em.persist(c);

            em.getTransaction().commit();
            return c;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void supprimerClient(Long clientId) {
        if (clientId == null) throw new ValidationException("Client id obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client c = em.find(Client.class, clientId);
            if (c == null) throw new ValidationException("Client introuvable.");

            Long nbReparations = em.createQuery(
                    "SELECT COUNT(r) FROM Reparation r WHERE r.client.id = :cid",
                    Long.class
            ).setParameter("cid", clientId).getSingleResult();

            if (nbReparations != null && nbReparations > 0) {
                throw new ValidationException("Suppression impossible : ce client a des réparations.");
            }

            em.remove(c);

            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
