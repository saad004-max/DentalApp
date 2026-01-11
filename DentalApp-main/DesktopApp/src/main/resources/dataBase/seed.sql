-- Insertion des Rôles
INSERT INTO Role (idRole, libelle, privileges) VALUES
                                                   (1, 'Administrateur', ARRAY['admin', 'lecture', 'ecriture']::Privilege[]),
                                                   (2, 'Médecin', ARRAY['lecture', 'ecriture']::Privilege[]),
                                                   (3, 'Secrétaire', ARRAY['lecture', 'ecriture']::Privilege[]),
                                                   (4, 'Patient', ARRAY['lecture']::Privilege[]);

-- Insertion Cabinet Médical (Utilisateur)
INSERT INTO Utilisateur (idUtiliser, nom, email, adresse, tel, sexe, login, motDePass, dateCreation, dateNaissance) VALUES
    (1, 'Cabinet Dentaire Al Amal', 'contact@cabinet-alamal.ma', '123 Avenue Mohammed V, Rabat', '+212-537-123456', 'M', 'cabinet_admin', '$2a$10$hashedpassword1', CURRENT_TIMESTAMP, '1980-01-01');

INSERT INTO CabinetMedicale (idUser, nom, email, logo, adresse, cin, tel1, tel2, siteWeb, instagram, facebook, description) VALUES
    (1, 'Cabinet Dentaire Al Amal', 'contact@cabinet-alamal.ma', 'logo_alamal.png', '123 Avenue Mohammed V, Rabat', 'AB123456', '+212-537-123456', '+212-661-234567', 'www.cabinet-alamal.ma', '@cabinet_alamal', 'CabinetAlAmal', 'Cabinet dentaire moderne offrant des soins de qualité');

-- Insertion Staff - Admin
INSERT INTO Utilisateur (idUtiliser, nom, email, adresse, tel, sexe, login, motDePass, dateCreation, dateNaissance) VALUES
    (2, 'Hassan Benjelloun', 'h.benjelloun@cabinet-alamal.ma', '45 Rue des Fleurs, Rabat', '+212-661-111111', 'M', 'h.benjelloun', '$2a$10$hashedpassword2', CURRENT_TIMESTAMP, '1985-03-15');

INSERT INTO Staff (idUser, salaire, prime, dateRecrutement, soldeConge) VALUES
    (2, 15000.00, 2000.00, '2020-01-10', 25);

INSERT INTO Admin (idUser) VALUES (2);

-- Insertion Staff - Médecins
INSERT INTO Utilisateur (idUtiliser, nom, email, adresse, tel, sexe, login, motDePass, dateCreation, dateNaissance) VALUES
                                                                                                                        (3, 'Dr. Amina Alaoui', 'dr.alaoui@cabinet-alamal.ma', '78 Boulevard Zerktouni, Casablanca', '+212-662-222222', 'F', 'dr.alaoui', '$2a$10$hashedpassword3', CURRENT_TIMESTAMP, '1982-07-22'),
                                                                                                                        (4, 'Dr. Youssef Tahiri', 'dr.tahiri@cabinet-alamal.ma', '12 Rue Patrice Lumumba, Rabat', '+212-663-333333', 'M', 'dr.tahiri', '$2a$10$hashedpassword4', CURRENT_TIMESTAMP, '1978-11-05');

INSERT INTO Staff (idUser, salaire, prime, dateRecrutement, soldeConge) VALUES
                                                                            (3, 25000.00, 5000.00, '2019-06-01', 20),
                                                                            (4, 28000.00, 6000.00, '2018-03-15', 18);

INSERT INTO Medecin (idUser, specialite, agendaMensuel) VALUES
                                                            (3, 'Orthodontie', 'agenda_dr_alaoui'),
                                                            (4, 'Chirurgie Dentaire', 'agenda_dr_tahiri');

-- Insertion Staff - Secrétaires
INSERT INTO Utilisateur (idUtiliser, nom, email, adresse, tel, sexe, login, motDePass, dateCreation, dateNaissance) VALUES
    (5, 'Fatima Zahra El Idrissi', 'f.elidrissi@cabinet-alamal.ma', '34 Rue Allal Ben Abdellah, Salé', '+212-664-444444', 'F', 'f.elidrissi', '$2a$10$hashedpassword5', CURRENT_TIMESTAMP, '1990-05-12');

