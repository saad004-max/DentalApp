package ma.emsi.repository.api;


import java.util.List;
import java.util.Optional;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.api.common.CrudRepo;

public interface SecretaireRepo extends CrudRepo<Secretaire, Long> {

    List<Secretaire> findAllOrderByNom();
    Optional<Secretaire> findByNumCNSS(String numCNSS);
    List<Secretaire> findByCommissionMin(Double minCommission);

    // ✅ Helper multi-table
    void insertSecretaireFields(Secretaire secretaire);
    void updateSecretaireFields(Secretaire secretaire);
}

