package ma.emsi.repository.impl.medical;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.entities.modules.medical.DossierMedical;
import ma.emsi.repository.api.medical.DossierMedicalRepo;
import ma.emsi.repository.common.RowMappers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DossierMedicalRepoImpl implements DossierMedicalRepo {

    private Connection connection;

    @Override
    public List<DossierMedical> findAll() {
        String sql = "SELECT * FROM DossiersMedicaux ORDER BY id";
        List<DossierMedical> dossiers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) dossiers.add(RowMappers.mapDossierMedical(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dossiers;
    }

    @Override
    public DossierMedical findById(Long id) {
        String sql = "SELECT * FROM DossiersMedicaux WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapDossierMedical(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(DossierMedical dossier) {
        String sql = """
            INSERT INTO DossiersMedicaux (patientId, dateDeCréation, dateCreation, creePar, modifiePar)
            VALUES (?, ?, COALESCE(?, CURRENT_DATE), ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, dossier.getPatientId());
            ps.setDate(2, Date.valueOf(dossier.getDateDeCréation()));
            ps.setDate(3, dossier.getDateCreation() != null ? Date.valueOf(dossier.getDateCreation()) : null);
            ps.setString(4, dossier.getCreePar());
            ps.setString(5, dossier.getModifiePar());
            
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dossier.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(DossierMedical dossier) {
        String sql = """
            UPDATE DossiersMedicaux
            SET patientId = ?, dateDeCréation = ?, dateDerniereModification = CURRENT_TIMESTAMP, modifiePar = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, dossier.getPatientId());
            ps.setDate(2, Date.valueOf(dossier.getDateDeCréation()));
            ps.setString(3, dossier.getModifiePar());
            ps.setLong(4, dossier.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(DossierMedical dossier) {
        if (dossier != null) deleteById(dossier.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM DossiersMedicaux WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DossierMedical findByPatientId(Long patientId) {
        String sql = "SELECT * FROM DossiersMedicaux WHERE patientId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapDossierMedical(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<DossierMedical> findByDateRange(java.time.LocalDate debut, java.time.LocalDate fin) {
        String sql = "SELECT * FROM DossiersMedicaux WHERE dateDeCréation BETWEEN ? AND ?";
        List<DossierMedical> dossiers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));
            ps.setDate(2, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) dossiers.add(RowMappers.mapDossierMedical(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dossiers;
    }

    @Override
    public boolean existsByPatientId(Long patientId) {
        String sql = "SELECT 1 FROM DossiersMedicaux WHERE patientId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
