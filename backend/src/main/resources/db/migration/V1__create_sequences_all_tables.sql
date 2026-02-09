
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'tb_veiculo_vei_id_seq') THEN
        CREATE SEQUENCE tb_veiculo_vei_id_seq START WITH 1 INCREMENT BY 1;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'tb_regra_financeira_ref_id_seq') THEN
        CREATE SEQUENCE tb_regra_financeira_ref_id_seq START WITH 1 INCREMENT BY 1;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'tb_movimento_veiculo_mvv_id_seq') THEN
        CREATE SEQUENCE tb_movimento_veiculo_mvv_id_seq START WITH 1 INCREMENT BY 1;
    END IF;
END
$$;

-- Define a propriedade OWNED BY para as tabelas/colunas
ALTER SEQUENCE tb_veiculo_vei_id_seq OWNED BY tb_veiculo.vei_id;
ALTER SEQUENCE tb_regra_financeira_ref_id_seq OWNED BY tb_regra_financeira.ref_id;
ALTER SEQUENCE tb_movimento_veiculo_mvv_id_seq OWNED BY tb_movimento_veiculo.mvv_id;
