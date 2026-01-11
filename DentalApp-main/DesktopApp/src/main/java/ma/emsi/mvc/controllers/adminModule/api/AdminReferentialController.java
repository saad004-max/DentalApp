package ma.emsi.mvc.controllers.adminModule.api;

import java.util.List;
import ma.emsi.mvc.dto.common.ResponseWrapper;

/**
 * Controller pour la gestion des données référentielles par l'admin.
 * Use Cases: [43-45, 53-63] - Catalogues (Actes, Médicaments, Antécédents, Assurances)
 */
public interface AdminReferentialController {
    
    // ========== GESTION DU CATALOGUE DES ACTES ==========
    
    /**
     * Récupère tous les actes.
     */
    ResponseWrapper<List<ActeDto>> getAllActes();
    
    /**
     * Crée un nouvel acte.
     */
    ResponseWrapper<ActeDto> createActe(ActeRequest request);
    
    /**
     * Modifie un acte.
     */
    ResponseWrapper<ActeDto> updateActe(Long acteId, ActeRequest request);
    
    /**
     * Supprime un acte.
     */
    ResponseWrapper<Void> deleteActe(Long acteId);
    
    // ========== GESTION DU CATALOGUE DES MÉDICAMENTS ==========
    
    /**
     * Récupère tous les médicaments.
     */
    ResponseWrapper<List<MedicamentDto>> getAllMedicaments();
    
    /**
     * Crée un nouveau médicament.
     */
    ResponseWrapper<MedicamentDto> createMedicament(MedicamentRequest request);
    
    /**
     * Modifie un médicament.
     */
    ResponseWrapper<MedicamentDto> updateMedicament(Long medicamentId, MedicamentRequest request);
    
    /**
     * Supprime un médicament.
     */
    ResponseWrapper<Void> deleteMedicament(Long medicamentId);
    
    /**
     * Recherche de médicaments.
     */
    ResponseWrapper<List<MedicamentDto>> searchMedicaments(String keyword);
    
    // ========== GESTION DU CATALOGUE DES ANTÉCÉDENTS ==========
    
    /**
     * Récupère tous les types d'antécédents.
     */
    ResponseWrapper<List<AntecedentTypeDto>> getAllAntecedentTypes();
    
    /**
     * Crée un type d'antécédent.
     */
    ResponseWrapper<AntecedentTypeDto> createAntecedentType(AntecedentTypeRequest request);
    
    /**
     * Modifie un type d'antécédent.
     */
    ResponseWrapper<AntecedentTypeDto> updateAntecedentType(Long typeId, AntecedentTypeRequest request);
    
    /**
     * Supprime un type d'antécédent.
     */
    ResponseWrapper<Void> deleteAntecedentType(Long typeId);
}

/**
 * DTOs pour ce controller.
 */
class ActeDto {
    public Long id;
    public String libelle;
    public String categorie;
    public Double prixDeBase;
}

class ActeRequest {
    public String libelle;
    public String categorie;
    public Double prixDeBase;
}

class MedicamentDto {
    public Long id;
    public String nom;
    public String laboratoire;
    public String type;
    public String forme;
    public Boolean remboursable;
    public Double prixUnitaire;
}

class MedicamentRequest {
    public String nom;
    public String laboratoire;
    public String type;
    public String forme;
    public Boolean remboursable;
    public Double prixUnitaire;
    public String description;
}

class AntecedentTypeDto {
    public Long id;
    public String nom;
    public String categorie;
    public String niveauRisque;
}

class AntecedentTypeRequest {
    public String nom;
    public String categorie;
    public String niveauRisque;
}
