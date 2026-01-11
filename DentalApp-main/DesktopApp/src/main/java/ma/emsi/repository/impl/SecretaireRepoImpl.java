package ma.emsi.repository.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.entities.modules.users.Secretaire;
import ma.emsi.repository.common.RowMappers;
import ma.emsi.repository.api.SecretaireRepo;

@Data @AllArgsConstructor
public class SecretaireRepoImpl implements SecretaireRepo {

    private final Connection c;
    
    private static final String BASE_SELECT = """
        SELECT u.*, s.salaire, s.prime, s.dateRecrutement, s.soldeConge,
               sec.numCNSS, sec.commission
        FROM Secretaires sec
        JOIN Staffs s ON sec.id = s.id
        JOIN Utilisateurs u ON s.id = u.id
        """;

    @Override
    public List<Secretaire> findAll() {
        String sql = BASE_SELECT + " ORDER BY u.id";
        List<Secretaire> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapSecretaire(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Secretaire findById(Long id) {
        String sql = BASE_SELECT + " WHERE u.id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapSecretaire(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Secretaire secretaire) {
        // Création multi-tables => service (Utilisateur + Staff + Secretaire)
        throw new UnsupportedOperationException("Créer une Secretaire doit passer par un service dédié.");
    }

    public void insertSecretaireFields(Secretaire s)  {
        String sql = "INSERT INTO Secretaires (id, numCNSS, commission) VALUES (?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, s.getId());
            ps.setString(2, s.getNumCNSS());
            if (s.getCommission() != null) ps.setDouble(3, s.getCommission()); else ps.setNull(3, Types.DOUBLE);
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("insertMedecinFields failed (id=" +s.getId() + ")", e);
        }
    }


    @Override
    public void updateSecretaireFields(Secretaire s) {
        if (s == null || s.getId() == null) {
            throw new IllegalArgumentException("Secretaire invalide (null/id null).");
        }

        String sql = """
            UPDATE Secretaires
               SET numCNSS = ?,
                   commission = ?
             WHERE id = ?
            """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            // numCNSS (nullable)
            if (s.getNumCNSS() != null && !s.getNumCNSS().isBlank()) {
                ps.setString(1, s.getNumCNSS().trim());
            } else {
                ps.setNull(1, Types.VARCHAR);
            }

            // commission (nullable)
            if (s.getCommission() != null) {
                ps.setDouble(2, s.getCommission());
            } else {
                ps.setNull(2, Types.DECIMAL);
            }

            ps.setLong(3, s.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("Aucune secrétaire trouvée à mettre à jour (id=" + s.getId() + ").");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erreur updateSecretaireFields: " + ex.getMessage(), ex);
        }
    }



    @Override
    public void update(Secretaire secretaire) {
        // Update multi-tables => service
        throw new UnsupportedOperationException("Mettre à jour une Secretaire doit passer par un service dédié.");
    }

    @Override
    public void delete(Secretaire secretaire) {
        if (secretaire != null) deleteById(secretaire.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Secretaires WHERE id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Secretaire> findAllOrderByNom() {
        String sql = BASE_SELECT + " ORDER BY u.nom";
        List<Secretaire> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapSecretaire(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Optional<Secretaire> findByNumCNSS(String numCNSS) {
        String sql = BASE_SELECT + " WHERE sec.numCNSS = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, numCNSS);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapSecretaire(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Secretaire> findByCommissionMin(Double minCommission) {
        String sql = BASE_SELECT + " WHERE sec.commission >= ? ORDER BY sec.commission DESC";
        List<Secretaire> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, minCommission);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapSecretaire(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}
