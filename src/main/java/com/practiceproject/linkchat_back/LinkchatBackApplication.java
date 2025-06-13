package com.practiceproject.linkchat_back;

import com.practiceproject.linkchat_back.model.User;
import com.practiceproject.linkchat_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LinkchatBackApplication {

//	@Bean
//	CommandLineRunner init(UserRepository repo, BCryptPasswordEncoder passwordEncoder) {
//		return args -> {
//			if (repo.findByUsername("admin3") == null) {
//				User admin = new User();
//				admin.setUsername("admin3");
//				admin.setPassword(passwordEncoder.encode("mysecretpass"));
//				repo.save(admin);
//			}
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(LinkchatBackApplication.class, args);
	}
}
