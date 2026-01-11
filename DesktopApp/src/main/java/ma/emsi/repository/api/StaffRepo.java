package ma.emsi.repository.api;

import java.time.LocalDate;
import java.util.List;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.api.common.CrudRepo;

public interface StaffRepo extends CrudRepo<Staff, Long> {

    List<Staff> findAllOrderByNom();
    List<Staff> findBySalaireBetween(Double min, Double max);
    List<Staff> findByDateRecrutementAfter(LocalDate date);

    List<Staff> findByCabinetId(Long cabinetId);
    void assignToCabinet(Long staffId, Long cabinetId);
    void removeFromCabinet(Long staffId);

    // ✅ Helpers multi-table (utilisés par services)
    void insertStaffFields(Staff staff);
    void updateStaffFields(Staff staff);
}
