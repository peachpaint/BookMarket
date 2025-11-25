-- ===================================================================
-- BookMarket 데이터베이스 스키마 확인 스크립트
-- ===================================================================
-- 이 스크립트를 MySQL에서 실행하여 테이블 구조와 관계를 확인하세요.
-- 
-- 실행 방법:
-- 1. MySQL 접속: mysql -u root -p
-- 2. 데이터베이스 선택: USE bookdb;
-- 3. 이 파일 실행: SOURCE c:/BookMarket/db/verify-schema.sql;
--    또는 MySQL Workbench에서 파일 열고 실행
-- ===================================================================

-- 현재 데이터베이스 확인
SELECT DATABASE() AS 'Current Database';

-- 1. 모든 테이블 목록 확인
SHOW TABLES;

-- 2. Member 테이블 구조 확인
DESCRIBE member;

-- 3. Address 테이블 구조 확인
DESCRIBE address;

-- 4. Delivery 테이블 구조 확인
DESCRIBE delivery;

-- 5. Orders 테이블 구조 확인
DESCRIBE orders;

-- 6. Order_Item 테이블 구조 확인
DESCRIBE order_item;

-- 7. Book 테이블 구조 확인
DESCRIBE book;

-- ===================================================================
-- 외래 키(Foreign Key) 관계 확인
-- ===================================================================

-- Address 테이블의 외래 키 확인 (member_id -> member.member_id)
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' AND TABLE_NAME = 'address'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Delivery 테이블의 외래 키 확인 (member_id -> member.member_id)
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' AND TABLE_NAME = 'delivery'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Orders 테이블의 외래 키 확인 (member_id -> member.member_id, delivery_id -> delivery.delivery_id)
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' AND TABLE_NAME = 'orders'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Order_Item 테이블의 외래 키 확인 (order_id -> orders.order_id, book_id -> book.book_id)
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' AND TABLE_NAME = 'order_item'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- ===================================================================
-- 모든 테이블의 외래 키 한눈에 보기
-- ===================================================================
SELECT 
    CONCAT(TABLE_NAME, '.', COLUMN_NAME) AS 'Foreign Key Column',
    CONCAT(REFERENCED_TABLE_NAME, '.', REFERENCED_COLUMN_NAME) AS 'References',
    CONSTRAINT_NAME AS 'Constraint Name'
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb'
AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, COLUMN_NAME;

-- ===================================================================
-- 기대되는 테이블 관계
-- ===================================================================
-- 
-- 1. member (1) <---> (1) address
--    - address.member_id (PK, FK) -> member.member_id (PK)
--    - @MapsId로 같은 ID 공유
--
-- 2. member (1) <---> (N) delivery
--    - delivery.member_id (FK) -> member.member_id (PK)
--
-- 3. orders (N) <---> (1) member
--    - orders.member_id (FK) -> member.member_id (PK)
--
-- 4. orders (1) <---> (1) delivery
--    - orders.delivery_id (FK) -> delivery.delivery_id (PK)
--
-- 5. orders (1) <---> (N) order_item
--    - order_item.order_id (FK) -> orders.order_id (PK)
--
-- 6. order_item (N) <---> (1) book
--    - order_item.book_id (FK) -> book.book_id (PK)
--
-- ===================================================================

-- ===================================================================
-- 데이터 샘플 확인 (존재하는 경우)
-- ===================================================================

-- Member 데이터 확인
SELECT member_id, name, phone, city, role 
FROM member 
LIMIT 5;

-- Address 데이터 확인
SELECT a.member_id, a.country, a.zipcode, a.addressname, m.name AS member_name
FROM address a
LEFT JOIN member m ON a.member_id = m.member_id
LIMIT 5;

-- Delivery 데이터 확인
SELECT d.delivery_id, d.delivery_name, d.delivery_date, m.name AS member_name
FROM delivery d
LEFT JOIN member m ON d.member_id = m.member_id
LIMIT 5;

-- Orders 데이터 확인
SELECT o.order_id, o.shipping_date, m.name AS member_name, d.delivery_name
FROM orders o
LEFT JOIN member m ON o.member_id = m.member_id
LEFT JOIN delivery d ON o.delivery_id = d.delivery_id
LIMIT 5;

-- Order_Item 데이터 확인
SELECT oi.order_item_id, oi.order_id, b.name AS book_name, oi.quantity, oi.unit_price
FROM order_item oi
LEFT JOIN book b ON oi.book_id = b.book_id
LIMIT 5;

-- ===================================================================
-- 테이블 생성 문장 확인 (전체 구조 상세 보기)
-- ===================================================================

SHOW CREATE TABLE member;
SHOW CREATE TABLE address;
SHOW CREATE TABLE delivery;
SHOW CREATE TABLE orders;
SHOW CREATE TABLE order_item;
SHOW CREATE TABLE book;

-- ===================================================================
-- 완료 메시지
-- ===================================================================
SELECT '스키마 확인 완료!' AS 'Status';
