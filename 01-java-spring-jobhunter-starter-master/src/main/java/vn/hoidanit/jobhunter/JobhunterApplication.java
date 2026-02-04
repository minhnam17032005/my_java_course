package vn.hoidanit.jobhunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import vn.hoidanit.jobhunter.repository.PermissionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import vn.hoidanit.jobhunter.repository.UserRepository;

//disable security
// @SpringBootApplication(exclude = {
// 		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
// 		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
// })

@SpringBootApplication
@EnableAsync //xử lí bất đồng bộ 
public class JobhunterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobhunterApplication.class, args);
    }
}

