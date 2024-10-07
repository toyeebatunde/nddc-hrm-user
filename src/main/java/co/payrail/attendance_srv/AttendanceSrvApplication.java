package co.payrail.attendance_srv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "co.payrail.attendance_srv")
@EnableCaching
@EnableJpaRepositories(basePackages = {
		"co.payrail.attendance_srv.auth.repository",
		"co.payrail.attendance_srv.employer.repository",
		"co.payrail.attendance_srv.kyc_srv.repository"
})

public class AttendanceSrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceSrvApplication.class, args);
	}

}
