-- =====================================================
-- Kateqoriyalar
-- Slug-lar SlugGeneratorService formatına uyğundur:
-- kiçik hərf, türk hərfləri normallaşdırılmış (ç->c, ş->s, ü->u, ...), boşluqlar defis
-- =====================================================
INSERT INTO category (name, slug, created_at, updated_at)
VALUES ('Boşqablar', 'bosqablar', NOW(), NOW()),
       ('Kasalar', 'kasalar', NOW(), NOW()),
       ('Vazalar', 'vazalar', NOW(), NOW()),
       ('Fincanlar', 'fincanlar', NOW(), NOW()),
       ('Tabaklar', 'tabaklar', NOW(), NOW()),
       ('Hediyelik Setler', 'hediyelik-setler', NOW(), NOW()),
       ('Dekoratif Ürünler', 'dekoratif-urunler', NOW(), NOW()),
       ('İznik Desenliler', 'iznik-desenliler', NOW(), NOW());

-- =====================================================
-- Məhsullar
-- category_id identity dəyərlərinə güvənmək əvəzinə slug üzərindən tapılır.
-- products.slug unikaldır: təkrarlanan adlarda generator kimi "-1" suffiksi işlədilir.
-- =====================================================

-- Boşqablar
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('El Yapımı Kütahya Çini Tabak', 'el-yapimi-kutahya-cini-tabak', 450.00, 10, 'KTC-PLT-001',
        'Geleneksel el yapımı, özgün desenli tabak',
        (SELECT id FROM category WHERE slug = 'bosqablar'), NOW(), NOW()),
       ('Kütahya Çini Süslemeli Tabak', 'kutahya-cini-suslemeli-tabak', 520.00, 8, 'KTC-PLT-002',
        'El işi, kırmızı ve mavi desenli tabak',
        (SELECT id FROM category WHERE slug = 'bosqablar'), NOW(), NOW()),
       ('İznik Desenli Tabak', 'iznik-desenli-tabak', 480.00, 12, 'KTC-PLT-003',
        'İznik desenli, özel boyalı tabak',
        (SELECT id FROM category WHERE slug = 'bosqablar'), NOW(), NOW());

-- Kasalar
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Geleneksel Çini Kase', 'geleneksel-cini-kase', 350.00, 15, 'KTC-BWL-001',
        'Geleneksel motifli, el yapımı kase',
        (SELECT id FROM category WHERE slug = 'kasalar'), NOW(), NOW()),
       ('Derin Çini Kase', 'derin-cini-kase', 390.00, 10, 'KTC-BWL-002',
        'Çorba ve yemek için derin kase',
        (SELECT id FROM category WHERE slug = 'kasalar'), NOW(), NOW()),
       ('Minyatür Desenli Kase', 'minyatur-desenli-kase', 280.00, 20, 'KTC-BWL-003',
        'Minyatür desenli, dekoratif kase',
        (SELECT id FROM category WHERE slug = 'kasalar'), NOW(), NOW());

-- Vazalar
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Dekoratif Çini Vazo', 'dekoratif-cini-vazo', 750.00, 5, 'KTC-VSE-001',
        'Büyük boy, el yapımı dekoratif vazo',
        (SELECT id FROM category WHERE slug = 'vazalar'), NOW(), NOW()),
       ('Mini Çini Vazo', 'mini-cini-vazo', 420.00, 7, 'KTC-VSE-002',
        'Mini boy, hediye için uygun vazo',
        (SELECT id FROM category WHERE slug = 'vazalar'), NOW(), NOW()),
       ('Uzun Boyunlu Çini Vazo', 'uzun-boyunlu-cini-vazo', 890.00, 4, 'KTC-VSE-003',
        'Uzun boyunlu, özel desenli vazo',
        (SELECT id FROM category WHERE slug = 'vazalar'), NOW(), NOW());

-- Fincanlar
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('İznik Desenli Fincan', 'iznik-desenli-fincan', 250.00, 20, 'KTC-CUP-001',
        'İznik desenli, fincan takımı',
        (SELECT id FROM category WHERE slug = 'fincanlar'), NOW(), NOW()),
       ('Kütahya Çini Fincan', 'kutahya-cini-fincan', 230.00, 25, 'KTC-CUP-002',
        'Geleneksel desenli fincan',
        (SELECT id FROM category WHERE slug = 'fincanlar'), NOW(), NOW()),
       ('Altın Desenli Fincan', 'altin-desenli-fincan', 350.00, 15, 'KTC-CUP-003',
        'Altın desenli, özel günler için',
        (SELECT id FROM category WHERE slug = 'fincanlar'), NOW(), NOW());

-- Tabaklar
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Servis Tabak', 'servis-tabak', 550.00, 8, 'KTC-TAB-001',
        'Büyük boy servis tabak',
        (SELECT id FROM category WHERE slug = 'tabaklar'), NOW(), NOW()),
       ('Yemek Tabak', 'yemek-tabak', 320.00, 18, 'KTC-TAB-002',
        'Günlük kullanım için yemek tabak',
        (SELECT id FROM category WHERE slug = 'tabaklar'), NOW(), NOW());

-- Hediyelik Setler
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Hediye Seti (Tabak + Kase)', 'hediye-seti-tabak-kase', 650.00, 8, 'KTC-GFT-001',
        'Özel hediye seti, şık kutuda',
        (SELECT id FROM category WHERE slug = 'hediyelik-setler'), NOW(), NOW()),
       ('Çini Kahve Seti', 'cini-kahve-seti', 890.00, 5, 'KTC-GFT-002',
        '6 kişilik kahve fincan seti',
        (SELECT id FROM category WHERE slug = 'hediyelik-setler'), NOW(), NOW());

-- Dekoratif Ürünler
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('Dekoratif Çini Tablo', 'dekoratif-cini-tablo', 1250.00, 3, 'KTC-DCR-001',
        'Duvara asılan çini tablo',
        (SELECT id FROM category WHERE slug = 'dekoratif-urunler'), NOW(), NOW()),
       ('Çini Süs Eşyası', 'cini-sus-esyasi', 180.00, 30, 'KTC-DCR-002',
        'Küçük dekoratif süs eşyası',
        (SELECT id FROM category WHERE slug = 'dekoratif-urunler'), NOW(), NOW());

-- İznik Desenliler
INSERT INTO products (name, slug, price, stock, stock_keeping_unit, description, category_id, created_at, updated_at)
VALUES ('İznik Desenli Tabak', 'iznik-desenli-tabak-1', 520.00, 9, 'KTC-IZN-001',
        'Özgün İznik desenli tabak',
        (SELECT id FROM category WHERE slug = 'iznik-desenliler'), NOW(), NOW()),
       ('İznik Desenli Kase', 'iznik-desenli-kase', 420.00, 12, 'KTC-IZN-002',
        'İznik desenli kase',
        (SELECT id FROM category WHERE slug = 'iznik-desenliler'), NOW(), NOW());

-- =====================================================
-- Admin istifadəçi (şifrə: bcrypt hash, dəyişməyib)
-- =====================================================
INSERT INTO users (name, last_name, email, password, phone_number, is_active, is_verified, role, created_at, updated_at)
VALUES ('Ekrem', 'Abi', 'admin@kutahya.com', '$2a$12$DF3Wje209NG0zKtWUEOOi.zhK26Yb4spDLPG8HYH17OjRJHWXkgQa',
        '05551234567', true, true, 'ADMIN', NOW(), NOW());
