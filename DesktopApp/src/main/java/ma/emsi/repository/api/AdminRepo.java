package ma.emsi.repository.api;

import java.util.List;
import java.util.Optional;
import ma.emsi.entities.modules.users.Admin;
import ma.emsi.repository.api.common.CrudRepo;

public interface AdminRepo extends CrudRepo<Admin, Long> {

    List<Admin> findAllOrderByNom();
    Optional<Admin> findByEmail(String email);

    // ✅ Helper multi-table
    void insertAdminFields(Admin admin);
}
