## Что внутри проекта
- Java 17
- Maven
- JUnit 5
- Testcontainers
- PostgreSQL
- Flyway
- JDBC без Spring

## Идея проекта
Есть небольшой backend-модуль регистрации пользователей.

Компоненты:
- `UserRepository` — работает с таблицей `users`
- `NotificationEventRepository` — пишет событие в `notification_events`
- `UserRegistrationService` — регистрирует пользователя и создает событие
- Flyway-миграции создают схему БД

## Что уже реализовано
Написаны тесты в классах:
- `UserRepositoryIntegrationTest`
- `UserRegistrationServiceIntegrationTest`
- `TransactionIsolationIntegrationTest`

## Как запускать
### Требования
- Java 17+
- Maven 3.9+
- Docker

### Команда
```bash
mvn test
```

## Домашнее задание
Нужно реализовать интеграционные тесты для проекта.
Формат сдачи:
- реализованные тесты в `HomeworkTasksTest`
- короткий текстовый ответ в LMS:
  - какие риски ловит интеграционный тест, которые не ловит unit
  - почему rollback удобен, но не всесилен
  - в каких случаях нужен не rollback, а другой способ очистки данных
- в качестве артефакта должен быть прикреплен архив проекта в LMS

## Домашнее задание 2
1. Дописать все тесты (API и WEB) на проект
2. Разметить тесты тегами
   a. тесты на самую важную логику помечаем как @Tag("smoke")
   b. более широкие как @Tag("regression")
   c. тяжелые end-to-end как @Tag("e2e")
3. Не забыть отредактировать pom.xml и дополнить workflow раннера по условиям
   выше.