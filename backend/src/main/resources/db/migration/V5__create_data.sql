
INSERT INTO tb_veiculo (vei_id, vei_modelo, vei_montadora, vei_placa, vei_dt_registro, vei_versao)
VALUES 
	(nextval('tb_veiculo_vei_id_seq'), 'HB20', 'HYUNDAI', 'OTO8226',   current_date, current_timestamp),
	(nextval('tb_veiculo_vei_id_seq'), 'ONIX', 'CHEVROLET', 'OTO8221', current_date, current_timestamp),
	(nextval('tb_veiculo_vei_id_seq'), 'HB20', 'HYUNDAI', 'OTO8228',   current_date, current_timestamp);


INSERT INTO tb_regra_financeira
	(ref_id, ref_descricao, ref_valor, ref_metodo_pag, ref_tipo_movimento, ref_ini_validade, ref_fim_validade, ref_situacao, ref_versao)
VALUES
	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA SEMANAL', 35.00, 7, 8, CURRENT_TIMESTAMP - INTERVAL '5 DAY', CURRENT_DATE, 2, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA SEMANAL DESAT', 8.00, 6, 18, CURRENT_TIMESTAMP - INTERVAL '5 DAY', CURRENT_DATE - INTERVAL '2 DAY', 3, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA FINAL DE SEMANA', 7.00, 7, 9, CURRENT_TIMESTAMP - INTERVAL '2 MONTH', NULL, 2, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'MENSALISTA EM DINHEIRO', 250.50, 4, 10, CURRENT_TIMESTAMP - INTERVAL '34 DAY', CURRENT_DATE, 2, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'MENSALISTA CARTÃO', 270.79, 6, 10, CURRENT_TIMESTAMP - INTERVAL '4 WEEK', CURRENT_DATE, 2, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'FRAÇÃO HORA UTIL INDIFERENTE', 5.50, 7, 8, CURRENT_TIMESTAMP - INTERVAL '1 YEAR', CURRENT_DATE, 2, CURRENT_TIMESTAMP),
	(nextval('tb_regra_financeira_ref_id_seq'), 'FRAÇÃO HORA FINAL DE SEMANA INDIFERENTE', 4.00, 7, 9, CURRENT_TIMESTAMP - INTERVAL '2 DAY', CURRENT_DATE, 2, CURRENT_TIMESTAMP);


INSERT INTO tb_movimento_veiculo
	(mvv_id, mvv_veiculo, mvv_tipo_movimento, mvv_dt_hr_entrada, mvv_dt_hr_saida, mvv_situacao, mvv_versao)
VALUES
	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8221'), 
	8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13, CURRENT_TIMESTAMP);


INSERT INTO tb_movimento_financeiro 
	(mvf_regra, mvf_movimento, mvf_valor, mvf_situacao, mvf_versao)
VALUES
	((SELECT ref_id FROM tb_regra_financeira WHERE ref_descricao ilike 'HORA SEMANAL'), 
	(SELECT mvv_id FROM tb_movimento_veiculo WHERE mvv_veiculo = (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8221')), 
	100.00, 13, CURRENT_TIMESTAMP);
	