INSERT INTO Staff (idUser, salaire, prime, dateRecrutement, soldeConge) VALUES
    (5, 8000.00, 1000.00, '2021-02-01', 22);

INSERT INTO Secretaire (idUser, numCNSS, commission) VALUES
    (5, 'CNSS123456789', 500.00);


INSERT INTO Patient (idPatient, nom, dateNaissance, sexe, adresse, telephone, assurance) VALUES
                                                                                             (1, 'Mohammed Bennani', '1990-04-15', 'M', '56 Avenue Hassan II, Rabat', '+212-665-555555', 'CNSS'),
                                                                                             (2, 'Salma Chakir', '1985-08-23', 'F', '89 Rue de la Liberté, Casablanca', '+212-666-666666', 'CNOPS'),
                                                                                             (3, 'Ahmed Tazi', '1995-12-10', 'M', '23 Boulevard Mohammed VI, Marrakech', '+212-667-777777', 'Aucune'),
                                                                                             (4, 'Nadia Fassi', '1988-03-30', 'F', '67 Rue Oued Fes, Fès', '+212-668-888888', 'Saham'),
                                                                                             (5, 'Karim Alami', '1992-09-18', 'M', '45 Avenue des FAR, Rabat', '+212-669-999999', 'CNSS');


INSERT INTO Antecedents (idAntecedent, nom, categorie, niveauDeRisque) VALUES
                                                                           (1, 'Diabète Type 2', 'medical', 'Élevé'),
                                                                           (2, 'Hypertension Artérielle', 'medical', 'Moyen'),
                                                                           (3, 'Allergie à la Pénicilline', 'allergie', 'Élevé'),
                                                                           (4, 'Appendicectomie', 'chirurgical', 'Faible'),
                                                                           (5, 'Asthme', 'medical', 'Moyen'),
                                                                           (6, 'Maladie Cardiaque Familiale', 'familial', 'Élevé');

-- Association Patients - Antécédents
INSERT INTO Patient_Antecedents (idPatient, idAntecedent) VALUES
                                                              (1, 1), -- Mohammed a du diabète
                                                              (1, 4), -- Mohammed a eu une appendicectomie
                                                              (2, 3), -- Salma est allergique à la pénicilline
                                                              (4, 2), -- Nadia a de l'hypertension
                                                              (4, 5), -- Nadia a de l'asthme
                                                              (5, 6); -- Karim a des antécédents familiaux cardiaques


INSERT INTO DossierMedicale (idDM, dateCreation, idPatient) VALUES
                                                                (1, CURRENT_TIMESTAMP, 1),
                                                                (2, CURRENT_TIMESTAMP, 2),
                                                                (3, CURRENT_TIMESTAMP, 3),
                                                                (4, CURRENT_TIMESTAMP, 4),
                                                                (5, CURRENT_TIMESTAMP, 5);

INSERT INTO SituationFinanciere (idSF, totalPrestations, totalPaye, reste, enPromo, idPatient) VALUES
                                                                                                   (1, 5000.00, 3000.00, 2000.00, FALSE, 1),
                                                                                                   (2, 8000.00, 8000.00, 0.00, TRUE, 2),
                                                                                                   (3, 3500.00, 1500.00, 2000.00, FALSE, 3),
                                                                                                   (4, 6000.00, 4500.00, 1500.00, FALSE, 4),
                                                                                                   (5, 2000.00, 2000.00, 0.00, FALSE, 5);

-- ============================================================================
-- PARTIE 6 : RENDEZ-VOUS
-- ============================================================================

INSERT INTO RDV (idRDV, date, heure, motif, statut, noteMedecin, idMedecin, idPatient) VALUES
                                                                                           (1, '2024-12-10', '09:00:00', 'Contrôle de routine', 'confirme', 'RAS', 3, 1),
                                                                                           (2, '2024-12-10', '10:30:00', 'Urgence - Douleur dentaire', 'confirme', 'Douleur molaire supérieure', 4, 2),
                                                                                           (3, '2024-12-11', '14:00:00', 'Consultation orthodontie', 'confirme', NULL, 3, 3),
                                                                                           (4, '2024-12-12', '11:00:00', 'Détartrage', 'en_attente', NULL, 3, 4),
                                                                                           (5, '2024-12-08', '15:00:00', 'Extraction dentaire', 'annule', 'Patient a annulé', 4, 5);

