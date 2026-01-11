package ma.emsi.repository.impl;

import ma.emsi.repository.api.UserRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.entities.modules.users.Utilisateur;
import ma.emsi.repository.common.RowMappers;

@Data  @AllArgsConstructor @NoArgsConstructor
public class UserRepoImpl implements UserRepo {

    private Connection connection;

    @Override
    public void updatePassword(Long userId, String encodedPassword) {
        String sql = "UPDATE Utilisateurs SET motDePasse=?, dateDerniereModification=CURRENT_TIMESTAMP WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, encodedPassword);
            ps.setLong(2, userId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new IllegalStateException("Aucun utilisateur (id=" + userId + ")");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Utilisateur> findByName(String name) {
        String sql = "SELECT * FROM Utilisateurs WHERE nom = ?";
        List<Utilisateur> out = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Utilisateur findByEmail(String email) {
        Utilisateur u = null;
        String sql = "SELECT * FROM Utilisateurs WHERE email = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u =  RowMappers.mapUtilisateur(rs);

            }
        } catch (SQLException e) {
            ConsoleLogger.error("Problème Repo", e);
        }

        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);
        return u;
    }

    @Override
    public Utilisateur findByLogin(String login) {
        Utilisateur u = null;
        String sql = "SELECT * FROM Utilisateurs WHERE login = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u =  RowMappers.mapUtilisateur(rs);

            }
        } catch (SQLException e) {
            ConsoleLogger.error("Problème Repo", e);
        }

        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);
        return u;
    }

    @Override
    public Utilisateur findByLoginAndPassword(String login, String password) {
        Utilisateur u = null;
        String sql = "SELECT * FROM Utilisateurs WHERE login = ? and motDePasse = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u= RowMappers.mapUtilisateur(rs);

            }
        } catch (SQLException e) {  ConsoleLogger.error("Problème Repo", e); }

        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);
        return u;
    }


    @Override
    public List<Utilisateur> findByAdresse(String adresse) {
        String sql = "SELECT * FROM Utilisateurs WHERE adresse = ?";
        List<Utilisateur> out = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
        }  catch (SQLException e) {
            ConsoleLogger.error("Problème Repo", e);
        }

        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);

        return out;
    }

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT * FROM Utilisateurs ORDER BY id";
        List<Utilisateur> out = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
        }   catch (SQLException e) {
            ConsoleLogger.error("Problème Repo", e);
        }
        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);

        return out;
    }

    @Override
    public Utilisateur findById(Long id) {

        Utilisateur u = null;
        String sql = "SELECT * FROM Utilisateurs WHERE id = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) u= RowMappers.mapUtilisateur(rs);
            }
        } catch (SQLException e) {  ConsoleLogger.error("Problème Repo", e); }

        ConsoleLogger.info("[JDBC USERREPO SQL] " + sql);
        return u;

    }




    @Override
    public void create(Utilisateur u) {
        String sql = """
            INSERT INTO Utilisateurs
            (nom, email, adresse, cin, tel, sexe, login, motDePasse,
             lastLoginDate, dateNaissance, dateCreation, creePar, modifiePar)
            VALUES (?,?,?,?,?,?,?,?,
                    ?,?,
                    COALESCE(?, CURRENT_DATE),
                    ?, ?)
            """;
        try (
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getAdresse());
            ps.setString(4, u.getCin());
            ps.setString(5, u.getTel());
            ps.setString(6, u.getSexe().name());
            ps.setString(7, u.getLogin());
            ps.setString(8, u.getMotDePasse());

            if (u.getLastLoginDate() != null) ps.setDate(9, Date.valueOf(u.getLastLoginDate()));
            else ps.setNull(9, Types.DATE);

            if (u.getDateNaissance() != null) ps.setDate(10, Date.valueOf(u.getDateNaissance()));
            else ps.setNull(10, Types.DATE);

            if (u.getDateCreation() != null) ps.setDate(11, Date.valueOf(u.getDateCreation()));
            else ps.setNull(11, Types.DATE);

            ps.setString(12, u.getCreePar());
            ps.setString(13, u.getModifiePar());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getLong(1));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Utilisateur u) {

        String sql = """
UPDATE Utilisateurs
   SET prenom=?,
       nom=?,
       email=?,
       tel=?,
       adresse=?,
       cin=?,
       sexe=?,
       avatar=?,
       dateNaissance=?,
       dateDerniereModification = CURRENT_TIMESTAMP,
       modifiePar = ?
 WHERE id=?
""";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getPrenom());
            ps.setString(2, u.getNom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getTel());
            ps.setString(5, u.getAdresse());
            ps.setString(6, u.getCin());
            ps.setString(7, u.getSexe() != null ? u.getSexe().name() : null);

            if (u.getAvatar() != null && !u.getAvatar().isBlank())
                ps.setString(8, u.getAvatar().trim());
            else ps.setNull(8, Types.VARCHAR);

            if (u.getDateNaissance() != null)
                ps.setDate(9, Date.valueOf(u.getDateNaissance()));
            else ps.setNull(9, Types.DATE);

            ps.setString(10, u.getModifiePar()); // si tu gères
            ps.setLong(11, u.getId());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(Utilisateur u) {
        if (u != null) deleteById(u.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Utilisateurs WHERE id = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM Utilisateurs WHERE email = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT 1 FROM Utilisateurs WHERE login = ?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Utilisateur> searchByNom(String keyword) {
        String sql = "SELECT * FROM Utilisateurs WHERE nom LIKE ? ORDER BY nom";
        List<Utilisateur> out = new ArrayList<>();
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Utilisateur> findPage(int limit, int offset) {
        String sql = "SELECT * FROM Utilisateurs ORDER BY id LIMIT ? OFFSET ?";
        List<Utilisateur> out = new ArrayList<>();
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapUtilisateur(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<String> getRoleLibellesOfUser(Long utilisateurId) {
        String sql = """
            SELECT r.libelle
            FROM Utilisateur_Roles ur
            JOIN Roles r ON ur.role_id = r.id
            WHERE ur.utilisateur_id = ?
            """;
        List<String> out = new ArrayList<>();
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString("libelle"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public void addRoleToUser(Long utilisateurId, Long roleId) {
        String sql = "INSERT IGNORE INTO Utilisateur_Roles(utilisateur_id, role_id) VALUES(?,?)";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            ps.setLong(2, roleId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void removeRoleFromUser(Long utilisateurId, Long roleId) {
        String sql = "DELETE FROM Utilisateur_Roles WHERE utilisateur_id=? AND role_id=?";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, utilisateurId);
            ps.setLong(2, roleId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


}
