package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutConsultation;
import ma.emsi.entities.modules.medical.Consultation;
import ma.emsi.repository.api.medical.ConsultationRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationRepoImpl implements ConsultationRepo {

    private Connection connection;

    @Override
    public List<Consultation> findAll() {
        String sql = "SELECT * FROM Consultations ORDER BY date DESC";
        return executeQuery(sql);
    }

    @Override
    public Consultation findById(Long id) {
        String sql = "SELECT * FROM Consultations WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapConsultation(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la consultation", e);
        }
        return null;
    }

    @Override
    public void create(Consultation consultation) {
        String sql = """
            INSERT INTO Consultations (dossierMedicalId, medecinId, date, statut, observationMedecin,
                                       dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, consultation.getDossierMedicalId());
            ps.setLong(2, consultation.getMedecinId());
            ps.setDate(3, Date.valueOf(consultation.getDate()));
            ps.setString(4, consultation.getStatut().name());
            ps.setString(5, consultation.getObservationMedecin());
            ps.setDate(6, consultation.getDateCreation() != null ? Date.valueOf(consultation.getDateCreation()) : null);
            ps.setString(7, consultation.getCreePar());
            ps.setString(8, consultation.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) consultation.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la consultation", e);
        }
    }

    @Override
    public void update(Consultation consultation) {
        String sql = """
            UPDATE Consultations
            SET dossierMedicalId = ?, medecinId = ?, date = ?, statut = ?, observationMedecin = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, consultation.getDossierMedicalId());
            ps.setLong(2, consultation.getMedecinId());
            ps.setDate(3, Date.valueOf(consultation.getDate()));
            ps.setString(4, consultation.getStatut().name());
            ps.setString(5, consultation.getObservationMedecin());
            ps.setString(6, consultation.getModifiePar());
            ps.setLong(7, consultation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la consultation", e);
        }
    }

    @Override
    public void delete(Consultation consultation) {
        if (consultation != null) deleteById(consultation.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Consultations WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la consultation", e);
        }
    }

    @Override
    public List<Consultation> findByPatientId(Long patientId) {
        String sql = """
            SELECT c.* FROM Consultations c
            JOIN DossiersMedicaux dm ON c.dossierMedicalId = dm.id
            WHERE dm.patientId = ?
            ORDER BY c.date DESC
            """;
        return executeQueryWithLong(sql, patientId);
    }

    @Override
    public List<Consultation> findByMedecinId(Long medecinId) {
        String sql = "SELECT * FROM Consultations WHERE medecinId = ? ORDER BY date DESC";
        return executeQueryWithLong(sql, medecinId);
    }

    @Override
    public List<Consultation> findByDossierMedicalId(Long dossierId) {
        String sql = "SELECT * FROM Consultations WHERE dossierMedicalId = ? ORDER BY date DESC";
        return executeQueryWithLong(sql, dossierId);
    }

    @Override
    public List<Consultation> findByStatut(StatutConsultation statut) {
        String sql = "SELECT * FROM Consultations WHERE statut = ? ORDER BY date DESC";
        return executeQueryWithString(sql, statut.name());
    }

    @Override
    public List<Consultation> findByDateRange(LocalDate debut, LocalDate fin) {
        String sql = "SELECT * FROM Consultations WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) consultations.add(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par période", e);
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByMedecinAndDate(Long medecinId, LocalDate date) {
        String sql = "SELECT * FROM Consultations WHERE medecinId = ? AND date = ? ORDER BY id";
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) consultations.add(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par médecin et date", e);
        }
        return consultations;
    }

    @Override
    public long countByStatut(StatutConsultation statut) {
        String sql = "SELECT COUNT(*) FROM Consultations WHERE statut = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, statut.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage par statut", e);
        }
        return 0;
    }

    // Helper methods
    private List<Consultation> executeQuery(String sql) {
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) consultations.add(RowMappers.mapConsultation(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consultations;
    }

    private List<Consultation> executeQueryWithLong(String sql, Long param) {
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) consultations.add(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consultations;
    }

    private List<Consultation> executeQueryWithString(String sql, String param) {
        List<Consultation> consultations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) consultations.add(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consultations;
    }
}
