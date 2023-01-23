package com.eb.disputemanagement.dispute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = NaturalRepositoryImpl.class, basePackages = "com.eb.disputemanagement")
public class JpaEnversConfiguration {
    @Bean
    AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }
}
