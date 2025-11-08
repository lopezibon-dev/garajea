
-- ----------------------------------------
-- 1. Datu Basea Sortu
-- ----------------------------------------
CREATE DATABASE IF NOT EXISTS garajea CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE garajea;

-- ----------------------------------------
-- 2. Taula Nagusiak Sortu
-- ----------------------------------------

-- BEZEROA (Bezeroak/Web Erabiltzaileak)
CREATE TABLE BEZEROA (
    bezeroa_id INT AUTO_INCREMENT PRIMARY KEY, -- PK estandarra: {entidad}_id
    izena VARCHAR(100) NOT NULL,
    abizenak VARCHAR(100) NOT NULL,
    emaila VARCHAR(150) UNIQUE NOT NULL, 
    telefonoa VARCHAR(20),
    pasahitza VARCHAR(255) NOT NULL
);

-- LANGILEA (Garajeko Langileak)
CREATE TABLE LANGILEA (
    langilea_id INT AUTO_INCREMENT PRIMARY KEY, -- PK estandarra: {entidad}_id
    izena VARCHAR(100) NOT NULL,
    abizena VARCHAR(100) NOT NULL,
    lanpostua VARCHAR(100) 
);

-- KABINA (Lan Kabinak)
CREATE TABLE KABINA (
    kabina_id INT AUTO_INCREMENT PRIMARY KEY, -- PK izena zuzendua: kabina_id
    izena VARCHAR(50) UNIQUE NOT NULL
);

-- IBILGAILUA (Bezeroen Ibilgailuak)
CREATE TABLE IBILGAILUA (
    ibilgailua_id INT AUTO_INCREMENT PRIMARY KEY,
    matrikula VARCHAR(15) UNIQUE NOT NULL, 
    marka VARCHAR(100),
    modeloa VARCHAR(100),
    urtea INT,
    bezeroa_id INT NOT NULL, -- FK bat dator PK-arekin: bezeroa_id
    FOREIGN KEY (bezeroa_id) REFERENCES BEZEROA(bezeroa_id) ON DELETE CASCADE 
);

-- ----------------------------------------
-- 3. Baliabideak/Inbentario Taulak
-- ----------------------------------------

-- ERREMINTA (Erreminta eskuragarriak)
CREATE TABLE ERREMINTA (
    erreminta_id INT AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(100) UNIQUE NOT NULL
);

-- MAKINA (Diagnostiko Makinak, etab.)
CREATE TABLE MAKINA (
    makina_id INT AUTO_INCREMENT PRIMARY KEY, -- PK izena zuzendua: makina_id
    izena VARCHAR(100) NOT NULL,
    mota VARCHAR(100),
    kabina_id INT NOT NULL, -- FK bat dator PK-arekin: kabina_id
    FOREIGN KEY (kabina_id) REFERENCES KABINA(kabina_id) ON DELETE CASCADE
);

-- GORAGAILUA (Goragailu Hidraulikoak)
CREATE TABLE GORAGAILUA (
    goragailua_id INT AUTO_INCREMENT PRIMARY KEY, -- PK izena zuzendua: goragailua_id
    izena VARCHAR(100) NOT NULL,
    kabina_id INT NOT NULL UNIQUE, -- FK bat dator PK-arekin: kabina_id
    FOREIGN KEY (kabina_id) REFERENCES KABINA(kabina_id) ON DELETE CASCADE
);

-- MATERIALA (Salmenta/Erabiltzeko Materialak)
CREATE TABLE MATERIALA (
    materiala_id INT AUTO_INCREMENT PRIMARY KEY, -- PK izena zuzendua: materiala_id
    izena VARCHAR(100) UNIQUE NOT NULL,
    mota VARCHAR(100),
    prezioa DECIMAL(10, 2)
);

-- ----------------------------------------
-- 4. Kudeaketa eta Transakzio Taulak
-- ----------------------------------------

-- FAKTURA (Sortutako Fakturak)
CREATE TABLE FAKTURA (
    faktura_id INT AUTO_INCREMENT PRIMARY KEY, -- PK izena zuzendua: faktura_id
    erreserba_id INT UNIQUE, 
    zenbatekoa DECIMAL(10, 2) NOT NULL,
    data DATE NOT NULL
);

-- ERRESERBA (Kabinen Erreserbak)
CREATE TABLE ERRESERBA (
    erreserba_id INT AUTO_INCREMENT PRIMARY KEY,
    bezeroa_id INT NOT NULL, 
    ibilgailua_id INT NOT NULL,
    kabina_id INT NOT NULL, -- FK bat dator PK-arekin
    langilea_id INT, 
    hasiera_data_ordua DATETIME NOT NULL,
    amaiera_data_ordua DATETIME NOT NULL,
    oharrak TEXT,
    egoera VARCHAR(50) NOT NULL, 
    faktura_id INT, -- FK bat dator PK-arekin
    
    FOREIGN KEY (bezeroa_id) REFERENCES BEZEROA(bezeroa_id),
    FOREIGN KEY (ibilgailua_id) REFERENCES IBILGAILUA(ibilgailua_id),
    FOREIGN KEY (kabina_id) REFERENCES KABINA(kabina_id),
    FOREIGN KEY (langilea_id) REFERENCES LANGILEA(langilea_id),
    FOREIGN KEY (faktura_id) REFERENCES FAKTURA(faktura_id)
);

-- ERRESERBA_MATERIALA (Erreserba eta Materialaren arteko N:M harremana)
CREATE TABLE ERRESERBA_MATERIALA (
    erreserba_id INT NOT NULL,
    materiala_id INT NOT NULL,
    kopurua INT NOT NULL DEFAULT 1,
    PRIMARY KEY (erreserba_id, materiala_id),
    FOREIGN KEY (erreserba_id) REFERENCES ERRESERBA(erreserba_id) ON DELETE CASCADE,
    FOREIGN KEY (materiala_id) REFERENCES MATERIALA(materiala_id) ON DELETE RESTRICT
);