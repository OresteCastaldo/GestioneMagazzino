-- Script SQL per l'inizializzazione del database 'Gestione Magazzino'
-- Include un set esteso di movimenti per testare lo storico.

-- 1. UTENTI
INSERT INTO utente (id_utente, nome, cognome, email, password, ruolo) VALUES (1, 'Mario', 'Rossi', 'responsabile@magazzino.it', 'admin123', 'RESPONSABILE');
INSERT INTO utente (id_utente, nome, cognome, email, password, ruolo) VALUES (2, 'Luigi', 'Verdi', 'operatore1@magazzino.it', 'operatore123', 'OPERATORE');
INSERT INTO utente (id_utente, nome, cognome, email, password, ruolo) VALUES (3, 'Giulia', 'Bianchi', 'operatore2@magazzino.it', 'operatore123', 'OPERATORE');

-- 2. TABELLE DERIVATE
INSERT INTO responsabile (id_responsabile) VALUES (1);
INSERT INTO operatore (id_operatore) VALUES (2);
INSERT INTO operatore (id_operatore) VALUES (3);

-- 3. PRODOTTI
-- Le quantità disponibili attuali riflettono il saldo finale dei movimenti sottostanti.
INSERT INTO prodotto (id_prodotto, nome, descrizione, categoria, scaffale, quantita_disponibile, soglia_minima, sotto_scorta) VALUES ('PROD01', 'Mela', 'Mele fresche di stagione', 'Frutta', 'SCAF01', 150, 50, 0);
INSERT INTO prodotto (id_prodotto, nome, descrizione, categoria, scaffale, quantita_disponibile, soglia_minima, sotto_scorta) VALUES ('PROD02', 'Banana', 'Banane biologiche', 'Frutta', 'SCAF05', 20, 30, 1);
INSERT INTO prodotto (id_prodotto, nome, descrizione, categoria, scaffale, quantita_disponibile, soglia_minima, sotto_scorta) VALUES ('PROD03', 'Zucchina', 'Zucchine verdi scure', 'Verdura', 'SCAF02', 100, 40, 0);
INSERT INTO prodotto (id_prodotto, nome, descrizione, categoria, scaffale, quantita_disponibile, soglia_minima, sotto_scorta) VALUES ('PROD04', 'Acqua Naturale', 'Bottiglia 1.5L', 'Bevande', 'SCAF03', 500, 100, 0);
INSERT INTO prodotto (id_prodotto, nome, descrizione, categoria, scaffale, quantita_disponibile, soglia_minima, sotto_scorta) VALUES ('PROD05', 'Sofficini', 'Surgelati', 'Surgelati', 'SCAF04', 10, 50, 1);

-- 4. MOVIMENTI
-- Test intensivo sullo storico di PROD01 (Saldo finale: 150)
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (100, '2026-06-01 08:00:00', 'CARICO', 'PROD01', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (20, '2026-06-05 10:30:00', 'SCARICO', 'PROD01', 3);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (150, '2026-06-20 09:15:00', 'CARICO', 'PROD01', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (80, '2026-07-02 11:45:00', 'SCARICO', 'PROD01', 3);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (50, '2026-07-10 14:20:00', 'SCARICO', 'PROD01', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (50, '2026-07-12 16:00:00', 'CARICO', 'PROD01', 3);

-- Movimenti sparsi per altri prodotti
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (50, '2026-06-15 09:00:00', 'CARICO', 'PROD02', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (30, '2026-06-28 15:30:00', 'SCARICO', 'PROD02', 3);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (100, '2026-07-01 08:00:00', 'CARICO', 'PROD03', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (600, '2026-07-05 10:00:00', 'CARICO', 'PROD04', 3);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (100, '2026-07-14 11:30:00', 'SCARICO', 'PROD04', 2);
INSERT INTO movimento (quantita, data, tipologia, id_prodotto, id_operatore) VALUES (10, '2026-07-15 09:20:00', 'CARICO', 'PROD05', 3);
