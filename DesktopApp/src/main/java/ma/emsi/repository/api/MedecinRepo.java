package ma.emsi.repository.api;


import java.util.List;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.api.common.CrudRepo;

public interface MedecinRepo extends CrudRepo<Medecin, Long> {

    List<Medecin> findAllOrderByNom();
    List<Medecin> findBySpecialite(String specialiteLike);


    // ✅ Helper multi-table
    void insertMedecinFields(Medecin medecin);
    void updateMedecinFields(Medecin medecin);

}

