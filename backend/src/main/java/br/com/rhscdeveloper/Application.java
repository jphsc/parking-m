package br.com.rhscdeveloper;

import java.util.TimeZone;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Application {

	public static void main(String... args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        Quarkus.run(args);
    }
}
