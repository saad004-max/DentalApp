package ma.emsi.repository.common;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import ma.emsi.entities.modules.enums.*;
import ma.emsi.entities.modules.users.*;

public final class RowMappers {

    private RowMappers(){}

    // ... mapPatient & mapAntecedent déjà présents

    public static Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getLong("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setAdresse(rs.getString("adresse"));
        u.setCin(rs.getString("cin"));
        u.setTel(rs.getString("tel"));
        u.setAvatar(rs.getString("avatar"));
        u.setSexe(Sexe.valueOf(rs.getString("sexe")));
        u.setLogin(rs.getString("login"));
        u.setMotDePasse(rs.getString("motDePasse"));

        Date ld = rs.getDate("lastLoginDate");
        if (ld != null) u.setLastLoginDate(ld.toLocalDate());
        Date dn = rs.getDate("dateNaissance");
        if (dn != null) u.setDateNaissance(dn.toLocalDate());

        Date dc = rs.getDate("dateCreation");
        if (dc != null) u.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) u.setDateDerniereModification(dm.toLocalDateTime());
        u.setCreePar(rs.getString("creePar"));
        u.setModifiePar(rs.getString("modifiePar"));

        return u;
    }

    public static Role mapRole(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setId(rs.getLong("id"));
        r.setLibelle(rs.getString("libelle"));
        r.setType(RoleType.valueOf(rs.getString("type")));

        Date dc = rs.getDate("dateCreation");
        if (dc != null) r.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) r.setDateDerniereModification(dm.toLocalDateTime());
        r.setCreePar(rs.getString("creePar"));
        r.setModifiePar(rs.getString("modifiePar"));

        return r;
    }

    // ------- Staff (pour la navigation Cabinet <-> Staff) -------
    public static Staff mapStaff(ResultSet rs) throws SQLException {
        Staff s = new Staff();

        // Champs Utilisateur hérités
        s.setId(rs.getLong("id"));
        s.setNom(rs.getString("nom"));
        s.setEmail(rs.getString("email"));
        s.setAdresse(rs.getString("adresse"));
        s.setCin(rs.getString("cin"));
        s.setTel(rs.getString("tel"));
        s.setPrenom(rs.getString("prenom"));
        s.setAvatar(rs.getString("avatar"));
        s.setSexe(Sexe.valueOf(rs.getString("sexe")));
        s.setLogin(rs.getString("login"));
        s.setMotDePasse(rs.getString("motDePasse"));

        var lastLogin = rs.getDate("lastLoginDate");
        if (lastLogin != null) s.setLastLoginDate(lastLogin.toLocalDate());
        var dn = rs.getDate("dateNaissance");
        if (dn != null) s.setDateNaissance(dn.toLocalDate());

        var dc = rs.getDate("dateCreation");
        if (dc != null) s.setDateCreation(dc.toLocalDate());
        var dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) s.setDateDerniereModification(dm.toLocalDateTime());
        s.setCreePar(rs.getString("creePar"));
        s.setModifiePar(rs.getString("modifiePar"));

        // Champs Staff
        double salaire = rs.getDouble("salaire");
        if (!rs.wasNull()) s.setSalaire(salaire);

        double prime = rs.getDouble("prime");
        if (!rs.wasNull()) s.setPrime(prime);

        var dr = rs.getDate("dateRecrutement");
        if (dr != null) s.setDateRecrutement(dr.toLocalDate());

        int solde = rs.getInt("soldeConge");
        if (!rs.wasNull()) s.setSoldeConge(solde);

        return s;
    }

    public static Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin a = new Admin();
        // On réutilise la même logique que Staff
        // (Admin n'ajoute pas de champs)
        // => on remplit les champs hérités "à la main"

        a.setId(rs.getLong("id"));
        a.setNom(rs.getString("nom"));
        a.setEmail(rs.getString("email"));
        a.setAdresse(rs.getString("adresse"));
        a.setCin(rs.getString("cin"));
        a.setTel(rs.getString("tel"));
        a.setPrenom(rs.getString("prenom"));
        a.setAvatar(rs.getString("avatar"));
        a.setSexe(Sexe.valueOf(rs.getString("sexe")));
        a.setLogin(rs.getString("login"));
        a.setMotDePasse(rs.getString("motDePasse"));

        var lastLogin = rs.getDate("lastLoginDate");
        if (lastLogin != null) a.setLastLoginDate(lastLogin.toLocalDate());
        var dn = rs.getDate("dateNaissance");
        if (dn != null) a.setDateNaissance(dn.toLocalDate());

        var dc = rs.getDate("dateCreation");
        if (dc != null) a.setDateCreation(dc.toLocalDate());
        var dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) a.setDateDerniereModification(dm.toLocalDateTime());
        a.setCreePar(rs.getString("creePar"));
        a.setModifiePar(rs.getString("modifiePar"));

        double salaire = rs.getDouble("salaire");
        if (!rs.wasNull()) a.setSalaire(salaire);

        double prime = rs.getDouble("prime");
        if (!rs.wasNull()) a.setPrime(prime);

        var dr = rs.getDate("dateRecrutement");
        if (dr != null) a.setDateRecrutement(dr.toLocalDate());

        int solde = rs.getInt("soldeConge");
        if (!rs.wasNull()) a.setSoldeConge(solde);


        return a;
    }


    public static Medecin mapMedecin(ResultSet rs) throws SQLException {
        Medecin m = new Medecin();

        // Champs Utilisateur + Staff
        m.setId(rs.getLong("id"));
        m.setNom(rs.getString("nom"));
        m.setEmail(rs.getString("email"));
        m.setAdresse(rs.getString("adresse"));
        m.setCin(rs.getString("cin"));
        m.setTel(rs.getString("tel"));
        m.setPrenom(rs.getString("prenom"));
        m.setAvatar(rs.getString("avatar"));
        m.setSexe(Sexe.valueOf(rs.getString("sexe")));
        m.setLogin(rs.getString("login"));
        m.setMotDePasse(rs.getString("motDePasse"));

        var lastLogin = rs.getDate("lastLoginDate");
        if (lastLogin != null) m.setLastLoginDate(lastLogin.toLocalDate());
        var dn = rs.getDate("dateNaissance");
        if (dn != null) m.setDateNaissance(dn.toLocalDate());

        var dc = rs.getDate("dateCreation");
        if (dc != null) m.setDateCreation(dc.toLocalDate());
        var dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) m.setDateDerniereModification(dm.toLocalDateTime());
        m.setCreePar(rs.getString("creePar"));
        m.setModifiePar(rs.getString("modifiePar"));

        double salaire = rs.getDouble("salaire");
        if (!rs.wasNull()) m.setSalaire(salaire);

        double prime = rs.getDouble("prime");
        if (!rs.wasNull()) m.setPrime(prime);

        var dr = rs.getDate("dateRecrutement");
        if (dr != null) m.setDateRecrutement(dr.toLocalDate());

        int solde = rs.getInt("soldeConge");
        if (!rs.wasNull()) m.setSoldeConge(solde);

        // Spécifique medecin
        m.setSpecialite(rs.getString("specialite"));

        return m;
    }

    public static Secretaire mapSecretaire(ResultSet rs) throws SQLException {
        Secretaire s = new Secretaire();

        // Champs Utilisateur + Staff
        s.setId(rs.getLong("id"));
        s.setNom(rs.getString("nom"));
        s.setEmail(rs.getString("email"));
        s.setAdresse(rs.getString("adresse"));
        s.setCin(rs.getString("cin"));
        s.setTel(rs.getString("tel"));
        s.setPrenom(rs.getString("prenom"));
        s.setAvatar(rs.getString("avatar"));
        s.setSexe(Sexe.valueOf(rs.getString("sexe")));
        s.setLogin(rs.getString("login"));
        s.setMotDePasse(rs.getString("motDePasse"));

        var lastLogin = rs.getDate("lastLoginDate");
        if (lastLogin != null) s.setLastLoginDate(lastLogin.toLocalDate());
        var dn = rs.getDate("dateNaissance");
        if (dn != null) s.setDateNaissance(dn.toLocalDate());

        var dc = rs.getDate("dateCreation");
        if (dc != null) s.setDateCreation(dc.toLocalDate());
        var dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) s.setDateDerniereModification(dm.toLocalDateTime());
        s.setCreePar(rs.getString("creePar"));
        s.setModifiePar(rs.getString("modifiePar"));

        double salaire = rs.getDouble("salaire");
        if (!rs.wasNull()) s.setSalaire(salaire);

        double prime = rs.getDouble("prime");
        if (!rs.wasNull()) s.setPrime(prime);

        var dr = rs.getDate("dateRecrutement");
        if (dr != null) s.setDateRecrutement(dr.toLocalDate());

        int solde = rs.getInt("soldeConge");
        if (!rs.wasNull()) s.setSoldeConge(solde);

        // Spécifique Secretaire
        s.setNumCNSS(rs.getString("numCNSS"));

        double commission = rs.getDouble("commission");
        if (!rs.wasNull()) s.setCommission(commission);

        return s;
    }

    // ... autres map si besoin

    // ==================== MEDICAL MODULE MAPPERS ====================
    
    public static ma.emsi.entities.modules.medical.Patient mapPatient(ResultSet rs) throws SQLException {
        var p = new ma.emsi.entities.modules.medical.Patient();
        p.setId(rs.getLong("id"));
        p.setNom(rs.getString("nom"));
        
        Date dateNaissance = rs.getDate("dateDeNaissance");
        if (dateNaissance != null) p.setDateDeNaissance(dateNaissance.toLocalDate());
        
        String sexeStr = rs.getString("sexe");
        if (sexeStr != null) p.setSexe(Sexe.valueOf(sexeStr));
        
        p.setAdresse(rs.getString("adresse"));
        p.setTelephone(rs.getString("telephone"));
        
        String assuranceStr = rs.getString("assurance");
        if (assuranceStr != null) p.setAssurance(Assurance.valueOf(assuranceStr));
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) p.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) p.setDateDerniereModification(dm.toLocalDateTime());
        p.setCreePar(rs.getString("creePar"));
        p.setModifiePar(rs.getString("modifiePar"));
        
        return p;
    }
    
    public static ma.emsi.entities.modules.medical.DossierMedical mapDossierMedical(ResultSet rs) throws SQLException {
        var dm = new ma.emsi.entities.modules.medical.DossierMedical();
        dm.setId(rs.getLong("id"));
        
        Date dateCrea = rs.getDate("dateDeCréation");
        if (dateCrea != null) dm.setDateDeCréation(dateCrea.toLocalDate());
        
        long patientId = rs.getLong("patientId");
        if (!rs.wasNull()) dm.setPatientId(patientId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) dm.setDateCreation(dc.toLocalDate());
        Timestamp dmod = rs.getTimestamp("dateDerniereModification");
        if (dmod != null) dm.setDateDerniereModification(dmod.toLocalDateTime());
        dm.setCreePar(rs.getString("creePar"));
        dm.setModifiePar(rs.getString("modifiePar"));
        
        return dm;
    }
    
    public static ma.emsi.entities.modules.medical.Antecedent mapAntecedent(ResultSet rs) throws SQLException {
        var a = new ma.emsi.entities.modules.medical.Antecedent();
        a.setId(rs.getLong("id"));
        a.setNom(rs.getString("nom"));
        a.setCategorie(rs.getString("categorie"));
        
        String niveauStr = rs.getString("niveauDeRisque");
        if (niveauStr != null) a.setNiveauDeRisque(NiveauRisque.valueOf(niveauStr));
        
        long dossierId = rs.getLong("dossierMedicalId");
        if (!rs.wasNull()) a.setDossierMedicalId(dossierId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) a.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) a.setDateDerniereModification(dm.toLocalDateTime());
        a.setCreePar(rs.getString("creePar"));
        a.setModifiePar(rs.getString("modifiePar"));
        
        return a;
    }
    
    public static ma.emsi.entities.modules.medical.Consultation mapConsultation(ResultSet rs) throws SQLException {
        var c = new ma.emsi.entities.modules.medical.Consultation();
        c.setId(rs.getLong("id"));
        
        Date date = rs.getDate("date");
        if (date != null) c.setDate(date.toLocalDate());
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) c.setStatut(StatutConsultation.valueOf(statutStr));
        
        c.setObservationMedecin(rs.getString("observationMedecin"));
        
        long dossierId = rs.getLong("dossierMedicalId");
        if (!rs.wasNull()) c.setDossierMedicalId(dossierId);
        
        long medecinId = rs.getLong("medecinId");
        if (!rs.wasNull()) c.setMedecinId(medecinId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) c.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) c.setDateDerniereModification(dm.toLocalDateTime());
        c.setCreePar(rs.getString("creePar"));
        c.setModifiePar(rs.getString("modifiePar"));
        
        return c;
    }
    
    public static ma.emsi.entities.modules.medical.Acte mapActe(ResultSet rs) throws SQLException {
        var a = new ma.emsi.entities.modules.medical.Acte();
        a.setId(rs.getLong("id"));
        a.setLibelle(rs.getString("libelle"));
        a.setCategorie(rs.getString("categorie"));
        
        double prix = rs.getDouble("prixDeBase");
        if (!rs.wasNull()) a.setPrixDeBase(prix);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) a.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) a.setDateDerniereModification(dm.toLocalDateTime());
        a.setCreePar(rs.getString("creePar"));
        a.setModifiePar(rs.getString("modifiePar"));
        
        return a;
    }
    
    public static ma.emsi.entities.modules.medical.InterventionMedecin mapInterventionMedecin(ResultSet rs) throws SQLException {
        var i = new ma.emsi.entities.modules.medical.InterventionMedecin();
        i.setId(rs.getLong("id"));
        
        double prix = rs.getDouble("prixDePatient");
        if (!rs.wasNull()) i.setPrixDePatient(prix);
        
        int numDent = rs.getInt("numDent");
        if (!rs.wasNull()) i.setNumDent(numDent);
        
        long consultationId = rs.getLong("consultationId");
        if (!rs.wasNull()) i.setConsultationId(consultationId);
        
        long acteId = rs.getLong("acteId");
        if (!rs.wasNull()) i.setActeId(acteId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) i.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) i.setDateDerniereModification(dm.toLocalDateTime());
        i.setCreePar(rs.getString("creePar"));
        i.setModifiePar(rs.getString("modifiePar"));
        
        return i;
    }
    
    public static ma.emsi.entities.modules.medical.Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        var o = new ma.emsi.entities.modules.medical.Ordonnance();
        o.setId(rs.getLong("id"));
        
        Date date = rs.getDate("date");
        if (date != null) o.setDate(date.toLocalDate());
        
        long consultationId = rs.getLong("consultationId");
        if (!rs.wasNull()) o.setConsultationId(consultationId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) o.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) o.setDateDerniereModification(dm.toLocalDateTime());
        o.setCreePar(rs.getString("creePar"));
        o.setModifiePar(rs.getString("modifiePar"));
        
        return o;
    }
    
    public static ma.emsi.entities.modules.medical.Prescription mapPrescription(ResultSet rs) throws SQLException {
        var p = new ma.emsi.entities.modules.medical.Prescription();
        p.setId(rs.getLong("id"));
        
        int quantite = rs.getInt("quantite");
        if (!rs.wasNull()) p.setQuantite(quantite);
        
        p.setFrequence(rs.getString("frequence"));
        
        int duree = rs.getInt("dureeEnJours");
        if (!rs.wasNull()) p.setDureeEnJours(duree);
        
        long ordonnanceId = rs.getLong("ordonnanceId");
        if (!rs.wasNull()) p.setOrdonnanceId(ordonnanceId);
        
        long medicamentId = rs.getLong("medicamentId");
        if (!rs.wasNull()) p.setMedicamentId(medicamentId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) p.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) p.setDateDerniereModification(dm.toLocalDateTime());
        p.setCreePar(rs.getString("creePar"));
        p.setModifiePar(rs.getString("modifiePar"));
        
        return p;
    }
    
    public static ma.emsi.entities.modules.medical.Medicament mapMedicament(ResultSet rs) throws SQLException {
        var m = new ma.emsi.entities.modules.medical.Medicament();
        m.setId(rs.getLong("id"));
        m.setNom(rs.getString("nom"));
        m.setLaboratoire(rs.getString("laboratoire"));
        m.setType(rs.getString("type"));
        
        String formeStr = rs.getString("forme");
        if (formeStr != null) m.setForme(FormeMedicament.valueOf(formeStr));
        
        boolean remboursable = rs.getBoolean("remboursable");
        m.setRemboursable(remboursable);
        
        double prix = rs.getDouble("prixUnitaire");
        if (!rs.wasNull()) m.setPrixUnitaire(prix);
        
        m.setDescription(rs.getString("description"));
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) m.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) m.setDateDerniereModification(dm.toLocalDateTime());
        m.setCreePar(rs.getString("creePar"));
        m.setModifiePar(rs.getString("modifiePar"));
        
        return m;
    }
    
    public static ma.emsi.entities.modules.medical.RDV mapRDV(ResultSet rs) throws SQLException {
        var r = new ma.emsi.entities.modules.medical.RDV();
        r.setId(rs.getLong("id"));
        
        Date date = rs.getDate("date");
        if (date != null) r.setDate(date.toLocalDate());
        
        var heure = rs.getTime("heure");
        if (heure != null) r.setHeure(heure.toLocalTime());
        
        r.setMotif(rs.getString("motif"));
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) r.setStatut(StatutRDV.valueOf(statutStr));
        
        r.setNoteMedecin(rs.getString("noteMedecin"));
        
        long patientId = rs.getLong("patientId");
        if (!rs.wasNull()) r.setPatientId(patientId);
        
        long medecinId = rs.getLong("medecinId");
        if (!rs.wasNull()) r.setMedecinId(medecinId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) r.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) r.setDateDerniereModification(dm.toLocalDateTime());
        r.setCreePar(rs.getString("creePar"));
        r.setModifiePar(rs.getString("modifiePar"));
        
        return r;
    }
    
    public static ma.emsi.entities.modules.medical.Certificat mapCertificat(ResultSet rs) throws SQLException {
        var c = new ma.emsi.entities.modules.medical.Certificat();
        c.setId(rs.getLong("id"));
        
        Date dateDebut = rs.getDate("dateDebut");
        if (dateDebut != null) c.setDateDebut(dateDebut.toLocalDate());
        
        Date dateFin = rs.getDate("dateFin");
        if (dateFin != null) c.setDateFin(dateFin.toLocalDate());
        
        int duree = rs.getInt("duree");
        if (!rs.wasNull()) c.setDuree(duree);
        
        c.setNoteMedecin(rs.getString("noteMedecin"));
        
        long patientId = rs.getLong("patientId");
        if (!rs.wasNull()) c.setPatientId(patientId);
        
        long medecinId = rs.getLong("medecinId");
        if (!rs.wasNull()) c.setMedecinId(medecinId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) c.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) c.setDateDerniereModification(dm.toLocalDateTime());
        c.setCreePar(rs.getString("creePar"));
        c.setModifiePar(rs.getString("modifiePar"));
        
        return c;
    }
    
    // ==================== FINANCIAL MODULE MAPPERS ====================
    
    public static ma.emsi.entities.modules.financial.SituationFinanciere mapSituationFinanciere(ResultSet rs) throws SQLException {
        var sf = new ma.emsi.entities.modules.financial.SituationFinanciere();
        sf.setId(rs.getLong("id"));
        
        double totaleActes = rs.getDouble("totaleDesActes");
        if (!rs.wasNull()) sf.setTotaleDesActes(totaleActes);
        
        double totalePaye = rs.getDouble("totalePaye");
        if (!rs.wasNull()) sf.setTotalePaye(totalePaye);
        
        double credit = rs.getDouble("credit");
        if (!rs.wasNull()) sf.setCredit(credit);
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) sf.setStatut(StatutFinancier.valueOf(statutStr));
        
        String promoStr = rs.getString("enPromo");
        if (promoStr != null) sf.setEnPromo(EnPromo.valueOf(promoStr));
        
        long patientId = rs.getLong("patientId");
        if (!rs.wasNull()) sf.setPatientId(patientId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) sf.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) sf.setDateDerniereModification(dm.toLocalDateTime());
        sf.setCreePar(rs.getString("creePar"));
        sf.setModifiePar(rs.getString("modifiePar"));
        
        return sf;
    }
    
    public static ma.emsi.entities.modules.financial.Facture mapFacture(ResultSet rs) throws SQLException {
        var f = new ma.emsi.entities.modules.financial.Facture();
        f.setId(rs.getLong("id"));
        
        double totaleFacture = rs.getDouble("totaleFacture");
        if (!rs.wasNull()) f.setTotaleFacture(totaleFacture);
        
        double totalePaye = rs.getDouble("totalePaye");
        if (!rs.wasNull()) f.setTotalePaye(totalePaye);
        
        double reste = rs.getDouble("reste");
        if (!rs.wasNull()) f.setReste(reste);
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) f.setStatut(StatutFacture.valueOf(statutStr));
        
        Timestamp dateFacture = rs.getTimestamp("dateFacture");
        if (dateFacture != null) f.setDateFacture(dateFacture.toLocalDateTime());
        
        long patientId = rs.getLong("patientId");
        if (!rs.wasNull()) f.setPatientId(patientId);
        
        long consultationId = rs.getLong("consultationId");
        if (!rs.wasNull()) f.setConsultationId(consultationId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) f.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) f.setDateDerniereModification(dm.toLocalDateTime());
        f.setCreePar(rs.getString("creePar"));
        f.setModifiePar(rs.getString("modifiePar"));
        
        return f;
    }
    
    // ==================== CABINET MODULE MAPPERS ====================
    
    public static ma.emsi.entities.modules.cabinet.CabinetMedical mapCabinetMedical(ResultSet rs) throws SQLException {
        var cm = new ma.emsi.entities.modules.cabinet.CabinetMedical();
        cm.setId(rs.getLong("id"));
        cm.setNom(rs.getString("nom"));
        cm.setEmail(rs.getString("email"));
        cm.setLogo(rs.getString("logo"));
        cm.setAdresse(rs.getString("adresse"));
        cm.setCin(rs.getString("cin"));
        cm.setTel1(rs.getString("tel1"));
        cm.setTel2(rs.getString("tel2"));
        cm.setSiteWeb(rs.getString("siteWeb"));
        cm.setInstagram(rs.getString("instagram"));
        cm.setFacebook(rs.getString("facebook"));
        cm.setDescription(rs.getString("description"));
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) cm.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) cm.setDateDerniereModification(dm.toLocalDateTime());
        cm.setCreePar(rs.getString("creePar"));
        cm.setModifiePar(rs.getString("modifiePar"));
        
        return cm;
    }
    
    public static ma.emsi.entities.modules.cabinet.Charges mapCharges(ResultSet rs) throws SQLException {
        var c = new ma.emsi.entities.modules.cabinet.Charges();
        c.setId(rs.getLong("id"));
        c.setTitre(rs.getString("titre"));
        c.setDescription(rs.getString("description"));
        
        double montant = rs.getDouble("montant");
        if (!rs.wasNull()) c.setMontant(montant);
        
        Timestamp date = rs.getTimestamp("date");
        if (date != null) c.setDate(date.toLocalDateTime());
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) c.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) c.setDateDerniereModification(dm.toLocalDateTime());
        c.setCreePar(rs.getString("creePar"));
        c.setModifiePar(rs.getString("modifiePar"));
        
        return c;
    }
    
    public static ma.emsi.entities.modules.cabinet.Revenues mapRevenues(ResultSet rs) throws SQLException {
        var r = new ma.emsi.entities.modules.cabinet.Revenues();
        r.setId(rs.getLong("id"));
        r.setTitre(rs.getString("titre"));
        r.setDescription(rs.getString("description"));
        
        double montant = rs.getDouble("montant");
        if (!rs.wasNull()) r.setMontant(montant);
        
        Timestamp date = rs.getTimestamp("date");
        if (date != null) r.setDate(date.toLocalDateTime());
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) r.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) r.setDateDerniereModification(dm.toLocalDateTime());
        r.setCreePar(rs.getString("creePar"));
        r.setModifiePar(rs.getString("modifiePar"));
        
        return r;
    }
    
    public static ma.emsi.entities.modules.cabinet.Notification mapNotification(ResultSet rs) throws SQLException {
        var n = new ma.emsi.entities.modules.cabinet.Notification();
        n.setId(rs.getLong("id"));
        
        String titreStr = rs.getString("titre");
        if (titreStr != null) n.setTitre(TitreNotification.valueOf(titreStr));
        
        n.setMessage(rs.getString("message"));
        
        Date date = rs.getDate("date");
        if (date != null) n.setDate(date.toLocalDate());
        
        var time = rs.getTime("time");
        if (time != null) n.setTime(time.toLocalTime());
        
        String typeStr = rs.getString("type");
        if (typeStr != null) n.setType(TypeNotification.valueOf(typeStr));
        
        String prioriteStr = rs.getString("priorite");
        if (prioriteStr != null) n.setPriorite(PrioriteNotification.valueOf(prioriteStr));
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) n.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) n.setDateDerniereModification(dm.toLocalDateTime());
        n.setCreePar(rs.getString("creePar"));
        n.setModifiePar(rs.getString("modifiePar"));
        
        return n;
    }
    
    public static ma.emsi.entities.modules.cabinet.Statistiques mapStatistiques(ResultSet rs) throws SQLException {
        var s = new ma.emsi.entities.modules.cabinet.Statistiques();
        s.setId(rs.getLong("id"));
        s.setNom(rs.getString("nom"));
        
        String categorieStr = rs.getString("categorie");
        if (categorieStr != null) s.setCategorie(CategorieStatistique.valueOf(categorieStr));
        
        double chiffre = rs.getDouble("chiffre");
        if (!rs.wasNull()) s.setChiffre(chiffre);
        
        Date dateCalcul = rs.getDate("dateCalcul");
        if (dateCalcul != null) s.setDateCalcul(dateCalcul.toLocalDate());
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) s.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) s.setDateDerniereModification(dm.toLocalDateTime());
        s.setCreePar(rs.getString("creePar"));
        s.setModifiePar(rs.getString("modifiePar"));
        
        return s;
    }
    
    public static ma.emsi.entities.modules.cabinet.AgendaMensuel mapAgendaMensuel(ResultSet rs) throws SQLException {
        var a = new ma.emsi.entities.modules.cabinet.AgendaMensuel();
        a.setId(rs.getLong("id"));
        
        String moisStr = rs.getString("mois");
        if (moisStr != null) a.setMois(Mois.valueOf(moisStr));
        
        long medecinId = rs.getLong("medecinId");
        if (!rs.wasNull()) a.setMedecinId(medecinId);
        
        // BaseEntity fields
        Date dc = rs.getDate("dateCreation");
        if (dc != null) a.setDateCreation(dc.toLocalDate());
        Timestamp dm = rs.getTimestamp("dateDerniereModification");
        if (dm != null) a.setDateDerniereModification(dm.toLocalDateTime());
        a.setCreePar(rs.getString("creePar"));
        a.setModifiePar(rs.getString("modifiePar"));
        
        return a;
    }

}
