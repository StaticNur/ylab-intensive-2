# Training Diary (homework - 4)
 Приложение для ведения дневника тренировок, которое позволит пользователям записывать свои тренировки, просматривать их и анализировать свой прогресс в тренировках.

## Инструкция сборки и запуск проекта
1) Загружаем проект к себе
2) Вводим команду ``` docker-compose up ``` для поднятия контейнера с бд
3) Собрать приложение: ``` mvn clean package ```
4) Запустить через контейнер сервлетов Tomcat 9.0.85 версии (можно использовать плагин Smart Tomcat)

## Инструкция запуска тестов
1) Заходим в ``` src/test/java/com/ylab/intensive/TestLauncher.java ``` и запускаем тесты в классе TestLauncher через зеленную кнопку.

## Endpoints (swagger)

#### GET http://localhost:8080/swagger-ui/index.html

При вставке токена не забудьте указать Bearer пример: ``` Bearer YOUR_TOKEN ```

## Технологии
- Java 17
- Java EE
- Spring Framework 5
- Spring Security
- Spring AOP
- Spring-jdbc
- Swagger + Swagger UI
- Tomcat 9
- База данных PostgreSQL
- Миграция данных через Liquibase
- JWT
- Lombok
- log4j
- jackson
- Mapstruct
- hibernate-validator
- assertj
- JUnit 5
- Testcontainers
- Aspectj
- Docker
- Maven


## Структура базы данных
(Пароли хранятся в зашифрованном виде)

![img.png](img.png)

### Training Diary. Домашнее задание 'Знакомство с Spring Framework'
### Необходимо обновить сервис согласно следующим требованиям и ограничениям.
## Требования:

- Java-конфигурация приложения
- Кастомные конфигурационные файлы заменить на application.yml
- Удалить сервлеты, реализовать Rest-контроллеры (Spring MVC)
- Swagger + Swagger UI
- Аспекты переписать на Spring AOP
- Внедрение зависимостей ТОЛЬКО через конструктор
- Удалить всю логику создания сервисов, репозиториев и тд. Приложение должно полностью управляться спрингом
- Добавить тесты на контроллеры (WebMVC)