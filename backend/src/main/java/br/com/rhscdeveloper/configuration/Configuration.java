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
public class Configuration {

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
		
		Flyway flyway = null;
		
		try {
			flyway = Flyway.configure().dataSource(host, user, password).load();
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
//			applyMigrations();
			
			if(vRepository.findAll().list().isEmpty()) {
				vRepository.persist(new VeiculoVO("HB20", "Hyundai", LocalDateTime.now(), "OTO8226", LocalDateTime.now()));
				vRepository.persist(new VeiculoVO("Onix", "Chevrolet", LocalDateTime.now(), "OTO8221", LocalDateTime.now()));
				vRepository.persist(new VeiculoVO("HB20", "Hyundai", LocalDateTime.now(), "OTO8228", LocalDateTime.now()));
			}
			
			if(rfRepository.findAll().list().isEmpty()) {
				rfRepository.persist(new RegraFinanceiraVO("Hora semanal", 9.00, TipoCobranca.INDIFERENTE, TipoMovimento.DIA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
				rfRepository.persist(new RegraFinanceiraVO("Hora final de semana", 7.00, TipoCobranca.INDIFERENTE, TipoMovimento.FINAL_SEMANA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
				rfRepository.persist(new RegraFinanceiraVO("Mensalista em dinheiro", 250.50, TipoCobranca.DINHEIRO, TipoMovimento.MENSALISTA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
				rfRepository.persist(new RegraFinanceiraVO("Mensalista no cartão", 270.79, TipoCobranca.CREDITO, TipoMovimento.MENSALISTA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
				rfRepository.persist(new RegraFinanceiraVO("Fração de hora util indiferente", 5.50, TipoCobranca.INDIFERENTE, TipoMovimento.DIA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
				rfRepository.persist(new RegraFinanceiraVO("Fração de hora final de semana indiferente", 4.00, TipoCobranca.INDIFERENTE, TipoMovimento.FINAL_SEMANA, LocalDate.now(), LocalDate.now(), Situacao.ATIVO, LocalDateTime.now()));
			}
			
			if(mvRepository.findAll().list().isEmpty()) {
				mvRepository.persist(new MovimentoVeiculoVO(vRepository.findAll().firstResult(), TipoMovimento.DIA.getId(), LocalDateTime.now(), LocalDateTime.now(), SituacaoMovimento.ENCERRADO.getId(), LocalDateTime.now()));
			}
			
			if(mfRepository.findAll().list().isEmpty()) {
				RegraFinanceiraVO rf = rfRepository.findAll().firstResult();
				MovimentoVeiculoVO mv = mvRepository.findAll().firstResult();
				
				mfRepository.persist(new MovimentoFinanceiroVO(rf, mv, 2.00, SituacaoMovimento.ABERTO, LocalDateTime.now()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Erro ao iniciar a aplicação");
		}
	}
}
