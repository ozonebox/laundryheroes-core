-- V2__extend_laundry_services_item_type_check.sql
-- Extend allowed item_type values for laundry_services

ALTER TABLE laundry_services
DROP CONSTRAINT IF EXISTS laundry_services_item_type_check;

ALTER TABLE laundry_services
ADD CONSTRAINT laundry_services_item_type_check
CHECK (
  item_type IN (
    -- existing
    'GHOTRA',
    'THOB',
    'THOB_WOOL',
    'BISHT',
    'JACKET',
    'ABAYA',
    'BLOUSE',
    'WEDDING_DRESS',
    'SPECIAL_SARI',
    'DRESS',
    'BED_SHEET',
    'CURTAINS_BIG',
    'PILLOW',

    -- bedding
    'BLANKET_SINGLE',
    'BLANKET_DOUBLE',
    'DUVET_SINGLE',
    'DUVET_DOUBLE',
    'BED_COVER',
    'BED_SHEET_SINGLE',
    'BED_SHEET_DOUBLE',
    'PILLOW_COVER',
    'BED_SPREAD_COVER',

    -- clothing
    'SWEATER',
    'SKIRT',
    'PANTS',
    'COAT',
    'MILITARY_SUIT',
    'SCHOOL_UNIFORM',

    -- bath
    'TOWEL',
    'BATH_ROBE',
    'BATH_MAT',

    -- accessories
    'CAP',
    'TIE',

    -- regional
    'SHAL_GHOTRA',
    'TOUB_COLORED',

    -- home
    'APRON',
    'CARPET',
    'CURTAINS_SMALL',

    -- new additions
    'SHIRT',
    'T_SHIRT',
    'UNDERPANTS',
    'SOCKS',
    'SERWAL',
    'SARI_NORMAL',
    'SHAYLA_HIJAB',
    'SUIT_2PCS',
    'DOCTOR_ROBE',
    'OVERALL',
    'PYJAMA',
    'NIGHT_WEAR',
    'SCARF',
    'GLOVES',
    'FLAG',
    'MATTRESS_PROTECTOR',
    'PILLOW_CASE',
    'FACE_TOWEL',
    'HAND_TOWEL',
    'KITCHEN_TOWEL',
    'TABLE_COVER',
    'CHAIR_COVER',
    'SOFA_COVER'
  )
);
