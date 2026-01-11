CREATE TABLE IF NOT EXISTS Utilisateurs (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                            prenom     VARCHAR(120)  NOT NULL,
                                            nom        VARCHAR(120)  NOT NULL,
                                            email      VARCHAR(150)  NOT NULL,
                                            adresse    VARCHAR(255),
                                            cin        VARCHAR(30),
                                            tel        VARCHAR(30),
                                            avatar     VARCHAR(255),
                                            sexe ENUM('Homme','Femme') NOT NULL,

                                            login       VARCHAR(80)  NOT NULL,
                                            motDePasse  VARCHAR(255) NOT NULL,

                                            lastLoginDate DATE,
                                            dateNaissance DATE,

    -- Champs communs (BaseEntity)
                                            dateCreation             DATE        NOT NULL DEFAULT (CURRENT_DATE),
                                            dateDerniereModification DATETIME    DEFAULT CURRENT_TIMESTAMP,
                                            creePar                  VARCHAR(80),
                                            modifiePar               VARCHAR(80),

                                            UNIQUE KEY uk_user_email (email),
                                            UNIQUE KEY uk_user_login (login),
                                            UNIQUE KEY uk_user_cin   (cin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ==========================================================
-- Table Roles
-- ENUM : RoleType { ADMIN, MEDECIN, SECRETAIRE }
-- ==========================================================
CREATE TABLE IF NOT EXISTS Roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                     libelle VARCHAR(120) NOT NULL,

                                     type ENUM('ADMIN','MEDECIN','SECRETAIRE') NOT NULL,

    -- Champs BaseEntity
                                     dateCreation             DATE     NOT NULL DEFAULT (CURRENT_DATE),
                                     dateDerniereModification DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     creePar                  VARCHAR(80),
                                     modifiePar               VARCHAR(80),

                                     UNIQUE KEY uk_role_libelle (libelle),
                                     KEY idx_role_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
``
CREATE TABLE IF NOT EXISTS Role_Privileges (
                                               role_id BIGINT NOT NULL,
                                               privilege VARCHAR(150) NOT NULL,

                                               PRIMARY KEY (role_id, privilege),

                                               CONSTRAINT fk_privilege_role
                                                   FOREIGN KEY (role_id) REFERENCES Roles(id)
                                                       ON DELETE CASCADE
                                                       ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




-- ==========================================================
-- Table User_Roles (association Many-to-Many)
-- ==========================================================
CREATE TABLE IF NOT EXISTS Utilisateur_Roles (
                                                 utilisateur_id BIGINT NOT NULL,
                                                 role_id BIGINT NOT NULL,

                                                 PRIMARY KEY (utilisateur_id, role_id),

                                                 CONSTRAINT fk_user_role_user
                                                     FOREIGN KEY (utilisateur_id) REFERENCES Utilisateurs(id)
                                                         ON DELETE CASCADE
                                                         ON UPDATE CASCADE,

                                                 CONSTRAINT fk_user_role_role
                                                     FOREIGN KEY (role_id) REFERENCES Roles(id)
                                                         ON DELETE CASCADE
                                                         ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- ==========================================================
-- Table Staffs (Staff extends Utilisateur)
-- ==========================================================
CREATE TABLE IF NOT EXISTS Staffs (
                                      id BIGINT PRIMARY KEY,

                                      salaire        DECIMAL(12,2),
                                      prime          DECIMAL(12,2),
                                      dateRecrutement DATE,
                                      soldeConge     INT,

                                      CONSTRAINT fk_staff_user
                                          FOREIGN KEY (id) REFERENCES Utilisateurs(id)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




-- ==========================================================
-- Table Admins (Admin extends Staff)
-- ==========================================================
CREATE TABLE IF NOT EXISTS Admins (
                                      id BIGINT PRIMARY KEY,

                                      CONSTRAINT fk_admin_staff
                                          FOREIGN KEY (id) REFERENCES Staffs(id)
                                              ON DELETE CASCADE
                                              ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




-- ==========================================================
-- Table Medecins (Medecin extends Staff)
-- ==========================================================
CREATE TABLE IF NOT EXISTS Medecins (
                                        id BIGINT PRIMARY KEY,

                                        specialite VARCHAR(150),
                                        agenda_id BIGINT NULL,

                                        CONSTRAINT fk_medecin_staff
                                            FOREIGN KEY (id) REFERENCES Staffs(id)
                                                ON DELETE CASCADE
                                                ON UPDATE CASCADE

    -- FK agenda_id à activer quand ton module Agenda sera créé
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




-- ==========================================================
-- Table Secretaires (Secretaire extends Staff)
-- ==========================================================
CREATE TABLE IF NOT EXISTS Secretaires (
                                           id BIGINT PRIMARY KEY,

                                           numCNSS VARCHAR(50),
                                           commission DECIMAL(12,2),

                                           CONSTRAINT fk_secretaire_staff
                                               FOREIGN KEY (id) REFERENCES Staffs(id)
                                                   ON DELETE CASCADE
                                                   ON UPDATE CASCADE,

                                           UNIQUE KEY uk_secretaire_numcnss (numCNSS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



