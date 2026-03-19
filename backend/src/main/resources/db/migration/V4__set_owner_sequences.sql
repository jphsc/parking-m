-- Define a propriedade OWNED BY para as tabelas/colunas
ALTER SEQUENCE tb_veiculo_vei_id_seq OWNED BY tb_veiculo.vei_id;
ALTER SEQUENCE tb_regra_financeira_ref_id_seq OWNED BY tb_regra_financeira.ref_id;
ALTER SEQUENCE tb_movimento_veiculo_mvv_id_seq OWNED BY tb_movimento_veiculo.mvv_id;