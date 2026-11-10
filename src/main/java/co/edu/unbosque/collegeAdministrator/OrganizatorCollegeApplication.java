package co.edu.unbosque.collegeAdministrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada de la aplicación Spring Boot de gestión de horarios y matrícula. Habilita además las
 * tareas programadas.
 */
@SpringBootApplication
@EnableScheduling
public class OrganizatorCollegeApplication {

	/**
	 * Inicia la aplicación.
	 *
	 * @param args los argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrganizatorCollegeApplication.class, args);
	}

}
