package ma.emsi.repository.impl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.common.RowMappers;
import ma.emsi.repository.api.*;

@Data @AllArgsConstructor
public class StaffRepoImpl implements StaffRepo {

    private final Connection c;

    private static final String BASE_SELECT = """
        SELECT u.*, s.salaire, s.prime, s.dateRecrutement, s.soldeConge
        FROM Staffs s
        JOIN Utilisateurs u ON s.id = u.id
        """;


    @Override
    public void insertStaffFields(Staff s) {
        String sql = """
        INSERT INTO Staffs (id, salaire, prime, dateRecrutement, soldeConge)
        VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, s.getId());
            if (s.getSalaire() != null) ps.setDouble(2, s.getSalaire()); else ps.setNull(2, Types.DOUBLE);
            if (s.getPrime() != null) ps.setDouble(3, s.getPrime()); else ps.setNull(3, Types.DOUBLE);
            if (s.getDateRecrutement() != null) ps.setDate(4, Date.valueOf(s.getDateRecrutement())); else ps.setNull(4, Types.DATE);
            if (s.getSoldeConge() != null) ps.setInt(5, s.getSoldeConge()); else ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("insertStaffFields failed (id=" + s.getId() + ")", e);
        }
    }

    public void updateStaffFields(Staff s)  {
        String sql = """
            UPDATE Staffs SET salaire=?, prime=?, dateRecrutement=?, soldeConge=?
            WHERE id=?
            """;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (s.getSalaire() != null) ps.setDouble(1, s.getSalaire()); else ps.setNull(1, Types.DOUBLE);
            if (s.getPrime() != null) ps.setDouble(2, s.getPrime()); else ps.setNull(2, Types.DOUBLE);
            if (s.getDateRecrutement() != null) ps.setDate(3, Date.valueOf(s.getDateRecrutement())); else ps.setNull(3, Types.DATE);
            if (s.getSoldeConge() != null) ps.setInt(4, s.getSoldeConge()); else ps.setNull(4, Types.INTEGER);
            ps.setLong(5, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
        throw new RuntimeException("updateStaffFields failed (id=" + s.getId() + ")", e);
    }
    }

    @Override
    public List<Staff> findAll() {
        String sql = BASE_SELECT + " ORDER BY u.id";
        List<Staff> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapStaff(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Staff findById(Long id) {
        String sql = BASE_SELECT + " WHERE u.id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapStaff(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Staff staff) {
        // Création multi-tables (Utilisateurs + Staffs) => à faire dans un service
        //throw new UnsupportedOperationException("Créer un Staff doit passer par un service (Utilisateur + Staff).");

            //insertStaffFields(staff);
    }

    @Override
    public void update(Staff staff) {
        // Update multi-tables (Utilisateurs + Staffs) => à faire dans un service
        throw new UnsupportedOperationException("Mettre à jour un Staff doit passer par un service.");
    }

    @Override
    public void delete(Staff staff) {
        if (staff != null) deleteById(staff.getId());
    }

    @Override
    public void deleteById(Long id) {
        // La suppression se fait naturellement via FK (Staffs -> Utilisateurs) si cascade,
        // sinon on delete ici Staffs, et ailleurs Utilisateurs.
        String sql = "DELETE FROM Staffs WHERE id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Staff> findAllOrderByNom() {
        String sql = BASE_SELECT + " ORDER BY u.nom";
        List<Staff> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapStaff(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Staff> findBySalaireBetween(Double min, Double max) {
        String sql = BASE_SELECT + " WHERE s.salaire BETWEEN ? AND ? ORDER BY s.salaire DESC";
        List<Staff> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStaff(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Staff> findByDateRecrutementAfter(LocalDate date) {
        String sql = BASE_SELECT + " WHERE s.dateRecrutement >= ? ORDER BY s.dateRecrutement DESC";
        List<Staff> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStaff(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Staff> findByCabinetId(Long cabinetId) {
        String sql = BASE_SELECT + " WHERE s.cabinet_id = ? ORDER BY u.nom";
        List<Staff> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapStaff(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }



    @Override
    public void removeFromCabinet(Long staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("staffId ne doit pas être null");
        }

        String sql = "UPDATE Staffs SET cabinet_id = NULL WHERE id = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, staffId);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("Aucun staff trouvé avec id=" + staffId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du retrait du staff du cabinet (staffId=" + staffId + ")", e);
        }
    }

    @Override
    public void assignToCabinet(Long staffId, Long cabinetId) {
        String sql = "UPDATE Staffs SET cabinet_id=? WHERE id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cabinetId);
            ps.setLong(2, staffId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new IllegalStateException("Aucun staff trouvé avec id=" + staffId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
