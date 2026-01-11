package ma.emsi.repository.impl;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.common.RowMappers;
import ma.emsi.repository.api.*;


//Impl : (on se base sur Medecins + Staffs + Utilisateurs)
@Data
@AllArgsConstructor
public class MedecinRepoImpl implements MedecinRepo {

    private final Connection c;

    private static final String BASE_SELECT = """
        SELECT u.*, s.salaire, s.prime, s.dateRecrutement, s.soldeConge,
               m.specialite
        FROM Medecins m
        JOIN Staffs s ON m.id = s.id
        JOIN Utilisateurs u ON s.id = u.id
        """;

    @Override
    public List<Medecin> findAll() {
        String sql = BASE_SELECT + " ORDER BY u.id";
        List<Medecin> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapMedecin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Medecin findById(Long id) {
        String sql = BASE_SELECT + " WHERE u.id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapMedecin(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Medecin m) {
        // On crée d'abord Utilisateur, puis Staff, puis Medecin
        throw new UnsupportedOperationException("Créer un Medecin doit passer par un service dédié (création utilisateur + staff + médecin).");
    }

    public void insertMedecinFields(Medecin m)  {
        String sql = "INSERT INTO Medecins (id, specialite) VALUES (?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, m.getId());
            ps.setString(2, m.getSpecialite());
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException("insertMedecinFields failed (id=" +m.getId() + ")", e);
        }
    }

    @Override
    public void updateMedecinFields(Medecin m) {
        if (m == null || m.getId() == null) {
            throw new IllegalArgumentException("Medecin invalide (null/id null).");
        }

        String sql = """
            UPDATE Medecins
               SET specialite = ?
                   -- , agenda_id = ?   -- à activer si tu utilises agenda_id
             WHERE id = ?
            """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            // specialite (nullable)
            if (m.getSpecialite() != null && !m.getSpecialite().isBlank())
                ps.setString(1, m.getSpecialite().trim());
            else
                ps.setNull(1, Types.VARCHAR);

            // agenda_id : exemple si vous l’activez plus tard
            // if (m.getAgendaId() != null) ps.setLong(2, m.getAgendaId());
            // else ps.setNull(2, Types.BIGINT);

            ps.setLong(2, m.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("Aucun médecin trouvé à mettre à jour (id=" + m.getId() + ").");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erreur updateMedecinFields: " + ex.getMessage(), ex);
        }
    }
    

    @Override
    public void update(Medecin m) {
        // Update Utilisateur, Staff, Medecin
        throw new UnsupportedOperationException("Mettre à jour un Medecin doit passer par un service dédié.");
    }

    @Override
    public void delete(Medecin m) {
        if (m != null) deleteById(m.getId());
    }

    @Override
    public void deleteById(Long id) {
        // Supprimer Medecin => cascade via FK si bien configuré (Medecins -> Staffs -> Utilisateurs)
        String sql = "DELETE FROM Medecins WHERE id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Medecin> findAllOrderByNom() {
        String sql = BASE_SELECT + " ORDER BY u.nom";
        List<Medecin> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapMedecin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public List<Medecin> findBySpecialite(String specialiteLike) {
        String sql = BASE_SELECT + " WHERE m.specialite LIKE ? ORDER BY u.nom";
        List<Medecin> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + specialiteLike + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(RowMappers.mapMedecin(rs));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}
