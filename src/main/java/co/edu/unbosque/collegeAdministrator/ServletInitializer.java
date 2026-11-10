package co.edu.unbosque.collegeAdministrator;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador que permite desplegar la aplicación como WAR en un contenedor de servlets externo.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Registra la clase principal como fuente de configuración de la aplicación.
	 *
	 * @param application el constructor de la aplicación
	 * @return el constructor con la fuente registrada
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OrganizatorCollegeApplication.class);
	}

}
