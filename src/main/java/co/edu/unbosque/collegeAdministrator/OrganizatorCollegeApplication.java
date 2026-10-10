package co.edu.unbosque.collegeAdministrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrganizatorCollegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganizatorCollegeApplication.class, args);
	}

}
