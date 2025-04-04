package ru.skirda;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestContainersConfig {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    protected static EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    @BeforeAll
    void setup() {
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USER", postgres.getUsername());
        System.setProperty("DB_PASS", postgres.getPassword());



        entityManagerFactory = Persistence.createEntityManagerFactory("lab2-persistence-unit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void cleanup() {
        entityManager.clear();
    }

    @AfterAll
    void tearDown() {
        entityManager.close();
        entityManagerFactory.close();
    }
}