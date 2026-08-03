package com.dat.ecommerce;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FlywayCheck implements CommandLineRunner {

    private final Flyway flyway;

    public FlywayCheck(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(String... args) {

        System.out.println("========== FLYWAY CHECK ==========");
        System.out.println("Current version: "
                + flyway.info().current());

        System.out.println("Pending migrations:");
        System.out.println(
                flyway.info().pending().length
        );

        System.out.println("=================================");
    }
}