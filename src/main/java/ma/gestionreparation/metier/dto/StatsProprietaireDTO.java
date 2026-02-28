package ma.gestionreparation.metier.dto;

public class StatsProprietaireDTO {
    private long nbBoutiques;
    private long nbReparateurs;
    private long nbReparations;
    private double chiffreAffaires;
    private double profitProprietaire;

    public long getNbBoutiques() { return nbBoutiques; }
    public void setNbBoutiques(long nbBoutiques) { this.nbBoutiques = nbBoutiques; }

    public long getNbReparateurs() { return nbReparateurs; }
    public void setNbReparateurs(long nbReparateurs) { this.nbReparateurs = nbReparateurs; }

    public long getNbReparations() { return nbReparations; }
    public void setNbReparations(long nbReparations) { this.nbReparations = nbReparations; }

    public double getChiffreAffaires() { return chiffreAffaires; }
    public void setChiffreAffaires(double chiffreAffaires) { this.chiffreAffaires = chiffreAffaires; }

    public double getProfitProprietaire() { return profitProprietaire; }
    public void setProfitProprietaire(double profitProprietaire) { this.profitProprietaire = profitProprietaire; }
}
