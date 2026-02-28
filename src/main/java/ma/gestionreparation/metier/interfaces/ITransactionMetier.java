package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.Transaction;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.dao.enums.TypeCaisse;
import ma.gestionreparation.dao.enums.TypeOperation;

import java.util.List;

public interface ITransactionMetier {

    Transaction creer(
            String description,
            double montant,
            TypeOperation operation,
            TypeCaisse caisse,
            User reparateur,
            Reparation reparation
    );

    void modifier(
            Long transactionId,
            String description,
            double montant,
            TypeOperation operation,
            TypeCaisse caisse
    );

    void supprimer(Long transactionId);

    List<Transaction> listerPourReparateur(Long reparateurId);

	void supprimer(Long transactionId, Long reparateurId);

	Transaction modifier(Long transactionId, Long reparateurId, String description, double montant,
			TypeOperation operation, TypeCaisse caisse);

	Transaction creer(Long reparateurId, Long reparationId, String description, double montant, TypeOperation operation,
			TypeCaisse caisse);
}
