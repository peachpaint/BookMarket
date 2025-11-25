-- ===================================================================
-- BookMarket 데이터베이스 스키마 업데이트 스크립트
-- 도메인 엔티티 구조에 맞춰 DB 테이블 수정
-- ===================================================================
-- 실행 방법:
-- 1. MySQL 접속: mysql -u root -p
-- 2. 데이터베이스 선택: USE bookdb;
-- 3. 이 파일 실행: SOURCE c:/BookMarket/db/update-schema.sql;
-- ===================================================================

USE bookdb;

-- ===================================================================
-- 1. Delivery 테이블 수정
-- ===================================================================
-- 문제: 현재 delivery 테이블이 도메인의 Delivery 엔티티와 일치하지 않음
-- 해결: 도메인에 맞춰 컬럼 추가/수정

-- 기존 delivery 테이블 확인
SELECT 'Before modification:' AS 'Status';
DESCRIBE delivery;

-- 1-1. delivery 테이블에 필요한 컬럼 추가 (없는 경우만)
-- recipient_name: 수취인 이름
ALTER TABLE delivery 
ADD COLUMN IF NOT EXISTS recipient_name VARCHAR(100) NOT NULL COMMENT '수취인 이름' AFTER member_id;

-- delivery_date: 배송 날짜
ALTER TABLE delivery 
ADD COLUMN IF NOT EXISTS delivery_date DATETIME NOT NULL COMMENT '배송 날짜' AFTER recipient_name;

-- address_id: Address 테이블 참조
ALTER TABLE delivery 
ADD COLUMN IF NOT EXISTS address_id BIGINT COMMENT '배송 주소 ID' AFTER delivery_date;

-- status: 배송 상태 (ENUM)
ALTER TABLE delivery 
ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'READY' COMMENT '배송 상태' AFTER address_id;

-- note: 배송 메모
ALTER TABLE delivery 
ADD COLUMN IF NOT EXISTS note VARCHAR(500) COMMENT '배송 메모' AFTER status;

-- 1-2. 기존 컬럼 중 불필요한 컬럼이 있다면 제거 (선택사항)
-- 예시: delivery_name 같은 컬럼이 있다면 recipient_name으로 대체
-- ALTER TABLE delivery DROP COLUMN IF EXISTS delivery_name;

-- 1-3. address_id에 외래 키 제약조건 추가
-- 먼저 기존 외래 키가 있는지 확인하고 없으면 추가
SET @fk_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_SCHEMA = 'bookdb' 
    AND TABLE_NAME = 'delivery' 
    AND CONSTRAINT_NAME = 'fk_delivery_address'
);

-- 외래 키 추가 (존재하지 않는 경우)
SET @sql = IF(@fk_exists = 0, 
    'ALTER TABLE delivery ADD CONSTRAINT fk_delivery_address FOREIGN KEY (address_id) REFERENCES address(member_id) ON DELETE SET NULL ON UPDATE CASCADE',
    'SELECT "fk_delivery_address already exists" AS Status'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 수정 후 확인
SELECT 'After modification:' AS 'Status';
DESCRIBE delivery;

-- ===================================================================
-- 2. Address 테이블 확인 및 수정
-- ===================================================================
-- Address는 Member와 1:1 관계 (@MapsId)
-- member_id가 PK이자 FK

SELECT 'Checking address table:' AS 'Status';
DESCRIBE address;

-- 도메인 엔티티 Address의 필드:
-- - memberId (PK, FK)
-- - member (@OneToOne, @MapsId)
-- - country (국가)
-- - zipcode (우편번호)
-- - addressname (주소) -> DB 컬럼: address_name
-- - detailname (상세주소) -> DB 컬럼: detail_name

-- 2-1. address 테이블 필수 컬럼 확인 및 추가
ALTER TABLE address 
ADD COLUMN IF NOT EXISTS country VARCHAR(50) NOT NULL DEFAULT '대한민국' COMMENT '국가' AFTER member_id;

ALTER TABLE address 
ADD COLUMN IF NOT EXISTS zipcode VARCHAR(20) COMMENT '우편번호' AFTER country;

ALTER TABLE address 
ADD COLUMN IF NOT EXISTS address_name VARCHAR(255) COMMENT '주소' AFTER zipcode;

ALTER TABLE address 
ADD COLUMN IF NOT EXISTS detail_name VARCHAR(255) COMMENT '상세주소' AFTER address_name;

-- 2-2. member_id가 PK이자 FK인지 확인
-- member_id는 PK여야 함 (@MapsId 사용)
SELECT CONSTRAINT_NAME, CONSTRAINT_TYPE 
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
WHERE TABLE_SCHEMA = 'bookdb' 
AND TABLE_NAME = 'address' 
AND CONSTRAINT_TYPE = 'PRIMARY KEY';

-- 2-3. member_id 외래 키 확인
SELECT CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' 
AND TABLE_NAME = 'address'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 수정 후 확인
SELECT 'After modification:' AS 'Status';
DESCRIBE address;

-- ===================================================================
-- 3. 외래 키 관계 확인
-- ===================================================================

SELECT '=== Foreign Key Relationships ===' AS 'Info';

-- Delivery 테이블의 모든 외래 키 확인
SELECT 
    CONSTRAINT_NAME AS 'Constraint',
    COLUMN_NAME AS 'Column',
    REFERENCED_TABLE_NAME AS 'References Table',
    REFERENCED_COLUMN_NAME AS 'References Column'
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'bookdb' 
AND TABLE_NAME = 'delivery'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- ===================================================================
-- 4. 기대되는 Delivery 관계
-- ===================================================================
-- 
-- Delivery 엔티티 구조:
-- - id (PK, AUTO_INCREMENT)
-- - member (FK -> member.member_id)
-- - recipientName (수취인 이름)
-- - deliveryDate (배송 날짜)
-- - deliveryAddress (FK -> address.member_id)  <-- OneToOne Address
-- - status (배송 상태 ENUM)
-- - note (배송 메모)
--
-- DB 관계:
-- 1. delivery.member_id -> member.member_id (누가 주문했는지)
-- 2. delivery.address_id -> address.member_id (어디로 배송하는지)
-- 3. orders.delivery_id -> delivery.id (어떤 주문의 배송인지)
--
-- ===================================================================

-- ===================================================================
-- 5. 데이터 마이그레이션 (기존 데이터가 있는 경우)
-- ===================================================================

-- 기존 delivery 데이터 확인
SELECT COUNT(*) AS 'Existing delivery records' FROM delivery;

-- 기존 데이터가 있다면 수동으로 마이그레이션 필요
-- 예시:
-- UPDATE delivery 
-- SET recipient_name = '수취인', 
--     delivery_date = NOW(), 
--     status = 'READY'
-- WHERE recipient_name IS NULL;

-- ===================================================================
-- 완료 메시지
-- ===================================================================

SELECT '====================================' AS '';
SELECT 'Schema Update Completed!' AS 'Status';
SELECT '====================================' AS '';
SELECT 'Please verify the changes:' AS 'Next Steps';
SELECT '1. DESCRIBE delivery;' AS 'Command 1';
SELECT '2. Check foreign keys' AS 'Command 2';
SELECT '3. Test with your application' AS 'Command 3';

