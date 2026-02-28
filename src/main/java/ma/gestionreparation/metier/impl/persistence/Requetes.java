package ma.gestionreparation.metier.impl.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.Transaction;
import ma.gestionreparation.dao.enums.TypeCaisse;
import ma.gestionreparation.dao.enums.TypeOperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Requetes {

    public static Optional<Reparation> findReparationByCodeSuivi(EntityManager em, String codeSuivi) {
        TypedQuery<Reparation> q = em.createQuery(
                "SELECT r FROM Reparation r WHERE r.codeSuivi = :code", Reparation.class);
        q.setParameter("code", codeSuivi);
        List<Reparation> res = q.getResultList();
        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    public static double soldeCaisse(EntityManager em, Long reparateurId, TypeCaisse typeCaisse,
                                     LocalDateTime from, LocalDateTime to) {

        Double totalEntree = em.createQuery(
                "SELECT COALESCE(SUM(t.montant), 0) FROM Transaction t " +
                        "WHERE t.reparateur.id = :rid AND t.typeCaisse = :tc AND t.typeOperation = :op " +
                        "AND t.date BETWEEN :d1 AND :d2", Double.class)
                .setParameter("rid", reparateurId)
                .setParameter("tc", typeCaisse)
                .setParameter("op", TypeOperation.ENTREE)
                .setParameter("d1", from)
                .setParameter("d2", to)
                .getSingleResult();

        Double totalSortie = em.createQuery(
                "SELECT COALESCE(SUM(t.montant), 0) FROM Transaction t " +
                        "WHERE t.reparateur.id = :rid AND t.typeCaisse = :tc AND t.typeOperation = :op " +
                        "AND t.date BETWEEN :d1 AND :d2", Double.class)
                .setParameter("rid", reparateurId)
                .setParameter("tc", typeCaisse)
                .setParameter("op", TypeOperation.SORTIE)
                .setParameter("d1", from)
                .setParameter("d2", to)
                .getSingleResult();

        return totalEntree - totalSortie;
    }

    public static List<Transaction> transactionsByReparateur(EntityManager em, Long reparateurId) {
        TypedQuery<Transaction> q = em.createQuery(
                "SELECT t FROM Transaction t WHERE t.reparateur.id = :rid ORDER BY t.date DESC", Transaction.class);
        q.setParameter("rid", reparateurId);
        return q.getResultList();
    }
}
