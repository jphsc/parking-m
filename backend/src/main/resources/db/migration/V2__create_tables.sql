
CREATE TABLE tb_veiculo (
	vei_id INTEGER DEFAULT nextval('tb_veiculo_vei_id_seq'),
	vei_modelo VARCHAR(20) NOT NULL,
	vei_montadora VARCHAR(20) NOT NULL,
	vei_dt_registro DATE NOT NULL,
	vei_placa VARCHAR(7) NOT NULL,
	vei_versao TIMESTAMP NOT NULL
);

CREATE TABLE tb_regra_financeira(
	ref_id INTEGER DEFAULT nextval('tb_regra_financeira_ref_id_seq'),
	ref_descricao VARCHAR(50) NOT NULL,
	ref_valor NUMERIC(6,2) NOT NULL,
	ref_metodo_pag INTEGER NOT NULL,
	ref_tipo_movimento INTEGER NOT NULL,
	ref_ini_validade DATE NOT NULL,
	ref_fim_validade DATE NULL,
	ref_situacao INTEGER NOT NULL,
	ref_versao TIMESTAMP NOT NULL
);

CREATE TABLE tb_movimento_veiculo (
	mvv_id INTEGER DEFAULT nextval('tb_movimento_veiculo_mvv_id_seq'),
	mvv_veiculo INTEGER NOT NULL,
	mvv_tipo_movimento INTEGER NOT NULL,
	mvv_dt_hr_entrada TIMESTAMP NOT NULL,
	mvv_dt_hr_saida TIMESTAMP NULL,
	mvv_situacao INTEGER NOT NULL,
	mvv_versao TIMESTAMP NOT NULL
);

CREATE TABLE tb_movimento_financeiro (
	mvf_regra INTEGER NOT NULL,
	mvf_movimento INTEGER NOT NULL,
	mvf_valor NUMERIC(6,2) NOT NULL,
	mvf_situacao INTEGER NOT NULL,
	mvf_versao TIMESTAMP NOT NULL
);