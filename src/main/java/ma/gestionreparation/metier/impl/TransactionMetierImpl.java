package ma.gestionreparation.metier.impl;

import jakarta.persistence.EntityManager;
import ma.gestionreparation.dao.*;
import ma.gestionreparation.dao.enums.*;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.impl.persistence.JpaUtil;
import ma.gestionreparation.metier.interfaces.ITransactionMetier;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionMetierImpl implements ITransactionMetier {

    @Override
    public Transaction creer(Long reparateurId, Long reparationId,
                             String description, double montant,
                             TypeOperation operation, TypeCaisse caisse) {

        if (montant <= 0) throw new ValidationException("Montant invalide");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            User reparateur = em.find(User.class, reparateurId);
            Reparation reparation = em.find(Reparation.class, reparationId);

            if (reparateur == null) throw new ValidationException("Réparateur introuvable");
            if (reparation == null || !reparation.getReparateur().getId().equals(reparateurId))
                throw new ValidationException("Accès interdit à cette réparation");

            Transaction t = new Transaction();
            t.setDate(LocalDateTime.now());
            t.setDescription(description);
            t.setMontant(montant);
            t.setTypeOperation(operation);
            t.setTypeCaisse(caisse);
            t.setReparateur(reparateur);
            t.setReparation(reparation);

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

    @Override
    public Transaction modifier(Long transactionId, Long reparateurId,
                                String description, double montant,
                                TypeOperation operation, TypeCaisse caisse) {

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Transaction t = em.find(Transaction.class, transactionId);
            if (t == null) throw new ValidationException("Transaction introuvable");

            if (!t.getReparateur().getId().equals(reparateurId))
                throw new ValidationException("Accès interdit");

            t.setDescription(description);
            t.setMontant(montant);
            t.setTypeOperation(operation);
            t.setTypeCaisse(caisse);

            em.merge(t);
            em.getTransaction().commit();
            return t;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void supprimer(Long transactionId, Long reparateurId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Transaction t = em.find(Transaction.class, transactionId);
            if (t == null) throw new ValidationException("Transaction introuvable");

            if (!t.getReparateur().getId().equals(reparateurId))
                throw new ValidationException("Accès interdit");

            em.remove(t);
            em.getTransaction().commit();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Transaction> listerPourReparateur(Long reparateurId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT t FROM Transaction t " +
                    "JOIN FETCH t.reparation " +
                    "WHERE t.reparateur.id = :id " +
                    "ORDER BY t.date DESC",
                    Transaction.class
            ).setParameter("id", reparateurId)
             .getResultList();
        } finally {
            em.close();
        }
    }

	@Override
	public Transaction creer(String description, double montant, TypeOperation operation, TypeCaisse caisse,
			User reparateur, Reparation reparation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifier(Long transactionId, String description, double montant, TypeOperation operation,
			TypeCaisse caisse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimer(Long transactionId) {
		// TODO Auto-generated method stub
		
	}
}
