package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IClientSuiviMetier;

public class ClientSuiviMetierImpl implements IClientSuiviMetier {

    @Override
    public Reparation findByCodeSuivi(String code) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // 1️⃣ Charger réparation + client + transactions
            Reparation r = em.createQuery(
                "SELECT r FROM Reparation r " +
                "LEFT JOIN FETCH r.client " +
                "LEFT JOIN FETCH r.transactions " +
                "WHERE r.codeSuivi = :code",
                Reparation.class
            )
            .setParameter("code", code)
            .getSingleResult();

            // 2️⃣ FORCER le chargement des lignes (séparément)
            r.getLignes().size(); // 🔥 IMPORTANT

            return r;

        } finally {
            em.close();
        }
    }
}
