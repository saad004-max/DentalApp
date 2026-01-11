package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.enums.StatutRDV;
import ma.emsi.entities.modules.medical.RDV;
import ma.emsi.repository.api.medical.RDVRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RDVRepoImpl implements RDVRepo {

    private Connection connection;

    @Override
    public List<RDV> findAll() {
        String sql = "SELECT * FROM RendezVous ORDER BY date DESC, heure DESC";
        return executeQuery(sql);
    }

    @Override
    public RDV findById(Long id) {
        String sql = "SELECT * FROM RendezVous WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapRDV(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(RDV rdv) {
        String sql = """
            INSERT INTO RendezVous (patientId, medecinId, date, heure, motif, statut, noteMedecin,
                                    dateCreation, creePar, modifiePar)
            VALUES (?, ?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, rdv.getPatientId());
            ps.setLong(2, rdv.getMedecinId());
            ps.setDate(3, Date.valueOf(rdv.getDate()));
            ps.setTime(4, Time.valueOf(rdv.getHeure()));
            ps.setString(5, rdv.getMotif());
            ps.setString(6, rdv.getStatut().name());
            ps.setString(7, rdv.getNoteMedecin());
            ps.setDate(8, rdv.getDateCreation() != null ? Date.valueOf(rdv.getDateCreation()) : null);
            ps.setString(9, rdv.getCreePar());
            ps.setString(10, rdv.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) rdv.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(RDV rdv) {
        String sql = """
            UPDATE RendezVous
            SET patientId = ?, medecinId = ?, date = ?, heure = ?, motif = ?, statut = ?, 
                noteMedecin = ?, dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, rdv.getPatientId());
            ps.setLong(2, rdv.getMedecinId());
            ps.setDate(3, Date.valueOf(rdv.getDate()));
            ps.setTime(4, Time.valueOf(rdv.getHeure()));
            ps.setString(5, rdv.getMotif());
            ps.setString(6, rdv.getStatut().name());
            ps.setString(7, rdv.getNoteMedecin());
            ps.setString(8, rdv.getModifiePar());
            ps.setLong(9, rdv.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(RDV rdv) {
        if (rdv != null) deleteById(rdv.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM RendezVous WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RDV> findByPatientId(Long patientId) {
        String sql = "SELECT * FROM RendezVous WHERE patientId = ? ORDER BY date DESC";
        return executeQueryWithLong(sql, patientId);
    }

    @Override
    public List<RDV> findByMedecinId(Long medecinId) {
        String sql = "SELECT * FROM RendezVous WHERE medecinId = ? ORDER BY date DESC";
        return executeQueryWithLong(sql, medecinId);
    }

    @Override
    public List<RDV> findByStatut(StatutRDV statut) {
        String sql = "SELECT * FROM RendezVous WHERE statut = ? ORDER BY date DESC";
        return executeQueryWithString(sql, statut.name());
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        String sql = "SELECT * FROM RendezVous WHERE date = ? ORDER BY heure";
        return executeQueryWithDate(sql, date);
    }

    @Override
    public List<RDV> findByMedecinAndDate(Long medecinId, LocalDate date) {
        String sql = "SELECT * FROM RendezVous WHERE medecinId = ? AND date = ? ORDER BY heure";
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }

    @Override
    public List<RDV> findByDateRange(LocalDate debut, LocalDate fin) {
        String sql = "SELECT * FROM RendezVous WHERE date BETWEEN ? AND ? ORDER BY date, heure";
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }

    @Override
    public boolean hasConflict(Long medecinId, LocalDate date, LocalTime heure) {
        String sql = """
            SELECT 1 FROM RendezVous 
            WHERE medecinId = ? AND date = ? AND heure = ? AND statut != 'ANNULE'
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, medecinId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(heure));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RDV> findEnAttente() {
        String sql = "SELECT * FROM RendezVous WHERE statut = 'EN_ATTENTE' ORDER BY date, heure";
        return executeQuery(sql);
    }

    // Helper methods
    private List<RDV> executeQuery(String sql) {
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }

    private List<RDV> executeQueryWithLong(String sql, Long param) {
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }

    private List<RDV> executeQueryWithString(String sql, String param) {
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }

    private List<RDV> executeQueryWithDate(String sql, LocalDate date) {
        List<RDV> rdvs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rdvs.add(RowMappers.mapRDV(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rdvs;
    }
}
