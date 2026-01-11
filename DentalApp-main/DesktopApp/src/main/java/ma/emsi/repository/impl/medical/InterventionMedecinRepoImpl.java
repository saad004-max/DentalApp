package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.InterventionMedecin;
import ma.emsi.repository.api.medical.InterventionMedecinRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionMedecinRepoImpl implements InterventionMedecinRepo {

    private Connection connection;

    @Override
    public List<InterventionMedecin> findAll() {
        String sql = "SELECT * FROM InterventionsMedecin ORDER BY id";
        return executeQuery(sql);
    }

    @Override
    public InterventionMedecin findById(Long id) {
        String sql = "SELECT * FROM InterventionsMedecin WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapInterventionMedecin(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'intervention", e);
        }
        return null;
    }

    @Override
    public void create(InterventionMedecin intervention) {
        String sql = """
            INSERT INTO InterventionsMedecin (consultationId, acteId, prixDePatient, numDent,
                                              dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, intervention.getConsultationId());
            ps.setLong(2, intervention.getActeId());
            ps.setDouble(3, intervention.getPrixDePatient());
            if (intervention.getNumDent() != null) {
                ps.setInt(4, intervention.getNumDent());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setDate(5, intervention.getDateCreation() != null ? Date.valueOf(intervention.getDateCreation()) : null);
            ps.setString(6, intervention.getCreePar());
            ps.setString(7, intervention.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) intervention.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'intervention", e);
        }
    }

    @Override
    public void update(InterventionMedecin intervention) {
        String sql = """
            UPDATE InterventionsMedecin
            SET consultationId = ?, acteId = ?, prixDePatient = ?, numDent = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, intervention.getConsultationId());
            ps.setLong(2, intervention.getActeId());
            ps.setDouble(3, intervention.getPrixDePatient());
            if (intervention.getNumDent() != null) {
                ps.setInt(4, intervention.getNumDent());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, intervention.getModifiePar());
            ps.setLong(6, intervention.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'intervention", e);
        }
    }

    @Override
    public void delete(InterventionMedecin intervention) {
        if (intervention != null) deleteById(intervention.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM InterventionsMedecin WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'intervention", e);
        }
    }

    @Override
    public List<InterventionMedecin> findByConsultationId(Long consultationId) {
        String sql = "SELECT * FROM InterventionsMedecin WHERE consultationId = ?";
        return executeQueryWithLong(sql, consultationId);
    }

    @Override
    public List<InterventionMedecin> findByActeId(Long acteId) {
        String sql = "SELECT * FROM InterventionsMedecin WHERE acteId = ?";
        return executeQueryWithLong(sql, acteId);
    }

    @Override
    public Double getTotalByConsultation(Long consultationId) {
        String sql = "SELECT SUM(prixDePatient) FROM InterventionsMedecin WHERE consultationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : total;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du total", e);
        }
        return 0.0;
    }

    // Helper methods
    private List<InterventionMedecin> executeQuery(String sql) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) interventions.add(RowMappers.mapInterventionMedecin(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return interventions;
    }

    private List<InterventionMedecin> executeQueryWithLong(String sql, Long param) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) interventions.add(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return interventions;
    }
}
