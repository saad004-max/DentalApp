package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.Prescription;
import ma.emsi.repository.api.medical.PrescriptionRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionRepoImpl implements PrescriptionRepo {

    private Connection connection;

    @Override
    public List<Prescription> findAll() {
        String sql = "SELECT * FROM Prescriptions ORDER BY id";
        return executeQuery(sql);
    }

    @Override
    public Prescription findById(Long id) {
        String sql = "SELECT * FROM Prescriptions WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapPrescription(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la prescription", e);
        }
        return null;
    }

    @Override
    public void create(Prescription prescription) {
        String sql = """
            INSERT INTO Prescriptions (ordonnanceId, medicamentId, quantite, frequence, dureeEnJours,
                                       dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, prescription.getOrdonnanceId());
            ps.setLong(2, prescription.getMedicamentId());
            ps.setInt(3, prescription.getQuantite());
            ps.setString(4, prescription.getFrequence());
            ps.setInt(5, prescription.getDureeEnJours());
            ps.setDate(6, prescription.getDateCreation() != null ? Date.valueOf(prescription.getDateCreation()) : null);
            ps.setString(7, prescription.getCreePar());
            ps.setString(8, prescription.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) prescription.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la prescription", e);
        }
    }

    @Override
    public void update(Prescription prescription) {
        String sql = """
            UPDATE Prescriptions
            SET ordonnanceId = ?, medicamentId = ?, quantite = ?, frequence = ?, dureeEnJours = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, prescription.getOrdonnanceId());
            ps.setLong(2, prescription.getMedicamentId());
            ps.setInt(3, prescription.getQuantite());
            ps.setString(4, prescription.getFrequence());
            ps.setInt(5, prescription.getDureeEnJours());
            ps.setString(6, prescription.getModifiePar());
            ps.setLong(7, prescription.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la prescription", e);
        }
    }

    @Override
    public void delete(Prescription prescription) {
        if (prescription != null) deleteById(prescription.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Prescriptions WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la prescription", e);
        }
    }

    @Override
    public List<Prescription> findByOrdonnanceId(Long ordonnanceId) {
        String sql = "SELECT * FROM Prescriptions WHERE ordonnanceId = ?";
        return executeQueryWithLong(sql, ordonnanceId);
    }

    @Override
    public List<Prescription> findByMedicamentId(Long medicamentId) {
        String sql = "SELECT * FROM Prescriptions WHERE medicamentId = ?";
        return executeQueryWithLong(sql, medicamentId);
    }

    // Helper methods
    private List<Prescription> executeQuery(String sql) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) prescriptions.add(RowMappers.mapPrescription(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prescriptions;
    }

    private List<Prescription> executeQueryWithLong(String sql, Long param) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) prescriptions.add(RowMappers.mapPrescription(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prescriptions;
    }
}
