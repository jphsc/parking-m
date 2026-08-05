package br.com.rhscdev.integration;

import org.junit.jupiter.api.AfterEach;

import br.com.rhscdev.infrastructure.persistence.MovFinanceiroPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.RegraFinancPanacheRepository;
import br.com.rhscdev.infrastructure.persistence.VeiculoPanacheRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public abstract class BaseIntegracao {
	
	@Inject private VeiculoPanacheRepository vRepository;
	@Inject private RegraFinancPanacheRepository rfRepository;
	@Inject private MovFinanceiroPanacheRepository mvRepository;
	
	@AfterEach
	@TestTransaction
	void clean() {
		vRepository.deleteAll();
		rfRepository.deleteAll();
		mvRepository.deleteAll();
	}
}
