package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.Transaction;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.dao.enums.TypeCaisse;
import ma.gestionreparation.dao.enums.TypeOperation;
import ma.gestionreparation.metier.interfaces.ITransactionMetier;

import java.util.List;

public class TransactionController {

    private final ITransactionMetier metier;

    public TransactionController(ITransactionMetier metier) {
        this.metier = metier;
    }

    public Transaction creer(
            String description,
            double montant,
            TypeOperation operation,
            TypeCaisse caisse,
            User reparateur,
            Reparation reparation
    ) {
        return metier.creer(description, montant, operation, caisse, reparateur, reparation);
    }

    public void modifier(
            Long transactionId,
            String description,
            double montant,
            TypeOperation operation,
            TypeCaisse caisse
    ) {
        metier.modifier(transactionId, description, montant, operation, caisse);
    }

    public void supprimer(Long transactionId) {
        metier.supprimer(transactionId);
    }

    public List<Transaction> listerPourReparateur(Long reparateurId) {
        return metier.listerPourReparateur(reparateurId);
    }
}
