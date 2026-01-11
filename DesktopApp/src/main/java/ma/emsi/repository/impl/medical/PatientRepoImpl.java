package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.Assurance;
import ma.emsi.entities.modules.medical.Patient;
import ma.emsi.repository.api.medical.PatientRepo;
import ma.emsi.repository.common.RowMappers;

/**
 * Implémentation JDBC du repository Patient.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRepoImpl implements PatientRepo {

    private Connection connection;

    @Override
    public List<Patient> findAll() {
        String sql = "SELECT * FROM Patients ORDER BY id";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                patients.add(RowMappers.mapPatient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des patients", e);
        }
        return patients;
    }

    @Override
    public Patient findById(Long id) {
        String sql = "SELECT * FROM Patients WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return RowMappers.mapPatient(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du patient", e);
        }
        return null;
    }

    @Override
    public void create(Patient patient) {
        String sql = """
            INSERT INTO Patients (nom, dateDeNaissance, sexe, adresse, telephone, assurance,
                                  dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, patient.getNom());
            ps.setDate(2, patient.getDateDeNaissance() != null ? Date.valueOf(patient.getDateDeNaissance()) : null);
            ps.setString(3, patient.getSexe() != null ? patient.getSexe().name() : null);
            ps.setString(4, patient.getAdresse());
            ps.setString(5, patient.getTelephone());
            ps.setString(6, patient.getAssurance() != null ? patient.getAssurance().name() : null);
            ps.setDate(7, patient.getDateCreation() != null ? Date.valueOf(patient.getDateCreation()) : null);
            ps.setString(8, patient.getCreePar());
            ps.setString(9, patient.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    patient.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du patient", e);
        }
    }

    @Override
    public void update(Patient patient) {
        String sql = """
            UPDATE Patients
            SET nom = ?, dateDeNaissance = ?, sexe = ?, adresse = ?, telephone = ?, assurance = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, patient.getNom());
            ps.setDate(2, patient.getDateDeNaissance() != null ? Date.valueOf(patient.getDateDeNaissance()) : null);
            ps.setString(3, patient.getSexe() != null ? patient.getSexe().name() : null);
            ps.setString(4, patient.getAdresse());
            ps.setString(5, patient.getTelephone());
            ps.setString(6, patient.getAssurance() != null ? patient.getAssurance().name() : null);
            ps.setString(7, patient.getModifiePar());
            ps.setLong(8, patient.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du patient", e);
        }
    }

    @Override
    public void delete(Patient patient) {
        if (patient != null) {
            deleteById(patient.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Patients WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du patient", e);
        }
    }

    @Override
    public List<Patient> searchByNom(String keyword) {
        String sql = "SELECT * FROM Patients WHERE nom LIKE ? ORDER BY nom";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(RowMappers.mapPatient(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de patients", e);
        }
        return patients;
    }

    @Override
    public List<Patient> findByAssurance(Assurance assurance) {
        String sql = "SELECT * FROM Patients WHERE assurance = ? ORDER BY nom";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, assurance.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(RowMappers.mapPatient(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des patients par assurance", e);
        }
        return patients;
    }

    @Override
    public Patient findByCin(String cin) {
        String sql = "SELECT * FROM Patients WHERE cin = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return RowMappers.mapPatient(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du patient par CIN", e);
        }
        return null;
    }

    @Override
    public boolean existsByCin(String cin) {
        String sql = "SELECT 1 FROM Patients WHERE cin = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du CIN", e);
        }
    }

    @Override
    public List<Patient> findPage(int limit, int offset) {
        String sql = "SELECT * FROM Patients ORDER BY id LIMIT ? OFFSET ?";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(RowMappers.mapPatient(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la pagination des patients", e);
        }
        return patients;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Patients";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des patients", e);
        }
        return 0;
    }
}
