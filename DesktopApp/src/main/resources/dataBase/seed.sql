INSERT INTO Utilisateurs
(id, nom, prenom, email, adresse, cin, tel, avatar, sexe, login, motDePasse, dateNaissance, dateCreation)
VALUES
    (1, 'El Midaoui', 'Omar','admin.omar@dentaltech.ma',
     'Rabat Agdal', 'X1234567', '0612345678', '/static/avatars/default.png','Homme',
     'admin.omar', '$2a$10$JzjdZK8fGupihk4/yfJLWeK1auTzwiFQg2E0g94MOxyPhg0e/rilC', '1994-04-10', '2026-01-01');

INSERT INTO Utilisateurs
(id, nom, prenom, email, adresse, cin, tel, avatar,  sexe, login, motDePasse, dateNaissance, dateCreation)
VALUES
    (2, 'Alami','Wafae','med.wafae@dentaltech.ma',
     'Rabat Hassan', 'X7654321', '0678901234', '/static/avatars/default.png','Femme',
     'dr.wafae', '$2a$10$XYTetaH42NaC52Uknz.0leKegAdoJXb8lXDV5aEEhUz2XaxeHmBvC', '1994-04-10', '2026-01-01');

INSERT INTO Utilisateurs
(id, nom, prenom, email, adresse, cin, tel, avatar,  sexe, login, motDePasse, dateNaissance, dateCreation)
VALUES
    (3, 'Ben Ali', 'Fatima Zahra', 'fz.secretariat@dentaltech.ma',
     'Salé Tabriquet', 'C998877', '0654321098', '/static/avatars/default.png','Femme',
     'fz.sec', '$2a$10$nPuXpxsbMyVBJVnheD/diOM0pw/8mm3S6moHBRMOM7p9V.4dCNG0O', '1998-02-20', '2026-01-01');


INSERT INTO Roles (id, libelle, type, dateCreation, creePar)
VALUES
    (1, 'ROLE_ADMIN',      'ADMIN',      '2026-01-01', 'SYSTEM'),
    (2, 'ROLE_MEDECIN',    'MEDECIN',    '2026-01-01', 'SYSTEM'),
    (3, 'ROLE_SECRETAIRE', 'SECRETAIRE', '2026-01-01', 'SYSTEM');


INSERT INTO Utilisateur_Roles VALUES
                                  (1, 1),
                                  (2, 2),
                                  (3, 3);


INSERT INTO Role_Privileges(role_id, privilege) VALUES
                                                    (1, 'GESTION_UTILISATEURS'),
                                                    (1, 'GESTION_CABINET'),

                                                    (2, 'GESTION_PATIENTS'),
                                                    (2, 'GERER_DOSSIERS'),
                                                    (2, 'GERER_RDV'),

                                                    (3, 'GESTION_PATIENTS'),
                                                    (3, 'GESTION_CAISSE'),
                                                    (3, 'GERER_AGENDA_MEDECIN'),
                                                    (3, 'GERER_RDV');




INSERT INTO Staffs VALUES
                       (1, 20000.00, 3000.00, '2020-01-10', 15),
                       (2, 25000.00, 2500.00, '2021-02-15', 12),
                       (3, 8000.00, 300.00,  '2022-10-01', 20);

INSERT INTO Admins VALUES (1);

INSERT INTO Medecins (id, specialite) VALUES
    (2, 'Chirurgie dentaire – Implantologie');

INSERT INTO Secretaires (id, numCNSS, commission) VALUES
    (3, 'CNSS009988', 5.0);

