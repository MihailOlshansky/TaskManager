package org.molsh.config;

import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {
    private final static long seed = 715381273942732865L;

    @Bean(name = "defaultTaskExecutor")
    @Primary
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("DefaultExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "bigTaskExecutor")
    public ThreadPoolTaskExecutor bigTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("BigExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    @Scope("singleton")
    public PasswordEncoder passwordEncoder() {
        SecureRandom rnd = new SecureRandom();
        rnd.setSeed(seed);
        return new BCryptPasswordEncoder(10, rnd);
    }
}
