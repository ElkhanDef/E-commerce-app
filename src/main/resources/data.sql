-- Kateqoriyalar (created_at və updated_at əlavə et)
INSERT INTO category (name, created_at, updated_at)
VALUES ('Boşqablar', NOW(), NOW()),
       ('Kasalar', NOW(), NOW()),
       ('Vazalar', NOW(), NOW()),
       ('Fincanlar', NOW(), NOW()),
       ('Tabaklar', NOW(), NOW()),
       ('Hediyelik Setler', NOW(), NOW()),
       ('Dekoratif Ürünler', NOW(), NOW()),
       ('İznik Desenliler', NOW(), NOW());

-- Məhsullar (created_at, updated_at əlavə et)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('El Yapımı Kütahya Çini Tabak', 450.00, 10, 'KTC-PLT-001', 'Geleneksel el yapımı, özgün desenli tabak', 1,
        NOW(), NOW()),
       ('Kütahya Çini Süslemeli Tabak', 520.00, 8, 'KTC-PLT-002', 'El işi, kırmızı ve mavi desenli tabak', 1, NOW(),
        NOW()),
       ('İznik Desenli Tabak', 480.00, 12, 'KTC-PLT-003', 'İznik desenli, özel boyalı tabak', 1, NOW(), NOW());

-- Kasalar (category_id = 2)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Geleneksel Çini Kase', 350.00, 15, 'KTC-BWL-001', 'Geleneksel motifli, el yapımı kase', 2, NOW(), NOW()),
       ('Derin Çini Kase', 390.00, 10, 'KTC-BWL-002', 'Çorba ve yemek için derin kase', 2, NOW(), NOW()),
       ('Minyatür Desenli Kase', 280.00, 20, 'KTC-BWL-003', 'Minyatür desenli, dekoratif kase', 2, NOW(), NOW());

-- Vazalar (category_id = 3)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Dekoratif Çini Vazo', 750.00, 5, 'KTC-VSE-001', 'Büyük boy, el yapımı dekoratif vazo', 3, NOW(), NOW()),
       ('Mini Çini Vazo', 420.00, 7, 'KTC-VSE-002', 'Mini boy, hediye için uygun vazo', 3, NOW(), NOW()),
       ('Uzun Boyunlu Çini Vazo', 890.00, 4, 'KTC-VSE-003', 'Uzun boyunlu, özel desenli vazo', 3, NOW(), NOW());

-- Fincanlar (category_id = 4)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('İznik Desenli Fincan', 250.00, 20, 'KTC-CUP-001', 'İznik desenli, fincan takımı', 4, NOW(), NOW()),
       ('Kütahya Çini Fincan', 230.00, 25, 'KTC-CUP-002', 'Geleneksel desenli fincan', 4, NOW(), NOW()),
       ('Altın Desenli Fincan', 350.00, 15, 'KTC-CUP-003', 'Altın desenli, özel günler için', 4, NOW(), NOW());

-- Tabaklar (category_id = 5)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Servis Tabak', 550.00, 8, 'KTC-TAB-001', 'Büyük boy servis tabak', 5, NOW(), NOW()),
       ('Yemek Tabak', 320.00, 18, 'KTC-TAB-002', 'Günlük kullanım için yemek tabak', 5, NOW(), NOW());

-- Hediyelik Setler (category_id = 6)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Hediye Seti (Tabak + Kase)', 650.00, 8, 'KTC-GFT-001', 'Özel hediye seti, şık kutuda', 6, NOW(), NOW()),
       ('Çini Kahve Seti', 890.00, 5, 'KTC-GFT-002', '6 kişilik kahve fincan seti', 6, NOW(), NOW());

-- Dekoratif Ürünler (category_id = 7)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Dekoratif Çini Tablo', 1250.00, 3, 'KTC-DCR-001', 'Duvara asılan çini tablo', 7, NOW(), NOW()),
       ('Çini Süs Eşyası', 180.00, 30, 'KTC-DCR-002', 'Küçük dekoratif süs eşyası', 7, NOW(), NOW());

-- İznik Desenliler (category_id = 8)
INSERT INTO products (name, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('İznik Desenli Tabak', 520.00, 9, 'KTC-IZN-001', 'Özgün İznik desenli tabak', 8, NOW(), NOW()),
       ('İznik Desenli Kase', 420.00, 12, 'KTC-IZN-002', 'İznik desenli kase', 8, NOW(), NOW());

-- Admin (created_at, updated_at əlavə et)
INSERT INTO users (name, last_name, email, password, phone_number, is_active, is_verified, role, created_at, updated_at)
VALUES ('Admin', 'Kütahya', 'admin@kutahya.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E',
        '05551234567', true, true, 'ADMIN', NOW(), NOW());