package br.com.rhscdeveloper.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.exception.FlywayValidateException;

import br.com.rhscdeveloper.enumerator.Enums.Situacao;
import br.com.rhscdeveloper.enumerator.Enums.SituacaoMovimento;
import br.com.rhscdeveloper.enumerator.Enums.TipoCobranca;
import br.com.rhscdeveloper.enumerator.Enums.TipoMovimento;
import br.com.rhscdeveloper.exception.GlobalException;
import br.com.rhscdeveloper.model.MovimentoFinanceiroVO;
import br.com.rhscdeveloper.model.MovimentoVeiculoVO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;
import br.com.rhscdeveloper.model.VeiculoVO;
import br.com.rhscdeveloper.repository.MovimentoFinanceiroRepository;
import br.com.rhscdeveloper.repository.MovimentoVeiculoRepository;
import br.com.rhscdeveloper.repository.RegraFinanceiraRepository;
import br.com.rhscdeveloper.repository.VeiculoRepository;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Startup
@ApplicationScoped
public class InitDataOnDataBase {

	@ConfigProperty(name = "quarkus.datasource.jdbc.url") private String host;
	@ConfigProperty(name = "quarkus.datasource.username") private String user;
	@ConfigProperty(name = "quarkus.datasource.password") private String password;

	@Inject VeiculoRepository vRepository;
	@Inject RegraFinanceiraRepository rfRepository;
	@Inject MovimentoVeiculoRepository mvRepository;
	@Inject MovimentoFinanceiroRepository mfRepository;
	
	public void onStart(@Observes StartupEvent ev) {
		init();
    }
		
	public void applyMigrations() {
		Flyway flyway = Flyway.configure().dataSource(host, user, password).load();
		
		try {
			flyway.migrate();
		} catch (FlywayValidateException e) {
			e.printStackTrace();
			flyway.repair();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	protected void init() {

		try {
			applyMigrations();
			
			if(vRepository.findAll().list().isEmpty()) {
				vRepository.persist(new VeiculoVO("HB20", "HYUNDAI", LocalDate.now(), "OTO8226"));
				vRepository.persist(new VeiculoVO("ONIX", "CHEVROLET", LocalDate.now(), "OTO8221"));
				vRepository.persist(new VeiculoVO("HB20", "HYUNDAI", LocalDate.now(), "OTO8228"));
			}
			
			if(rfRepository.findAll().list().isEmpty()) {

				rfRepository.persist(new RegraFinanceiraVO("HORA SEMANAL", 35.00, TipoCobranca.INDIFERENTE, TipoMovimento.DIA, LocalDate.now().minusDays(5), LocalDate.now(), Situacao.ATIVO));
				rfRepository.persist(new RegraFinanceiraVO("HORA SEMANAL DESAT", 8.00, TipoCobranca.CREDITO, TipoMovimento.HORA, LocalDate.now().minusDays(5), LocalDate.now().minusDays(2), Situacao.INATIVO));
				rfRepository.persist(new RegraFinanceiraVO("HORA FINAL DE SEMANA", 7.00, TipoCobranca.INDIFERENTE, TipoMovimento.FINAL_SEMANA, LocalDate.now().minusMonths(2), null, Situacao.ATIVO));
				rfRepository.persist(new RegraFinanceiraVO("MENSALISTA EM DINHEIRO", 250.50, TipoCobranca.DINHEIRO, TipoMovimento.MENSALISTA, LocalDate.now().minusDays(34), LocalDate.now(), Situacao.ATIVO));
				rfRepository.persist(new RegraFinanceiraVO("MENSALISTA CARTÃO", 270.79, TipoCobranca.CREDITO, TipoMovimento.MENSALISTA, LocalDate.now().minusWeeks(4), LocalDate.now(), Situacao.ATIVO));
				rfRepository.persist(new RegraFinanceiraVO("FRAÇÃO HORA UTIL INDIFERENTE", 5.50, TipoCobranca.INDIFERENTE, TipoMovimento.DIA, LocalDate.now().minusYears(1), LocalDate.now(), Situacao.ATIVO));
				rfRepository.persist(new RegraFinanceiraVO("FRAÇÃO HORA FINAL DE SEMANA INDIFERENTE", 4.00, TipoCobranca.INDIFERENTE, TipoMovimento.FINAL_SEMANA, LocalDate.now().minusDays(2), LocalDate.now(), Situacao.ATIVO));
			}
			
			if(mvRepository.findAll().list().isEmpty()) {
				mvRepository.persist(new MovimentoVeiculoVO(vRepository.findAll().firstResult(), TipoMovimento.DIA.getId(), LocalDateTime.now(), LocalDateTime.now(), SituacaoMovimento.ENCERRADO.getId()));
			}
			
			if(mfRepository.findAll().list().isEmpty()) {
				RegraFinanceiraVO rf = rfRepository.findAll().firstResult();
				MovimentoVeiculoVO mv = mvRepository.findAll().firstResult();
				mfRepository.persist(new MovimentoFinanceiroVO(rf, mv, 2.00, SituacaoMovimento.ENCERRADO.getId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Erro ao iniciar a aplicação");
		}
	}
}