INSERT INTO Consultation (idConsultation, date, statut, observationMedecin, idDM) VALUES
                                                                                      (1, '2024-12-05', 'terminee', 'Patient en bonne santé, détartrage effectué', 1),
                                                                                      (2, '2024-12-06', 'terminee', 'Extraction molaire nécessaire, traitement antibiotique prescrit', 2),
                                                                                      (3, '2024-12-07', 'terminee', 'Pose d appareil dentaire programmée', 3),
                                                                                      (4, '2024-12-08', 'en_cours', 'Traitement de carie en cours', 4);

INSERT INTO Consultation_Medecin (idConsultation, idMedecin) VALUES
                                                                 (1, 3),
                                                                 (2, 4),
                                                                 (3, 3),
                                                                 (4, 4);

INSERT INTO Acte (idActe, libelle, categorie, prixDeBase) VALUES
                                                              (1, 'Détartrage', 'Hygiène', 300.00),
                                                              (2, 'Extraction Simple', 'Chirurgie', 500.00),
                                                              (3, 'Extraction Complexe', 'Chirurgie', 800.00),
                                                              (4, 'Pose Appareil Dentaire', 'Orthodontie', 15000.00),
                                                              (5, 'Traitement Carie', 'Soins Conservateurs', 400.00),
                                                              (6, 'Dévitalisation', 'Endodontie', 1200.00),
                                                              (7, 'Couronne Céramique', 'Prothèse', 2500.00);


INSERT INTO InterventionMedecin (idIM, prixDePatient, numDent, idConsultation) VALUES
                                                                                   (1, 300.00, NULL, 1),
                                                                                   (2, 500.00, 26, 2),
                                                                                   (3, 15000.00, NULL, 3),
                                                                                   (4, 400.00, 16, 4);
INSERT INTO InterventionMedecin_Acte (idIntervention, idActe) VALUES
                                                                  (1, 1), -- Détartrage
                                                                  (2, 2), -- Extraction simple
                                                                  (3, 4), -- Pose appareil
                                                                  (4, 5); -- Traitement carie

INSERT INTO Facture (idFacture, totalFacture, totalPaye, reste, statut, dateFacture, idSF) VALUES
                                                                                               (1, 300.00, 300.00, 0.00, 'payee', '2024-12-05 10:30:00', 1),
                                                                                               (2, 500.00, 200.00, 300.00, 'partiellement_payee', '2024-12-06 11:00:00', 2),
                                                                                               (3, 15000.00, 5000.00, 10000.00, 'partiellement_payee', '2024-12-07 14:30:00', 3),
                                                                                               (4, 400.00, 0.00, 400.00, 'impayee', '2024-12-08 16:00:00', 4);

INSERT INTO Consultation_Facture (idConsultation, idFacture) VALUES
                                                                 (1, 1),
                                                                 (2, 2),
                                                                 (3, 3),
                                                                 (4, 4);


INSERT INTO Ordonnance (idOrd, date, idDM) VALUES
                                               (1, '2024-12-06', 2),
                                               (2, '2024-12-08', 4);

INSERT INTO Medicament (id, nom, laboratoire, type, forme, remboursable, prixUnitaire, description) VALUES
                                                                                                        (1, 'Amoxicilline 500mg', 'Sanofi', 'Antibiotique', 'Comprimé', TRUE, 35.00, 'Antibiotique à large spectre'),
                                                                                                        (2, 'Ibuprofène 400mg', 'Pfizer', 'Anti-inflammatoire', 'Comprimé', TRUE, 25.00, 'Anti-inflammatoire non stéroïdien'),
                                                                                                        (3, 'Paracétamol 1g', 'GSK', 'Antalgique', 'Comprimé', TRUE, 15.00, 'Antalgique et antipyrétique'),
                                                                                                        (4, 'Bain de bouche Chlorhexidine', 'Pierre Fabre', 'Antiseptique', 'Solution', FALSE, 45.00, 'Antiseptique buccal');

INSERT INTO Prescription (idPr, quantite, frequence, dureeEnJours, idOrd, idMedicament) VALUES
                                                                                            (1, 21, '3 fois par jour', 7, 1, 1), -- Amoxicilline pour 7 jours
                                                                                            (2, 12, '2 fois par jour après les repas', 6, 1, 2), -- Ibuprofène pour 6 jours
                                                                                            (3, 10, '3 fois par jour si douleur', 10, 2, 3), -- Paracétamol
                                                                                            (4, 1, '2 bains de bouche par jour', 14, 2, 4); -- Bain de bouche


