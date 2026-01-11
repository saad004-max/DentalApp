package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.Ordonnance;
import ma.emsi.repository.api.medical.OrdonnanceRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdonnanceRepoImpl implements OrdonnanceRepo {

    private Connection connection;

    @Override
    public List<Ordonnance> findAll() {
        String sql = "SELECT * FROM Ordonnances ORDER BY date DESC";
        return executeQuery(sql);
    }

    @Override
    public Ordonnance findById(Long id) {
        String sql = "SELECT * FROM Ordonnances WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapOrdonnance(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'ordonnance", e);
        }
        return null;
    }

    @Override
    public void create(Ordonnance ordonnance) {
        String sql = """
            INSERT INTO Ordonnances (consultationId, date, dateCreation, creePar, modifiePar)
            VALUES (?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, ordonnance.getConsultationId());
            ps.setDate(2, Date.valueOf(ordonnance.getDate()));
            ps.setDate(3, ordonnance.getDateCreation() != null ? Date.valueOf(ordonnance.getDateCreation()) : null);
            ps.setString(4, ordonnance.getCreePar());
            ps.setString(5, ordonnance.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) ordonnance.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'ordonnance", e);
        }
    }

    @Override
    public void update(Ordonnance ordonnance) {
        String sql = """
            UPDATE Ordonnances
            SET consultationId = ?, date = ?, dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, ordonnance.getConsultationId());
            ps.setDate(2, Date.valueOf(ordonnance.getDate()));
            ps.setString(3, ordonnance.getModifiePar());
            ps.setLong(4, ordonnance.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'ordonnance", e);
        }
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        if (ordonnance != null) deleteById(ordonnance.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Ordonnances WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'ordonnance", e);
        }
    }

    @Override
    public Ordonnance findByConsultationId(Long consultationId) {
        String sql = "SELECT * FROM Ordonnances WHERE consultationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapOrdonnance(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération par consultation", e);
        }
        return null;
    }

    @Override
    public List<Ordonnance> findByDateRange(LocalDate debut, LocalDate fin) {
        String sql = "SELECT * FROM Ordonnances WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        List<Ordonnance> ordonnances = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ordonnances.add(RowMappers.mapOrdonnance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par période", e);
        }
        return ordonnances;
    }

    @Override
    public List<Ordonnance> findByMedecinId(Long medecinId) {
        String sql = """
            SELECT o.* FROM Ordonnances o
            JOIN Consultations c ON o.consultationId = c.id
            WHERE c.medecinId = ?
            ORDER BY o.date DESC
            """;
        List<Ordonnance> ordonnances = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ordonnances.add(RowMappers.mapOrdonnance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par médecin", e);
        }
        return ordonnances;
    }

    // Helper method
    private List<Ordonnance> executeQuery(String sql) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ordonnances.add(RowMappers.mapOrdonnance(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordonnances;
    }
}
