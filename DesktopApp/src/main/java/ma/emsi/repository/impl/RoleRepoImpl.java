package ma.emsi.repository.impl;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.entities.modules.enums.RoleType;
import ma.emsi.entities.modules.users.Role;
import ma.emsi.repository.common.RowMappers;
import ma.emsi.repository.api.RoleRepo;

@Data @AllArgsConstructor
public class RoleRepoImpl implements RoleRepo {

    private final Connection c;

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM Roles ORDER BY id";
        List<Role> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapRole(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Role findById(Long id) {
        String sql = "SELECT * FROM Roles WHERE id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRole(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Role> findByLibelle(String libelle) {
        String sql = "SELECT * FROM Roles WHERE libelle = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapRole(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Optional<Role> findByType(RoleType type) {
        final String sql = "SELECT * FROM Roles WHERE type = ?";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(RowMappers.mapRole(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByType(" + type + ")", e);
        }
    }

    @Override
    public List<String> getPrivileges(Long roleId) {
        String sql = "SELECT privilege FROM Role_Privileges WHERE role_id = ? ORDER BY privilege";
        List<String> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString("privilege"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addPrivilege(Long roleId, String privilege) {
        String sql = "INSERT IGNORE INTO Role_Privileges(role_id, privilege) VALUES(?,?)";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            ps.setString(2, privilege);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removePrivilege(Long roleId, String privilege) {
        String sql = "DELETE FROM Role_Privileges WHERE role_id=? AND privilege=?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            ps.setString(2, privilege);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByLibelle(String libelle) {
        String sql = "SELECT 1 FROM Roles WHERE libelle = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Role> findRolesByUtilisateurId(Long utilisateurId) {
        String sql = """
            SELECT r.*
            FROM Roles r
            JOIN Utilisateur_Roles ur ON ur.role_id = r.id
            WHERE ur.utilisateur_id = ?
            ORDER BY r.type
            """;

        List<Role> out = new ArrayList<>();

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, utilisateurId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role r = RowMappers.mapRole(rs);
                    r.setPrivileges(loadPrivileges(c, r.getId()));
                    out.add(r);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    @Override
    public void assignRoleToUser(Long utilisateurId, Long roleId) {
        final String sql = """
            INSERT INTO Utilisateur_Roles (utilisateur_id, role_id)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE role_id = role_id
            """;
        // Le ON DUPLICATE KEY UPDATE évite une exception si la liaison existe déjà.

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, utilisateurId);
            ps.setLong(2, roleId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur assignRoleToUser(" +
                                       utilisateurId + ", " + roleId + ")", e);
        }
    }



    @Override
    public void removeRoleFromUser(Long utilisateurId, Long roleId) {
        final String sql =
                "DELETE FROM Utilisateur_Roles WHERE utilisateur_id = ? AND role_id = ?";

        try (
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, utilisateurId);
            ps.setLong(2, roleId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur removeRoleFromUser(" +
                                       utilisateurId + ", " + roleId + ")", e);
        }
    }



    @Override
    public void create(Role r) {
        String sql = """
            INSERT INTO Roles(libelle, type, dateCreation, creePar, modifiePar)
            VALUES(?,?,?,?,?)
            """;
        try (
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getLibelle());
            ps.setString(2, r.getType().name());
            if (r.getDateCreation() != null) ps.setDate(3, Date.valueOf(r.getDateCreation()));
            else ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setString(4, r.getCreePar());
            ps.setString(5, r.getModifiePar());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Role r) {
        String sql = """
            UPDATE Roles SET libelle=?, type=?, dateCreation=?, 
                             dateDerniereModification=CURRENT_TIMESTAMP,
                             creePar=?, modifiePar=? 
            WHERE id=?
            """;
        try (
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getLibelle());
            ps.setString(2, r.getType().name());
            if (r.getDateCreation() != null) ps.setDate(3, Date.valueOf(r.getDateCreation()));
            else ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setString(4, r.getCreePar());
            ps.setString(5, r.getModifiePar());
            ps.setLong(6, r.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Role r) {
        if (r != null) deleteById(r.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Roles WHERE id = ?";
        try (
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }



    // -------------------------------
    //   Helpers : Privileges
    // -------------------------------

    private List<String> loadPrivileges(Connection c, Long roleId) throws SQLException {
        List<String> out = new ArrayList<>();

        String sql = "SELECT privilege FROM Role_Privileges WHERE role_id = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(rs.getString("privilege"));
                }
            }
        }
        return out;
    }

    private void deletePrivileges(Connection c, Long roleId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM Role_Privileges WHERE role_id = ?")) {
            ps.setLong(1, roleId);
            ps.executeUpdate();
        }
    }

    private void insertPrivileges(Connection c, Role r) throws SQLException {
        if (r.getPrivileges() == null || r.getPrivileges().isEmpty()) return;

        String sql = "INSERT INTO Role_Privileges(role_id, privilege) VALUES(?,?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            for (String privilege : r.getPrivileges()) {
                ps.setLong(1, r.getId());
                ps.setString(2, privilege);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
