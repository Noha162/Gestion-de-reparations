package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.Boutique;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.dao.enums.Role;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IProprietaireMetier;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import ma.gestionreparation.metier.dto.StatsProprietaireDTO;
import ma.gestionreparation.metier.dto.TopReparateurDTO;


public class ProprietaireMetierImpl implements IProprietaireMetier {

    @Override
    public Boutique creerBoutique(String nom, String adresse, String numeroPatente, User proprietaire) {

        if (proprietaire == null || proprietaire.getRole() != Role.PROPRIETAIRE) {
            throw new ValidationException("Le propriétaire est invalide.");
        }
        if (proprietaire.getId() == null) {
            throw new ValidationException("Propriétaire non persisté (id null).");
        }

        if (nom == null || nom.isBlank()) throw new ValidationException("Nom boutique obligatoire.");
        if (adresse == null || adresse.isBlank()) throw new ValidationException("Adresse boutique obligatoire.");
        if (numeroPatente == null || numeroPatente.isBlank()) throw new ValidationException("Numéro patente obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User propManaged = em.find(User.class, proprietaire.getId());
            if (propManaged == null) {
                throw new ValidationException("Propriétaire introuvable en base.");
            }

            Boutique b = new Boutique(nom, adresse, numeroPatente, propManaged);
            em.persist(b);

            em.getTransaction().commit();
            return b;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new ValidationException("Création boutique impossible (patente déjà utilisée ou données invalides).");
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<Boutique> listerBoutiquesDuProprietaire(Long proprietaireId) {
        if (proprietaireId == null) throw new ValidationException("Propriétaire id obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Boutique> q = em.createQuery(
                    "SELECT b FROM Boutique b WHERE b.proprietaire.id = :pid ORDER BY b.id DESC",
                    Boutique.class
            );
            q.setParameter("pid", proprietaireId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void supprimerBoutique(Long boutiqueId) {
        if (boutiqueId == null) throw new ValidationException("Boutique id obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Boutique b = em.find(Boutique.class, boutiqueId);
            if (b == null) throw new ValidationException("Boutique introuvable.");

            // Option simple: refuser suppression si des réparateurs ou réparations existent
            Long nbRep = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.boutique.id = :bid", Long.class
            ).setParameter("bid", boutiqueId).getSingleResult();

            Long nbReparations = em.createQuery(
                    "SELECT COUNT(r) FROM Reparation r WHERE r.boutique.id = :bid", Long.class
            ).setParameter("bid", boutiqueId).getSingleResult();

            if (nbRep != null && nbRep > 0) {
                throw new ValidationException("Suppression impossible : des réparateurs sont rattachés à cette boutique.");
            }
            if (nbReparations != null && nbReparations > 0) {
                throw new ValidationException("Suppression impossible : des réparations existent pour cette boutique.");
            }

            em.remove(b);

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    @Override
    public double profitProprietaire(double coutTotal, double pourcentageReparateur) {
        return coutTotal * (100.0 - pourcentageReparateur) / 100.0;
    }
    
    @Override
    public List<User> listerReparateursParBoutique(Long boutiqueId) {
        if (boutiqueId == null) throw new ValidationException("Boutique id obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                    "SELECT u FROM User u WHERE u.role = :role AND u.boutique.id = :bid ORDER BY u.id DESC",
                    User.class
            );
            q.setParameter("role", Role.REPARATEUR);
            q.setParameter("bid", boutiqueId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User creerReparateur(String email, String motDePasse, String nom, String prenom, String telephone,
                               Double pourcentage, Long boutiqueId) {

        if (email == null || email.isBlank()) throw new ValidationException("Email obligatoire.");
        if (motDePasse == null || motDePasse.isBlank()) throw new ValidationException("Mot de passe obligatoire.");
        if (nom == null || nom.isBlank()) throw new ValidationException("Nom obligatoire.");
        if (prenom == null || prenom.isBlank()) throw new ValidationException("Prénom obligatoire.");
        if (telephone == null || telephone.isBlank()) throw new ValidationException("Téléphone obligatoire.");
        if (pourcentage == null) throw new ValidationException("Pourcentage obligatoire.");
        if (pourcentage < 0 || pourcentage > 100) throw new ValidationException("Pourcentage doit être entre 0 et 100.");
        if (boutiqueId == null) throw new ValidationException("Boutique obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Boutique b = em.find(Boutique.class, boutiqueId);
            if (b == null) throw new ValidationException("Boutique introuvable.");

            User rep = new User();
            rep.setEmail(email.trim());
            rep.setMotDePasse(motDePasse);
            rep.setNom(nom.trim());
            rep.setPrenom(prenom.trim());
            rep.setTelephone(telephone.trim());
            rep.setRole(Role.REPARATEUR);
            rep.setPourcentage(pourcentage);
            rep.setBoutique(b);

            em.persist(rep);

            em.getTransaction().commit();
            return rep;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void supprimerReparateur(Long reparateurId) {
        if (reparateurId == null) throw new ValidationException("Réparateur id obligatoire.");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User rep = em.find(User.class, reparateurId);
            if (rep == null) throw new ValidationException("Réparateur introuvable.");
            if (rep.getRole() != Role.REPARATEUR) throw new ValidationException("L'utilisateur sélectionné n'est pas un réparateur.");

            Long nbReparations = em.createQuery(
                    "SELECT COUNT(r) FROM Reparation r WHERE r.reparateur.id = :rid", Long.class
            ).setParameter("rid", reparateurId).getSingleResult();

            Long nbTransactions = em.createQuery(
                    "SELECT COUNT(t) FROM Transaction t WHERE t.reparateur.id = :rid", Long.class
            ).setParameter("rid", reparateurId).getSingleResult();

            if (nbReparations != null && nbReparations > 0) {
                throw new ValidationException("Suppression impossible : ce réparateur a des réparations.");
            }
            if (nbTransactions != null && nbTransactions > 0) {
                throw new ValidationException("Suppression impossible : ce réparateur a des transactions.");
            }

            em.remove(rep);

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    @Override
    public StatsProprietaireDTO statsProprietaire(Long proprietaireId, LocalDate dateDebut, LocalDate dateFin) {
        if (proprietaireId == null) throw new ValidationException("Propriétaire id obligatoire.");

        LocalDateTime start = (dateDebut == null) ? LocalDate.of(2000,1,1).atStartOfDay() : dateDebut.atStartOfDay();
        LocalDateTime end = (dateFin == null) ? LocalDate.of(2100,1,1).atTime(23,59,59) : dateFin.atTime(23,59,59);

        EntityManager em = JpaUtil.getEntityManager();
        try {
            StatsProprietaireDTO dto = new StatsProprietaireDTO();

            Long nbBoutiques = em.createQuery(
                    "SELECT COUNT(b) FROM Boutique b WHERE b.proprietaire.id = :pid", Long.class
            ).setParameter("pid", proprietaireId).getSingleResult();

            Long nbReparateurs = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.boutique.proprietaire.id = :pid", Long.class
            ).setParameter("role", Role.REPARATEUR)
             .setParameter("pid", proprietaireId)
             .getSingleResult();

            Long nbReparations = em.createQuery(
                    "SELECT COUNT(r) FROM Reparation r WHERE r.boutique.proprietaire.id = :pid " +
                            "AND r.dateCreation BETWEEN :start AND :end", Long.class
            ).setParameter("pid", proprietaireId)
             .setParameter("start", start)
             .setParameter("end", end)
             .getSingleResult();

            Double ca = em.createQuery(
                    "SELECT COALESCE(SUM(r.coutTotal), 0) FROM Reparation r WHERE r.boutique.proprietaire.id = :pid " +
                            "AND r.dateCreation BETWEEN :start AND :end", Double.class
            ).setParameter("pid", proprietaireId)
             .setParameter("start", start)
             .setParameter("end", end)
             .getSingleResult();

            // Profit propriétaire = SUM(coutTotal * (100 - pourcentage)/100) sur réparations
            Double profit = em.createQuery(
                    "SELECT COALESCE(SUM(r.coutTotal * (100.0 - r.reparateur.pourcentage) / 100.0), 0) " +
                            "FROM Reparation r WHERE r.boutique.proprietaire.id = :pid " +
                            "AND r.dateCreation BETWEEN :start AND :end", Double.class
            ).setParameter("pid", proprietaireId)
             .setParameter("start", start)
             .setParameter("end", end)
             .getSingleResult();

            dto.setNbBoutiques(nbBoutiques == null ? 0 : nbBoutiques);
            dto.setNbReparateurs(nbReparateurs == null ? 0 : nbReparateurs);
            dto.setNbReparations(nbReparations == null ? 0 : nbReparations);
            dto.setChiffreAffaires(ca == null ? 0 : ca);
            dto.setProfitProprietaire(profit == null ? 0 : profit);

            return dto;
        } finally {
            em.close();
        }
    }
    
    
    //* Top réparateur dans statistiques (prop)
    @Override
    public List<TopReparateurDTO> topReparateurs(Long proprietaireId, LocalDate d1, LocalDate d2) {

        LocalDateTime start = (d1 == null) ? LocalDate.of(2000,1,1).atStartOfDay() : d1.atStartOfDay();
        LocalDateTime end = (d2 == null) ? LocalDate.of(2100,1,1).atTime(23,59,59) : d2.atTime(23,59,59);

        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT new ma.gestionreparation.metier.dto.TopReparateurDTO(" +
                "u.id, u.nom, u.prenom, COALESCE(SUM(r.coutTotal),0)) " +
                "FROM Reparation r JOIN r.reparateur u " +
                "WHERE r.boutique.proprietaire.id = :pid " +
                "AND r.dateCreation BETWEEN :start AND :end " +
                "GROUP BY u.id, u.nom, u.prenom " +
                "ORDER BY SUM(r.coutTotal) DESC",
                TopReparateurDTO.class
            )
            .setParameter("pid", proprietaireId)
            .setParameter("start", start)
            .setParameter("end", end)
            .setMaxResults(5)
            .getResultList();
        } finally {
            em.close();
        }
    }



}
