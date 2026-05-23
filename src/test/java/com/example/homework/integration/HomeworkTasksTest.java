package com.example.homework.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HomeworkTasksTest extends AbstractPostgresIntegrationTest {

    @Test
    @Disabled("Домашнее задание: снять @Disabled и реализовать тест")
    void shouldFindUserByEmailAfterSavingThroughRepository() throws Exception {
        // arrange
        // создать пользователя через userRepository.save(...)

        // act
        // найти пользователя по email

        // assert
        // проверить email, fullName и status
    }

    @Test
    @Disabled("Домашнее задание: снять @Disabled и реализовать тест")
    void shouldNotReturnInactiveUserInFindActiveUsers() throws Exception {
        // arrange
        // создать одного ACTIVE и одного INACTIVE пользователя

        // act
        // вызвать userRepository.findActiveUsers()

        // assert
        // проверить, что вернулся только ACTIVE пользователь
    }

    @Test
    @Disabled("Домашнее задание: снять @Disabled и реализовать тест")
    void shouldCreateNotificationEventOnRegistration() throws Exception {
        // arrange + act
        // вызвать userRegistrationService.register(...)

        // assert
        // проверить количество пользователей и notification_events
        // проверить тип события USER_REGISTERED
        // проверить, что payload содержит email
    }

    @Test
    @Disabled("Домашнее задание: снять @Disabled и реализовать тест")
    void shouldRollbackChangesBetweenTests() throws Exception {
        // Подсказка:
        // 1. в этом тесте создать пользователя
        // 2. убедиться, что count() == 1 внутри теста
        // 3. в отдельном тесте ниже проверить, что база снова пустая
    }

    @Test
    @Disabled("Домашнее задание: снять @Disabled и реализовать тест")
    void shouldStartFromCleanStateInAnotherTest() throws Exception {
        // Подсказка:
        // здесь нужно проверить, что база пуста в начале теста
        // и тем самым доказать, что rollback из AfterEach сработал
    }

    @Test
    @Disabled("Домашнее задание со звездочкой: показать ограничение транзакций")
    void shouldDemonstrateThatCommitInsideCodeBreaksRollbackIsolation() throws Exception {
        // Идея:
        // вызвать userRegistrationService.registerAndCommitInsideService(..., connection)
        // затем проверить, что rollback в @AfterEach уже не вернет базу в исходное состояние.
        // Для этого понадобится отдельная проверка через новое соединение.
    }
}
