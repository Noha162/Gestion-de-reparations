package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.metier.dto.BoutiqueDTO;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.IBoutiqueMetier;

import java.util.List;

public class BoutiqueMetierImpl implements IBoutiqueMetier {

    // EXISTANT (si tu l’avais déjà)
    @Override
    public List<BoutiqueDTO> lister() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT new ma.gestionreparation.metier.dto.BoutiqueDTO(b.id, b.nom) FROM Boutique b",
                    BoutiqueDTO.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    // ✅ NOUVEAU : boutiques accessibles par le réparateur
    @Override
    public List<BoutiqueDTO> listerPourReparateur(Long reparateurId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    """
                    SELECT new ma.gestionreparation.metier.dto.BoutiqueDTO(
                        b.id, b.nom
                    )
                    FROM User u
                    JOIN u.boutique b
                    WHERE u.id = :rid
                    """,
                    BoutiqueDTO.class
            )
            .setParameter("rid", reparateurId)
            .getResultList();
        } finally {
            em.close();
        }
    }
}
