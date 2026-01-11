package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.Acte;
import ma.emsi.repository.api.medical.ActeRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActeRepoImpl implements ActeRepo {

    private Connection connection;

    @Override
    public List<Acte> findAll() {
        String sql = "SELECT * FROM Actes ORDER BY categorie, libelle";
        List<Acte> actes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) actes.add(RowMappers.mapActe(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actes;
    }

    @Override
    public Acte findById(Long id) {
        String sql = "SELECT * FROM Actes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapActe(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(Acte acte) {
        String sql = """
            INSERT INTO Actes (libelle, categorie, prixDeBase, dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setDate(4, acte.getDateCreation() != null ? Date.valueOf(acte.getDateCreation()) : null);
            ps.setString(5, acte.getCreePar());
            ps.setString(6, acte.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) acte.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Acte acte) {
        String sql = """
            UPDATE Actes
            SET libelle = ?, categorie = ?, prixDeBase = ?, 
                dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, acte.getLibelle());
            ps.setString(2, acte.getCategorie());
            ps.setDouble(3, acte.getPrixDeBase());
            ps.setString(4, acte.getModifiePar());
            ps.setLong(5, acte.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Acte acte) {
        if (acte != null) deleteById(acte.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Actes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Acte> findByCategorie(String categorie) {
        String sql = "SELECT * FROM Actes WHERE categorie = ? ORDER BY libelle";
        List<Acte> actes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) actes.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actes;
    }

    @Override
    public List<Acte> searchByLibelle(String keyword) {
        String sql = "SELECT * FROM Actes WHERE libelle LIKE ? ORDER BY libelle";
        List<Acte> actes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) actes.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actes;
    }

    @Override
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT categorie FROM Actes ORDER BY categorie";
        List<String> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) categories.add(rs.getString("categorie"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public List<Acte> findByPrixRange(Double minPrix, Double maxPrix) {
        String sql = "SELECT * FROM Actes WHERE prixDeBase BETWEEN ? AND ? ORDER BY prixDeBase";
        List<Acte> actes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, minPrix);
            ps.setDouble(2, maxPrix);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) actes.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actes;
    }
}
