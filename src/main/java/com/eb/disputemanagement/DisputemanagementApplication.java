package com.eb.disputemanagement;

import com.eb.disputemanagement.dispute.atmElectronicJournal.AtmElectronicJournal;
import com.eb.disputemanagement.dispute.config.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableConfigurationProperties({AtmElectronicJournal.class})
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class DisputemanagementApplication {

	@Bean
	AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }

	public static void main(String[] args) {
		SpringApplication.run(DisputemanagementApplication.class, args);
	}

}
