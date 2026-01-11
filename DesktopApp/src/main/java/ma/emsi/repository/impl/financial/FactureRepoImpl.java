package ma.emsi.repository.impl.financial;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutFacture;
import ma.emsi.entities.modules.financial.Facture;
import ma.emsi.repository.api.financial.FactureRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactureRepoImpl implements FactureRepo {

    private Connection connection;

    @Override
    public List<Facture> findAll() {
        String sql = "SELECT * FROM Factures ORDER BY dateFacture DESC";
        return executeQuery(sql);
    }

    @Override
    public Facture findById(Long id) {
        String sql = "SELECT * FROM Factures WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFacture(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la facture", e);
        }
        return null;
    }

    @Override
    public void create(Facture facture) {
        String sql = """
            INSERT INTO Factures (patientId, consultationId, totaleFacture, totalePaye, reste, statut, dateFacture,
                                  dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, facture.getPatientId());
            ps.setLong(2, facture.getConsultationId());
            ps.setDouble(3, facture.getTotaleFacture());
            ps.setDouble(4, facture.getTotalePaye());
            ps.setDouble(5, facture.getReste());
            ps.setString(6, facture.getStatut().name());
            ps.setTimestamp(7, Timestamp.valueOf(facture.getDateFacture()));
            ps.setDate(8, facture.getDateCreation() != null ? Date.valueOf(facture.getDateCreation()) : null);
            ps.setString(9, facture.getCreePar());
            ps.setString(10, facture.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) facture.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la facture", e);
        }
    }

    @Override
    public void update(Facture facture) {
        String sql = """
            UPDATE Factures
            SET patientId = ?, consultationId = ?, totaleFacture = ?, totalePaye = ?, reste = ?, 
                statut = ?, dateFacture = ?, dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, facture.getPatientId());
            ps.setLong(2, facture.getConsultationId());
            ps.setDouble(3, facture.getTotaleFacture());
            ps.setDouble(4, facture.getTotalePaye());
            ps.setDouble(5, facture.getReste());
            ps.setString(6, facture.getStatut().name());
            ps.setTimestamp(7, Timestamp.valueOf(facture.getDateFacture()));
            ps.setString(8, facture.getModifiePar());
            ps.setLong(9, facture.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la facture", e);
        }
    }

    @Override
    public void delete(Facture facture) {
        if (facture != null) deleteById(facture.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Factures WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la facture", e);
        }
    }

    @Override
    public List<Facture> findByPatientId(Long patientId) {
        String sql = "SELECT * FROM Factures WHERE patientId = ? ORDER BY dateFacture DESC";
        return executeQueryWithLong(sql, patientId);
    }

    @Override
    public List<Facture> findByStatut(StatutFacture statut) {
        String sql = "SELECT * FROM Factures WHERE statut = ? ORDER BY dateFacture DESC";
        return executeQueryWithString(sql, statut.name());
    }

    @Override
    public List<Facture> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT * FROM Factures WHERE dateFacture BETWEEN ? AND ? ORDER BY dateFacture DESC";
        List<Facture> factures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) factures.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par période", e);
        }
        return factures;
    }

    @Override
    public Double getTotalByDateRange(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT SUM(totaleFacture) FROM Factures WHERE dateFacture BETWEEN ? AND ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(debut));
            ps.setTimestamp(2, Timestamp.valueOf(fin));
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

    @Override
    public Double getTotalImpaye() {
        String sql = "SELECT SUM(reste) FROM Factures WHERE statut IN ('IMPAYEE', 'PARTIELLE')";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                double total = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : total;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du total impayé", e);
        }
        return 0.0;
    }

    @Override
    public Facture findByConsultationId(Long consultationId) {
        String sql = "SELECT * FROM Factures WHERE consultationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapFacture(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la facture par consultation", e);
        }
        return null;
    }

    // Helper methods
    private List<Facture> executeQuery(String sql) {
        List<Facture> factures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) factures.add(RowMappers.mapFacture(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }

    private List<Facture> executeQueryWithLong(String sql, Long param) {
        List<Facture> factures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) factures.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }

    private List<Facture> executeQueryWithString(String sql, String param) {
        List<Facture> factures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) factures.add(RowMappers.mapFacture(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }
}
