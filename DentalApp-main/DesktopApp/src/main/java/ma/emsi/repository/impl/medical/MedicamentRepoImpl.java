package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.FormeMedicament;
import ma.emsi.entities.modules.medical.Medicament;
import ma.emsi.repository.api.medical.MedicamentRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentRepoImpl implements MedicamentRepo {

    private Connection connection;

    @Override
    public List<Medicament> findAll() {
        String sql = "SELECT * FROM Medicaments ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    @Override
    public Medicament findById(Long id) {
        String sql = "SELECT * FROM Medicaments WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapMedicament(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(Medicament medicament) {
        String sql = """
            INSERT INTO Medicaments (nom, laboratoire, type, forme, remboursable, prixUnitaire, description,
                                     dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medicament.getNom());
            ps.setString(2, medicament.getLaboratoire());
            ps.setString(3, medicament.getType());
            ps.setString(4, medicament.getForme() != null ? medicament.getForme().name() : null);
            ps.setBoolean(5, medicament.isRemboursable());
            ps.setDouble(6, medicament.getPrixUnitaire());
            ps.setString(7, medicament.getDescription());
            ps.setDate(8, medicament.getDateCreation() != null ? Date.valueOf(medicament.getDateCreation()) : null);
            ps.setString(9, medicament.getCreePar());
            ps.setString(10, medicament.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) medicament.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Medicament medicament) {
        String sql = """
            UPDATE Medicaments
            SET nom = ?, laboratoire = ?, type = ?, forme = ?, remboursable = ?, prixUnitaire = ?, description = ?,
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, medicament.getNom());
            ps.setString(2, medicament.getLaboratoire());
            ps.setString(3, medicament.getType());
            ps.setString(4, medicament.getForme() != null ? medicament.getForme().name() : null);
            ps.setBoolean(5, medicament.isRemboursable());
            ps.setDouble(6, medicament.getPrixUnitaire());
            ps.setString(7, medicament.getDescription());
            ps.setString(8, medicament.getModifiePar());
            ps.setLong(9, medicament.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Medicament medicament) {
        if (medicament != null) deleteById(medicament.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Medicaments WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Medicament> searchByNom(String keyword) {
        String sql = "SELECT * FROM Medicaments WHERE nom LIKE ? ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByLaboratoire(String laboratoire) {
        String sql = "SELECT * FROM Medicaments WHERE laboratoire = ? ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, laboratoire);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByForme(FormeMedicament forme) {
        String sql = "SELECT * FROM Medicaments WHERE forme = ? ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, forme.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findRemboursables() {
        String sql = "SELECT * FROM Medicaments WHERE remboursable = TRUE ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByType(String type) {
        String sql = "SELECT * FROM Medicaments WHERE type = ? ORDER BY nom";
        List<Medicament> medicaments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) medicaments.add(RowMappers.mapMedicament(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return medicaments;
    }
}
