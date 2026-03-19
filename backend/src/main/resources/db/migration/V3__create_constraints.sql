
ALTER TABLE tb_veiculo ADD CONSTRAINT pk_veiculo PRIMARY KEY (vei_id);
ALTER TABLE tb_veiculo ADD CONSTRAINT uk_veiculo_placa UNIQUE (vei_placa);


ALTER TABLE tb_regra_financeira ADD CONSTRAINT pk_regrafinanceira PRIMARY KEY (ref_id);


ALTER TABLE tb_movimento_veiculo ADD CONSTRAINT pk_movimentoveiculo PRIMARY KEY (mvv_id);
ALTER TABLE tb_movimento_veiculo ADD CONSTRAINT fk_movimentoveiculo_veiculo_01 FOREIGN KEY (mvv_veiculo) REFERENCES tb_veiculo(vei_id);


ALTER TABLE tb_movimento_financeiro ADD CONSTRAINT pk_movimentofinanceiro PRIMARY KEY (mvf_regra, mvf_movimento);
ALTER TABLE tb_movimento_financeiro ADD CONSTRAINT fk_movfinanceiro_regrafin_01 FOREIGN KEY (mvf_regra) REFERENCES tb_regra_financeira(ref_id);
ALTER TABLE tb_movimento_financeiro ADD CONSTRAINT fk_movfinanceiro_movveiculo_01 FOREIGN KEY (mvf_movimento) REFERENCES tb_movimento_veiculo(mvv_id);
ALTER TABLE tb_movimento_financeiro ADD CONSTRAINT uk_mvf_movimento UNIQUE (mvf_movimento);