INSERT INTO Certificat (idCertif, dateDebut, dateFin, duree, noteMedecin, idDM) VALUES
    (1, '2024-12-06', '2024-12-08', 3, 'Repos suite à extraction dentaire complexe', 2);


INSERT INTO AgendaMensuel (id, mois, joursNonDisponible, idMedecin) VALUES
                                                                        (1, 'DEC', ARRAY['2024-12-25', '2024-12-26'], 3), -- Dr. Alaoui - Décembre
                                                                        (2, 'DEC', ARRAY['2024-12-25', '2024-12-26', '2024-12-31'], 4), -- Dr. Tahiri - Décembre
                                                                        (3, 'JAN', ARRAY['2025-01-01'], 3), -- Dr. Alaoui - Janvier
                                                                        (4, 'JAN', ARRAY['2025-01-01'], 4); -- Dr. Tahiri - Janvier


INSERT INTO Statistiques (id, nom, categorie, chiffre, dateCalcul, idCabinet) VALUES
                                                                                  (1, 'Revenus Mensuels', 'financiere', 45000.00, CURRENT_TIMESTAMP, 1),
                                                                                  (2, 'Nombre de Patients', 'patient', 5, CURRENT_TIMESTAMP, 1),
                                                                                  (3, 'Rendez-vous Confirmés', 'rendez_vous', 3, CURRENT_TIMESTAMP, 1),
                                                                                  (4, 'Taux de Recouvrement', 'financiere', 65.5, CURRENT_TIMESTAMP, 1);

INSERT INTO Charges (id, titre, description, montant, date, idCabinet) VALUES
                                                                           (1, 'Loyer Bureau', 'Loyer mensuel du cabinet', 8000.00, '2024-12-01 00:00:00', 1),
                                                                           (2, 'Salaires Staff', 'Salaires du mois de décembre', 76000.00, '2024-12-01 00:00:00', 1),
                                                                           (3, 'Matériel Médical', 'Achat de matériel dentaire', 15000.00, '2024-12-05 00:00:00', 1),
                                                                           (4, 'Électricité', 'Facture électricité', 2500.00, '2024-12-03 00:00:00', 1);

INSERT INTO Revenus (id, titre, description, montant, date, idCabinet) VALUES
                                                                           (1, 'Consultations Décembre', 'Revenus des consultations', 16200.00, '2024-12-01 00:00:00', 1),
                                                                           (2, 'Traitements Orthodontie', 'Revenus orthodontie', 15000.00, '2024-12-07 00:00:00', 1),
                                                                           (3, 'Soins Conservateurs', 'Revenus soins divers', 8500.00, '2024-12-05 00:00:00', 1);


INSERT INTO Notification (id, message, date, time, type, priorite) VALUES
                                                                       (1, 'Rendez-vous demain avec M. Bennani à 09h00', '2024-12-09', '08:00:00', 'rappel', 'normale'),
                                                                       (2, 'Paiement en retard - Patient Ahmed Tazi', '2024-12-08', '10:00:00', 'alerte', 'haute'),
                                                                       (3, 'Nouvelle commande de matériel médical livrée', '2024-12-07', '14:30:00', 'information', 'basse'),
                                                                       (4, 'Urgence - Mme Chakir demande RDV urgent', '2024-12-06', '16:45:00', 'alerte', 'urgente');

INSERT INTO Utilisateur_Notification (idUtilisateur, idNotification) VALUES
                                                                         (3, 1), -- Dr. Alaoui reçoit notification RDV
                                                                         (5, 2), -- Secrétaire reçoit alerte paiement
                                                                         (2, 3), -- Admin reçoit info livraison
                                                                         (4, 4), -- Dr. Tahiri reçoit alerte urgence
                                                                         (5, 4); -- Secrétaire reçoit aussi alerte urgence

INSERT INTO Utilisateur_Role (idUtilisateur, idRole) VALUES
                                                         (2, 1), -- Hassan = Admin
                                                         (3, 2), -- Dr. Alaoui = Médecin
                                                         (4, 2), -- Dr. Tahiri = Médecin
                                                         (5, 3); -- Fatima = Secrétaire
