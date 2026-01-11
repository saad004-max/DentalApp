-- ##########################################################################
-- #                                 PARTIE 1 : TABLES PRINCIPALES          #
-- ##########################################################################

-- Types Enum (Simplifiés en TEXT pour la flexibilité en SQL)

-- Enum pour CabinetMedicale
CREATE TYPE StatutCabinet AS ENUM ('actif', 'fermé', 'en pause'); -- Exemple d'Enum si Statut était un attribut
-- Enum pour Role.privileges
CREATE TYPE Privilege AS ENUM ('lecture', 'ecriture', 'admin', 'moderateur');
-- Enum pour Notification.type et Notification.priorite
CREATE TYPE TypeNotification AS ENUM ('rappel', 'information', 'alerte');
CREATE TYPE PrioriteNotification AS ENUM ('basse', 'normale', 'haute', 'urgente');
-- Enum pour Statistiques.categorie
CREATE TYPE CategorieStatistique AS ENUM ('financiere', 'rendez_vous', 'patient', 'autre');
-- Enum pour AgendaMensuel.mois
CREATE TYPE Mois AS ENUM ('JAN', 'FEV', 'MAR', 'AVR', 'MAI', 'JUN', 'JUL', 'AOU', 'SEP', 'OCT', 'NOV', 'DEC');
-- Enum pour Facture.statut
CREATE TYPE StatutFacture AS ENUM ('payee', 'partiellement_payee', 'impayee', 'annulee');
-- Enum pour Consultation.statut
CREATE TYPE StatutConsultation AS ENUM ('terminee', 'en_cours', 'annulee', 'reportee');
-- Enum pour RDV.statut
CREATE TYPE StatutRDV AS ENUM ('confirme', 'annule', 'en_attente');
-- Enum pour Antecedents.categorie
CREATE TYPE CategorieAntecedent AS ENUM ('medical', 'chirurgical', 'allergie', 'familial', 'social');


