package ma.emsi.repository.impl.financial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.EnPromo;
import ma.emsi.entities.modules.enums.StatutFinancier;
import ma.emsi.entities.modules.financial.SituationFinanciere;
import ma.emsi.repository.api.financial.SituationFinanciereRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationFinanciereRepoImpl implements SituationFinanciereRepo {

    private Connection connection;

    @Override
    public List<SituationFinanciere> findAll() {
        String sql = "SELECT * FROM SituationsFinancieres ORDER BY id";
        return executeQuery(sql);
    }

    @Override
    public SituationFinanciere findById(Long id) {
        String sql = "SELECT * FROM SituationsFinancieres WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapSituationFinanciere(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la situation financière", e);
        }
        return null;
    }

    @Override
    public void create(SituationFinanciere situation) {
        String sql = """
            INSERT INTO SituationsFinancieres (patientId, totaleDesActes, totalePaye, credit, statut, enPromo,
                                               dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, situation.getPatientId());
            ps.setDouble(2, situation.getTotaleDesActes());
            ps.setDouble(3, situation.getTotalePaye());
            ps.setDouble(4, situation.getCredit());
            ps.setString(5, situation.getStatut().name());
            ps.setString(6, situation.getEnPromo().name());
            ps.setDate(7, situation.getDateCreation() != null ? Date.valueOf(situation.getDateCreation()) : null);
            ps.setString(8, situation.getCreePar());
            ps.setString(9, situation.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) situation.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la situation financière", e);
        }
    }

    @Override
    public void update(SituationFinanciere situation) {
        String sql = """
            UPDATE SituationsFinancieres
            SET patientId = ?, totaleDesActes = ?, totalePaye = ?, credit = ?, statut = ?, enPromo = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, situation.getPatientId());
            ps.setDouble(2, situation.getTotaleDesActes());
            ps.setDouble(3, situation.getTotalePaye());
            ps.setDouble(4, situation.getCredit());
            ps.setString(5, situation.getStatut().name());
            ps.setString(6, situation.getEnPromo().name());
            ps.setString(7, situation.getModifiePar());
            ps.setLong(8, situation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la situation financière", e);
        }
    }

    @Override
    public void delete(SituationFinanciere situation) {
        if (situation != null) deleteById(situation.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM SituationsFinancieres WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la situation financière", e);
        }
    }

    @Override
    public SituationFinanciere findByPatientId(Long patientId) {
        String sql = "SELECT * FROM SituationsFinancieres WHERE patientId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapSituationFinanciere(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération par patient", e);
        }
        return null;
    }

    @Override
    public List<SituationFinanciere> findByStatut(StatutFinancier statut) {
        String sql = "SELECT * FROM SituationsFinancieres WHERE statut = ?";
        return executeQueryWithString(sql, statut.name());
    }

    @Override
    public List<SituationFinanciere> findEnPromo() {
        String sql = "SELECT * FROM SituationsFinancieres WHERE enPromo = 'OUI'";
        return executeQuery(sql);
    }

    @Override
    public Double getTotalCredit() {
        String sql = "SELECT SUM(credit) FROM SituationsFinancieres";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double total = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : total;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du crédit total", e);
        }
        return 0.0;
    }

    @Override
    public List<SituationFinanciere> findByCreditGreaterThan(Double montant) {
        String sql = "SELECT * FROM SituationsFinancieres WHERE credit > ? ORDER BY credit DESC";
        List<SituationFinanciere> situations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, montant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) situations.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par crédit", e);
        }
        return situations;
    }

    // Helper methods
    private List<SituationFinanciere> executeQuery(String sql) {
        List<SituationFinanciere> situations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) situations.add(RowMappers.mapSituationFinanciere(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return situations;
    }

    private List<SituationFinanciere> executeQueryWithString(String sql, String param) {
        List<SituationFinanciere> situations = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) situations.add(RowMappers.mapSituationFinanciere(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return situations;
    }
}
