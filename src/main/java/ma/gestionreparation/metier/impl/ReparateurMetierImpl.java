package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.*;
import ma.gestionreparation.dao.enums.*;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IReparateurMetier;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReparateurMetierImpl implements IReparateurMetier {

    @Override
    public Client creerClient(String nom, String prenom, String telephone) {

        if (telephone == null || telephone.isBlank()) {
            throw new ValidationException("Téléphone obligatoire");
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client existing = em.createQuery(
                    "SELECT c FROM Client c WHERE c.telephone = :t",
                    Client.class
            ).setParameter("t", telephone.trim())
             .getResultStream()
             .findFirst()
             .orElse(null);

            if (existing != null) {
                existing.setNom(nom);
                existing.setPrenom(prenom);
                Client merged = em.merge(existing);
                em.getTransaction().commit();
                return merged;
            }

            Client c = new Client();
            c.setNom(nom);
            c.setPrenom(prenom);
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
    public Appareil creerAppareilPourClient(
            Long clientId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    ) {
        if (clientId == null) throw new ValidationException("Client obligatoire");
        if (modele == null || modele.isBlank()) throw new ValidationException("Modèle obligatoire");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client client = em.find(Client.class, clientId);
            if (client == null) throw new ValidationException("Client introuvable");

            Appareil a = new Appareil();
            a.setType(type);
            a.setMarque(marque);
            a.setModele(modele);
            a.setRam(ram);
            a.setStockage(stockage);
            a.setClient(client);

            em.persist(a);
            em.getTransaction().commit();
            return a;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Reparation creerReparation(
            String descriptionPanne,
            double coutTotal,
            User reparateur,
            Client client,
            Boutique boutique
    ) {
        if (reparateur == null) throw new ValidationException("Réparateur obligatoire");
        if (client == null) throw new ValidationException("Client obligatoire");
        if (boutique == null) throw new ValidationException("Boutique obligatoire");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User rep = em.find(User.class, reparateur.getId());
            Client cl = em.find(Client.class, client.getId());
            Boutique bt = em.find(Boutique.class, boutique.getId());

            Reparation r = new Reparation();
            r.setClient(cl);
            r.setReparateur(rep);
            r.setBoutique(bt);
            r.setDescriptionPanne(descriptionPanne);
            r.setDateCreation(LocalDateTime.now());
            r.setStatut(StatutReparation.EN_COURS);
            r.setCoutTotal(coutTotal);
            r.setCodeSuivi(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

            em.persist(r);
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
    public LigneReparation ajouterLigne(
            Reparation reparation,
            Appareil appareil,
            EtatAppareil etat,
            Double cout,
            String commentaire
    ) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Reparation rep = em.find(Reparation.class, reparation.getId());
            Appareil app = em.find(Appareil.class, appareil.getId());

            LigneReparation lr = new LigneReparation();
            lr.setReparation(rep);
            lr.setAppareil(app);
            lr.setEtatAppareil(etat);
            lr.setCoutAppareil(cout == null ? 0.0 : cout);
            lr.setCommentaire(commentaire);

            em.persist(lr);

            rep.setCoutTotal(rep.getCoutTotal() + lr.getCoutAppareil());
            em.merge(rep);

            em.getTransaction().commit();
            return lr;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Transaction enregistrerTransaction(
            String description,
            double montant,
            TypeOperation op,
            TypeCaisse caisse,
            User reparateur,
            Reparation reparation
    ) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User rep = em.find(User.class, reparateur.getId());
            Reparation repa = reparation == null ? null : em.find(Reparation.class, reparation.getId());

            Transaction t = new Transaction();
            t.setDate(LocalDateTime.now());
            t.setDescription(description);
            t.setMontant(montant);
            t.setTypeOperation(op);
            t.setTypeCaisse(caisse);
            t.setReparateur(rep);
            t.setReparation(repa);

            em.persist(t);
            em.getTransaction().commit();
            return t;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
