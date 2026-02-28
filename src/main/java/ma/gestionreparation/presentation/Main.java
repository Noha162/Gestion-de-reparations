package ma.gestionreparation.presentation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.gestionreparation.dao.*;
import ma.gestionreparation.dao.enums.*;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // 0) CHECK : déjà seed ?
            User existingProp = findUserByEmail(em, "prop@mail.com");
            if (existingProp != null) {
                System.out.println("ℹ️ Seed déjà présent (prop@mail.com existe). Aucun insert.");
                em.getTransaction().commit();
                return;
            }

            // 1) PROPRIÉTAIRE
            User proprietaire = new User();
            proprietaire.setEmail("prop@mail.com");
            proprietaire.setMotDePasse("1234");
            proprietaire.setNom("Doe");
            proprietaire.setPrenom("John");
            proprietaire.setTelephone("0600000000");
            proprietaire.setRole(Role.PROPRIETAIRE);
            proprietaire.setPourcentage(null);
            proprietaire.setBoutique(null);
            em.persist(proprietaire);

            // 2) BOUTIQUE
            Boutique boutique = new Boutique();
            boutique.setNom("Boutique Centre");
            boutique.setAdresse("10 rue Paris");
            boutique.setNumeroPatente("PAT-001");
            boutique.setProprietaire(proprietaire);
            em.persist(boutique);

            // 3) RÉPARATEUR
            User reparateur = new User();
            reparateur.setEmail("rep@mail.com");
            reparateur.setMotDePasse("1234");
            reparateur.setNom("Smith");
            reparateur.setPrenom("Ali");
            reparateur.setTelephone("0611111111");
            reparateur.setRole(Role.REPARATEUR);
            reparateur.setPourcentage(30.0);
            reparateur.setBoutique(boutique);
            em.persist(reparateur);

            // 4) CLIENT
            Client client = new Client();
            client.setNom("Client");
            client.setPrenom("Test");
            client.setTelephone("0622222222");
            em.persist(client);

            // 5) APPAREIL
            Appareil appareil = new Appareil();
            appareil.setType("Téléphone");
            appareil.setMarque("Apple");
            appareil.setModele("iPhone 13");
            appareil.setRam("4Go");
            appareil.setStockage("128Go");
            appareil.setClient(client);
            em.persist(appareil);

            // 6) RÉPARATION
            Reparation reparation = new Reparation();
            reparation.setClient(client);
            reparation.setReparateur(reparateur);
            reparation.setBoutique(boutique);
            reparation.setDescriptionPanne("Écran cassé");
            reparation.setDateCreation(LocalDateTime.now());
            reparation.setStatut(StatutReparation.EN_COURS);
            reparation.setCoutTotal(200.0);
            reparation.setCodeSuivi(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            em.persist(reparation);

            // 7) LIGNE REPARATION
            LigneReparation ligne = new LigneReparation();
            ligne.setReparation(reparation);
            ligne.setAppareil(appareil);
            ligne.setEtatAppareil(EtatAppareil.EN_COURS);
            ligne.setCoutAppareil(200.0);
            ligne.setCommentaire("Remplacement écran");
            em.persist(ligne);

            try {
                reparation.addLigne(ligne);
            } catch (Exception ignored) {}

            em.merge(reparation);

            // COMMIT
            em.getTransaction().commit();

            System.out.println("✅ Seed OK");
            System.out.println("Propriétaire: " + proprietaire.getEmail());
            System.out.println("Réparateur: " + reparateur.getEmail());
            System.out.println("Boutique: " + boutique.getNom());
            System.out.println("Client: " + client.getPrenom() + " " + client.getNom());
            System.out.println("Code suivi réparation: " + reparation.getCodeSuivi());

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
            JpaUtil.close();
        }
    }

    private static User findUserByEmail(EntityManager em, String email) {
        TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email",
                User.class
        );
        q.setParameter("email", email);
        List<User> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
