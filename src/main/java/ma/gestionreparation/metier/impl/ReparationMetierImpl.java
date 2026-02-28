package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.*;
import ma.gestionreparation.dao.enums.EtatAppareil;
import ma.gestionreparation.dao.enums.StatutReparation;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IReparationMetier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



public class ReparationMetierImpl implements IReparationMetier {

    @Override
    public Reparation creerReparationComplete(
            Long reparateurId,
            Long boutiqueId,
            String clientNom,
            String clientPrenom,
            String clientTel,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage,
            String descriptionPanne,
            double cout,
            String commentaire
    ) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            User reparateur = em.find(User.class, reparateurId);
            Boutique boutique = em.find(Boutique.class, boutiqueId);

            if (reparateur == null || boutique == null) {
                throw new RuntimeException("Réparateur ou boutique introuvable");
            }

            // CLIENT (créé à la volée)
            Client client = new Client();
            client.setNom(clientNom);
            client.setPrenom(clientPrenom);
            client.setTelephone(clientTel);
            em.persist(client);

            // APPAREIL (créé à la volée)
            Appareil appareil = new Appareil();
            appareil.setType(type);
            appareil.setMarque(marque);
            appareil.setModele(modele);
            appareil.setRam(ram);
            appareil.setStockage(stockage);
            appareil.setClient(client);
            em.persist(appareil);

            // RÉPARATION
            Reparation r = new Reparation();
            r.setClient(client);
            r.setReparateur(reparateur);
            r.setBoutique(boutique);
            r.setDescriptionPanne(descriptionPanne);
            r.setDateCreation(LocalDateTime.now());
            r.setStatut(StatutReparation.EN_COURS);
            r.setCoutTotal(cout);
            r.setCodeSuivi(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            em.persist(r);

            // LIGNE
            LigneReparation lr = new LigneReparation();
            lr.setReparation(r);
            lr.setAppareil(appareil);
            lr.setEtatAppareil(EtatAppareil.EN_COURS);
            lr.setCoutAppareil(cout);
            lr.setCommentaire(commentaire);
            em.persist(lr);

            em.getTransaction().commit();
            return r;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Reparation> listerPourReparateur(Long reparateurId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT r FROM Reparation r " +
                    "LEFT JOIN FETCH r.client " +
                    "LEFT JOIN FETCH r.boutique " +
                    "LEFT JOIN FETCH r.transactions " +
                    "WHERE r.reparateur.id = :id",
                    Reparation.class
            )
            .setParameter("id", reparateurId)
            .getResultList();
        } finally {
            em.close();
        }
    }
    
    public Reparation findByIdAvecTransactions(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT r FROM Reparation r " +
                    "LEFT JOIN FETCH r.transactions " +
                    "WHERE r.id = :id",
                    Reparation.class
            )
            .setParameter("id", id)
            .getSingleResult();
        } finally {
            em.close();
        }
    }




}
