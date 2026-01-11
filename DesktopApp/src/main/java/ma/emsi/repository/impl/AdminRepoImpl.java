package ma.emsi.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import ma.emsi.entities.modules.users.*;
import ma.emsi.repository.common.RowMappers;
import ma.emsi.repository.api.*;

@Data @AllArgsConstructor
public class AdminRepoImpl implements AdminRepo {

    private final Connection c;
    
    private static final String BASE_SELECT = """
        SELECT u.*, s.salaire, s.prime, s.dateRecrutement, s.soldeConge
        FROM Admins a
        JOIN Staffs s ON a.id = s.id
        JOIN Utilisateurs u ON s.id = u.id
        """;

    @Override
    public List<Admin> findAll() {
        String sql = BASE_SELECT + " ORDER BY u.id";
        List<Admin> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapAdmin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Admin findById(Long id) {
        String sql = BASE_SELECT + " WHERE u.id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return RowMappers.mapAdmin(rs);
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Admin admin) {
        // Création multi-tables => service (Utilisateur + Staff + Admin)
        throw new UnsupportedOperationException("Créer un Admin doit passer par un service dédié.");
    }


    @Override
    public void insertAdminFields(Admin a) {
        String sql = "INSERT INTO Admins (id) VALUES (?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("insertAdminFields failed (id=" + a.getId() + ")", e);
        }
    }


    @Override
    public void update(Admin admin) {
        // Update multi-tables => service
        throw new UnsupportedOperationException("Mettre à jour un Admin doit passer par un service dédié.");
    }

    @Override
    public void delete(Admin admin) {
        if (admin != null) deleteById(admin.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Admins WHERE id = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Admin> findAllOrderByNom() {
        String sql = BASE_SELECT + " ORDER BY u.nom";
        List<Admin> out = new ArrayList<>();
        try (
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(RowMappers.mapAdmin(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        String sql = BASE_SELECT + " WHERE u.email = ?";
        try (
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapAdmin(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}

