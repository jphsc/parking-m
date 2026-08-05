package br.com.rhscdev;

import java.io.IOException;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@QuarkusMain
public class Application {

	public static void main(String... args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        Quarkus.run(args);
    }
	
	@Provider
	public static class HttpInterceptorRequests implements ContainerRequestFilter {

		private static final Logger LOG = Logger.getLogger(HttpInterceptorRequests.class.getName());
		
		@Override
		public void filter(ContainerRequestContext requestContext) throws IOException {
			// TODO Auto-generated method stub
			
			String method = requestContext.getMethod();
	        String path = requestContext.getUriInfo().getRequestUri().getPath();
	        
	        LOG.info(String.format("Requisição interceptada: %s %s", method, path));
	        
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@ApplicationScoped
	public static class Init {
		
		@Inject
		EntityManager em;
		
		@Transactional
		public void initData(@Observes StartupEvent event) {
			
			String profile = ConfigProvider.getConfig()
		            .getOptionalValue("quarkus.profile", String.class)
		            .orElse("unknown");
			
			if ("dev".equals(profile)) {
				String sql = "\n"
						+ "INSERT INTO tb_veiculo (vei_id, vei_modelo, vei_montadora, vei_placa, vei_dt_registro, vei_versao)\n"
						+ "VALUES \n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'HB20', 'HYUNDAI', 'OTO8226',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'ONIX', 'CHEVROLET', 'OTO8221', current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'HB20', 'HYUNDAI', 'OTO8228',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX1',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX2',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX3',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX4',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX5',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX6',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX7',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX8',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXXX9',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX10',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX11',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX12',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX13',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX14',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX15',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX16',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX17',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX18',   current_date, current_timestamp),\n"
						+ "	(nextval('tb_veiculo_vei_id_seq'), 'XXXX', 'XXXX', 'XXXXX19',   current_date, current_timestamp);\n"
						+ "\n"
						+ "\n"
						+ "INSERT INTO tb_regra_financeira\n"
						+ "	(ref_id, ref_descricao, ref_valor, ref_metodo_pag, ref_tipo_movimento, ref_ini_validade, ref_fim_validade, ref_situacao, ref_versao)\n"
						+ "VALUES\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA SEMANAL', 35.00, 7, 8, DATEADD('DAY', -5, CURRENT_TIMESTAMP), CURRENT_DATE, 2, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA SEMANAL DESAT', 8.00, 6, 18, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP), 3, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'HORA FINAL DE SEMANA', 7.00, 7, 9, DATEADD('MONTH', -2, CURRENT_TIMESTAMP), NULL, 2, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'MENSALISTA EM DINHEIRO', 250.50, 4, 10, DATEADD('DAY', -34, CURRENT_TIMESTAMP), CURRENT_DATE, 2, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'MENSALISTA CARTÃO', 270.79, 6, 10, DATEADD('WEEK', -4, CURRENT_TIMESTAMP), CURRENT_DATE, 2, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'FRAÇÃO HORA UTIL INDIFERENTE', 5.50, 7, 8, DATEADD('YEAR', -1, CURRENT_TIMESTAMP), CURRENT_DATE, 2, CURRENT_TIMESTAMP),\n"
						+ "	(nextval('tb_regra_financeira_ref_id_seq'), 'FRAÇÃO HORA FINAL DE SEMANA INDIFERENTE', 4.00, 7, 9, DATEADD('DAY', -2, CURRENT_TIMESTAMP), CURRENT_DATE, 2, CURRENT_TIMESTAMP);\n"
						+ "\n"
						+ "\n"
						+ "INSERT INTO tb_movimento_veiculo\n"
						+ "	(mvv_id, mvv_veiculo, mvv_tipo_movimento, mvv_dt_hr_entrada, mvv_dt_hr_saida, mvv_situacao, mvv_versao)\n"
						+ "VALUES\n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8221'), 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 13, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8226'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8226'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8228'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP), \n"
						+ "	(nextval('tb_movimento_veiculo_mvv_id_seq'), (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8226'), 8, CURRENT_TIMESTAMP, NULL, 12, CURRENT_TIMESTAMP); \n"
						+ "\n"
						+ "\n"
						+ "INSERT INTO tb_movimento_financeiro \n"
						+ "	(mvf_regra, mvf_movimento, mvf_valor, mvf_situacao, mvf_versao)\n"
						+ "VALUES\n"
						+ "	((SELECT ref_id FROM tb_regra_financeira WHERE ref_descricao ilike 'HORA SEMANAL'), \n"
						+ "	(SELECT mvv_id FROM tb_movimento_veiculo WHERE mvv_veiculo = (SELECT vei_id FROM tb_veiculo WHERE vei_placa ilike 'OTO8221')), \n"
						+ "	100.00, 13, CURRENT_TIMESTAMP);\n"
						+ "	";
				
				Query query = em.createNativeQuery(sql);
				query.executeUpdate();
			}
		}
	}
}