-- 1. Utilisateur (Superclasse pour l'héritage)
CREATE TABLE Utilisateur (
                             idUtiliser BIGINT PRIMARY KEY,
                             nom VARCHAR(255) NOT NULL,
                             email VARCHAR(255) UNIQUE NOT NULL,
                             adresse TEXT, -- Adresse
                             tel VARCHAR(20),
                             sexe CHAR(1), -- M ou F
                             login VARCHAR(50) UNIQUE NOT NULL,
                             motDePass VARCHAR(255) NOT NULL,
                             dateCreation TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             dateNaissance DATE
);

-- 2. Role
CREATE TABLE Role (
                      idRole BIGINT PRIMARY KEY,
                      libelle VARCHAR(100) NOT NULL,
    -- privileges est une List<String> dans UML, mieux de le gérer comme un tableau de ENUM ou une table d'association
                      privileges Privilege[] -- Tableau d'ENUMs
);

-- 3. CabinetMedicale
CREATE TABLE CabinetMedicale (
                                 idUser BIGINT PRIMARY KEY, -- Clé étrangère pour l'association 1 à 1 avec Utilisateur
                                 nom VARCHAR(255) NOT NULL,
                                 email VARCHAR(255) UNIQUE NOT NULL,
                                 logo VARCHAR(255),
                                 adresse TEXT, -- Adresse
                                 cin VARCHAR(50),
                                 tel1 VARCHAR(20),
                                 tel2 VARCHAR(20),
                                 siteWeb VARCHAR(255),
                                 instagram VARCHAR(255),
                                 facebook VARCHAR(255),
                                 description TEXT,
                                 CONSTRAINT fk_cabinet_utilisateur FOREIGN KEY (idUser) REFERENCES Utilisateur(idUtiliser)
);

-- 4. Statistiques
CREATE TABLE Statistiques (
                              id BIGINT PRIMARY KEY,
                              nom VARCHAR(255) NOT NULL,
                              categorie CategorieStatistique NOT NULL,
                              chiffre DOUBLE PRECISION,
                              dateCalcul TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    -- Association 1 à * vers CabinetMedicale (via la table d'association Statistique_Cabinet)
                              idCabinet BIGINT NOT NULL,
                              CONSTRAINT fk_stat_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedicale(idUser)
);

-- 5. AgendaMensuel
CREATE TABLE AgendaMensuel (
                               id BIGINT PRIMARY KEY,
                               mois Mois NOT NULL,
                               joursNonDisponible VARCHAR(255)[], -- Liste<Jour> peut être gérée comme un tableau de jours de la semaine (e.g., LUN, MAR...) ou dates

    -- Association 1 à * vers Médecin
                               idMedecin BIGINT NOT NULL,
                               CONSTRAINT fk_agenda_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(idUser) -- Dépend de la création de la table Medecin
);

-- 6. Charges
CREATE TABLE Charges (
                         id BIGINT PRIMARY KEY,
                         titre VARCHAR(255) NOT NULL,
                         description TEXT,
                         montant DOUBLE PRECISION NOT NULL,
                         date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- Association 1 à * vers CabinetMedicale (via la table d'association Charge_Cabinet)
                         idCabinet BIGINT NOT NULL,
                         CONSTRAINT fk_charge_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedicale(idUser)
);

-- 7. Revenus
CREATE TABLE Revenus (
                         id BIGINT PRIMARY KEY,
                         titre VARCHAR(255) NOT NULL,
                         description TEXT,
                         montant DOUBLE PRECISION NOT NULL,
                         date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- Association 1 à * vers CabinetMedicale (via la table d'association Revenu_Cabinet)
                         idCabinet BIGINT NOT NULL,
                         CONSTRAINT fk_revenu_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedicale(idUser)
);

-- 8. Notification
CREATE TABLE Notification (
                              id BIGINT PRIMARY KEY,
                              message TEXT NOT NULL,
                              date DATE NOT NULL,
                              time TIME WITHOUT TIME ZONE NOT NULL,
                              type TypeNotification NOT NULL,
                              priorite PrioriteNotification NOT NULL
    -- La relation * à * avec Utilisateur sera gérée par une table d'association
);

-- ##########################################################################
-- #                                 PARTIE 2 : GESTION DE L'HERITAGE STAFF #
-- ##########################################################################

-- 9. Staff (Hérite de Utilisateur)
CREATE TABLE Staff (
                       idUser BIGINT PRIMARY KEY, -- Clé primaire et clé étrangère vers Utilisateur
                       salaire DOUBLE PRECISION NOT NULL,
                       prime DOUBLE PRECISION,
                       dateRecrutement DATE NOT NULL,
                       soldeConge INT NOT NULL,
                       CONSTRAINT fk_staff_utilisateur FOREIGN KEY (idUser) REFERENCES Utilisateur(idUtiliser)
);

-- 10. Admin (Hérite de Staff - Pas d'attributs spécifiques)
CREATE TABLE Admin (
                       idUser BIGINT PRIMARY KEY, -- Clé primaire et clé étrangère vers Staff
                       CONSTRAINT fk_admin_staff FOREIGN KEY (idUser) REFERENCES Staff(idUser)
);

-- 11. Medecin (Hérite de Staff)
CREATE TABLE Medecin (
                         idUser BIGINT PRIMARY KEY, -- Clé primaire et clé étrangère vers Staff
                         specialite VARCHAR(100),
                         agendaMensuel VARCHAR(255), -- AgendaDocteur (Probablement un objet ou un lien, ici une chaîne)
                         CONSTRAINT fk_medecin_staff FOREIGN KEY (idUser) REFERENCES Staff(idUser)
);

-- 12. Secretaire (Hérite de Staff)
CREATE TABLE Secretaire (
                            idUser BIGINT PRIMARY KEY, -- Clé primaire et clé étrangère vers Staff
                            numCNSS VARCHAR(50),
                            commission DOUBLE PRECISION,
                            CONSTRAINT fk_secretaire_staff FOREIGN KEY (idUser) REFERENCES Staff(idUser)
);

-- ##########################################################################
-- #                                 PARTIE 3 : TABLES MEDICALES            #
-- ##########################################################################

-- 13. Patient
CREATE TABLE Patient (
                         idPatient BIGINT PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         dateNaissance DATE,
                         sexe CHAR(1), -- M ou F
                         adresse TEXT,
                         telephone VARCHAR(20),
                         assurance VARCHAR(100)
);

-- 14. Antecedents
CREATE TABLE Antecedents (
                             idAntecedent BIGINT PRIMARY KEY,
                             nom VARCHAR(255) NOT NULL,
                             categorie CategorieAntecedent NOT NULL,
                             niveauDeRisque VARCHAR(50) -- Enum
    -- La relation * à * avec Patient sera gérée par une table d'association
);

-- 15. DossierMedicale
CREATE TABLE DossierMedicale (
                                 idDM BIGINT PRIMARY KEY,
                                 dateCreation TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Association 1 à 1 vers Patient
                                 idPatient BIGINT UNIQUE NOT NULL, -- UNIQUE pour garantir 1 Dossier par Patient
                                 CONSTRAINT fk_dm_patient FOREIGN KEY (idPatient) REFERENCES Patient(idPatient)
);

-- 16. SituationFinanciere
CREATE TABLE SituationFinanciere (
                                     idSF BIGINT PRIMARY KEY,
                                     totalPrestations DOUBLE PRECISION NOT NULL DEFAULT 0.0,
                                     totalPaye DOUBLE PRECISION NOT NULL DEFAULT 0.0,
                                     reste DOUBLE PRECISION NOT NULL DEFAULT 0.0,
                                     enPromo BOOLEAN NOT NULL DEFAULT FALSE,

    -- Association 1 à 1 vers Patient
                                     idPatient BIGINT UNIQUE NOT NULL, -- UNIQUE pour garantir 1 SituationFinanciere par Patient
                                     CONSTRAINT fk_sf_patient FOREIGN KEY (idPatient) REFERENCES Patient(idPatient)
);

-- 17. Facture
CREATE TABLE Facture (
                         idFacture BIGINT PRIMARY KEY,
                         totalFacture DOUBLE PRECISION NOT NULL,
                         totalPaye DOUBLE PRECISION NOT NULL,
                         reste DOUBLE PRECISION NOT NULL,
                         statut StatutFacture NOT NULL,
                         dateFacture TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    -- Association 1 à * vers SituationFinanciere
                         idSF BIGINT NOT NULL,
                         CONSTRAINT fk_facture_sf FOREIGN KEY (idSF) REFERENCES SituationFinanciere(idSF)
    -- La relation * à * avec Consultation sera gérée par une table d'association
);

-- 18. Consultation
CREATE TABLE Consultation (
                              idConsultation BIGINT PRIMARY KEY,
                              date DATE NOT NULL,
                              statut StatutConsultation NOT NULL,
                              observationMedecin TEXT,

    -- Association 1 à * vers DossierMedicale
                              idDM BIGINT NOT NULL,
                              CONSTRAINT fk_consultation_dm FOREIGN KEY (idDM) REFERENCES DossierMedicale(idDM)
    -- La relation * à * avec Medecin (le médecin qui effectue la consultation) est gérée par une table d'association
);

-- 19. RDV
CREATE TABLE RDV (
                     idRDV BIGINT PRIMARY KEY,
                     date DATE NOT NULL,
                     heure TIME WITHOUT TIME ZONE NOT NULL,
                     motif TEXT,
                     statut StatutRDV NOT NULL,
                     noteMedecin TEXT,

    -- Association 1 à * vers Medecin (le médecin concerné par le RDV)
                     idMedecin BIGINT NOT NULL,
                     CONSTRAINT fk_rdv_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(idUser),

    -- Association 1 à * vers Patient (le patient concerné par le RDV)
                     idPatient BIGINT NOT NULL,
                     CONSTRAINT fk_rdv_patient FOREIGN KEY (idPatient) REFERENCES Patient(idPatient)
);

-- 20. InterventionMedecin
CREATE TABLE InterventionMedecin (
                                     idIM BIGINT PRIMARY KEY,
                                     prixDePatient DOUBLE PRECISION NOT NULL,
                                     numDent INTEGER,

    -- Association 1 à 1 vers Consultation
                                     idConsultation BIGINT UNIQUE NOT NULL, -- UNIQUE pour garantir 1 Intervention par Consultation
                                     CONSTRAINT fk_im_consultation FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation)
    -- La relation 1 à * vers Acte est gérée par une table d'association
);

-- 21. Acte
CREATE TABLE Acte (
                      idActe BIGINT PRIMARY KEY,
                      libelle VARCHAR(255) NOT NULL,
                      categorie VARCHAR(100), -- Catégorie
                      prixDeBase DOUBLE PRECISION NOT NULL
    -- La relation * à * avec InterventionMedecin est gérée par une table d'association
);

-- 22. Ordonnance
CREATE TABLE Ordonnance (
                            idOrd BIGINT PRIMARY KEY,
                            date DATE NOT NULL,

    -- Association 1 à 1 vers DossierMedicale
                            idDM BIGINT UNIQUE NOT NULL, -- UNIQUE pour garantir 1 Ordonnance par Dossier Médical (ou à revoir la cardinalité si plusieurs ordonnances sont possibles)
                            CONSTRAINT fk_ordonnance_dm FOREIGN KEY (idDM) REFERENCES DossierMedicale(idDM)
);

-- 23. Certificat
CREATE TABLE Certificat (
                            idCertif BIGINT PRIMARY KEY,
                            dateDebut DATE NOT NULL,
                            dateFin DATE NOT NULL,
                            duree INTEGER NOT NULL,
                            noteMedecin TEXT,

    -- Association 1 à 1 vers DossierMedicale
                            idDM BIGINT UNIQUE NOT NULL, -- UNIQUE pour garantir 1 Certificat par Dossier Médical (ou à revoir la cardinalité)
                            CONSTRAINT fk_certificat_dm FOREIGN KEY (idDM) REFERENCES DossierMedicale(idDM)
);

-- 24. Medicament
CREATE TABLE Medicament (
                            id BIGINT PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            laboratoire VARCHAR(100),
                            type VARCHAR(100), -- Type
                            forme VARCHAR(100), -- Forme
                            remboursable BOOLEAN NOT NULL DEFAULT TRUE,
                            prixUnitaire DOUBLE PRECISION,
                            description TEXT
    -- La relation * à * avec Prescription sera gérée par une table d'association
);

-- 25. Prescription
CREATE TABLE Prescription (
                              idPr BIGINT PRIMARY KEY,
                              quantite INT NOT NULL,
                              frequence VARCHAR(100),
                              dureeEnJours INT,

    -- Association 1 à * vers Ordonnance
                              idOrd BIGINT NOT NULL,
                              CONSTRAINT fk_prescription_ordonnance FOREIGN KEY (idOrd) REFERENCES Ordonnance(idOrd),

    -- Association 1 à * vers Medicament (la clé primaire du médicament prescrit)
                              idMedicament BIGINT NOT NULL,
                              CONSTRAINT fk_prescription_medicament FOREIGN KEY (idMedicament) REFERENCES Medicament(id)
);


-- ##########################################################################
-- #                                 PARTIE 4 : TABLES D'ASSOCIATION (M:N)  #
-- ##########################################################################

-- 26. Table d'association : Utilisateur - Role (* à *)
CREATE TABLE Utilisateur_Role (
                                  idUtilisateur BIGINT NOT NULL,
                                  idRole BIGINT NOT NULL,
                                  PRIMARY KEY (idUtilisateur, idRole),
                                  CONSTRAINT fk_ur_utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtiliser),
                                  CONSTRAINT fk_ur_role FOREIGN KEY (idRole) REFERENCES Role(idRole)
);

-- 27. Table d'association : Utilisateur - Notification (* à *)
CREATE TABLE Utilisateur_Notification (
                                          idUtilisateur BIGINT NOT NULL,
                                          idNotification BIGINT NOT NULL,
                                          PRIMARY KEY (idUtilisateur, idNotification),
                                          CONSTRAINT fk_un_utilisateur FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(idUtiliser),
                                          CONSTRAINT fk_un_notification FOREIGN KEY (idNotification) REFERENCES Notification(id)
);

-- 28. Table d'association : Consultation - Facture (* à *)
CREATE TABLE Consultation_Facture (
                                      idConsultation BIGINT NOT NULL,
                                      idFacture BIGINT NOT NULL,
                                      PRIMARY KEY (idConsultation, idFacture),
                                      CONSTRAINT fk_cf_consultation FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation),
                                      CONSTRAINT fk_cf_facture FOREIGN KEY (idFacture) REFERENCES Facture(idFacture)
);

-- 29. Table d'association : InterventionMedecin - Acte (1 à *) -> * à *
CREATE TABLE InterventionMedecin_Acte (
                                          idIntervention BIGINT NOT NULL,
                                          idActe BIGINT NOT NULL,
                                          PRIMARY KEY (idIntervention, idActe),
                                          CONSTRAINT fk_ia_intervention FOREIGN KEY (idIntervention) REFERENCES InterventionMedecin(idIM),
                                          CONSTRAINT fk_ia_acte FOREIGN KEY (idActe) REFERENCES Acte(idActe)
);

-- 30. Table d'association : Consultation - Medecin (* à *)
CREATE TABLE Consultation_Medecin (
                                      idConsultation BIGINT NOT NULL,
                                      idMedecin BIGINT NOT NULL,
                                      PRIMARY KEY (idConsultation, idMedecin),
                                      CONSTRAINT fk_cm_consultation FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation),
                                      CONSTRAINT fk_cm_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(idUser)
);

-- 31. Table d'association : Patient - Antecedents (* à *)
CREATE TABLE Patient_Antecedents (
                                     idPatient BIGINT NOT NULL,
                                     idAntecedent BIGINT NOT NULL,
                                     PRIMARY KEY (idPatient, idAntecedent),
                                     CONSTRAINT fk_pa_patient FOREIGN KEY (idPatient) REFERENCES Patient(idPatient),
                                     CONSTRAINT fk_pa_antecedent FOREIGN KEY (idAntecedent) REFERENCES Antecedents(idAntecedent)
);