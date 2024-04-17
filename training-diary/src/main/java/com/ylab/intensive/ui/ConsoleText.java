package com.ylab.intensive.ui;

/**
 * ConsoleText Class.
 * This class provides constants for various console messages used in the user interface.
 * <p>
 * Constants available:
 * - WELCOME: A welcome message for the console application.
 * - AUTHORIZED_USER: A message that displays the currently authorized user.
 * - MENU_NOT_AUTH: The menu options for a non-authorized user.
 * - MENU_AUTH: The menu options for an authorized user.
 * - UNKNOWN_COMMAND: A message for an unknown command.
 * - GOODBYE: A farewell message when the application exits.
 */
public class ConsoleText {
    public static final String WELCOME = " Здравствуйте, добро пожаловать в консольное приложения! Следуйте указаниям из документации. ";
    public static final String AUTHORIZED_USER = " Я %s ";
    public static final String MENU_NOT_AUTH =
            " 1 - регистрация пользователя\n" +
            " 2 - авторизация пользователя\n" +
            " 12 - завершить приложения ";
    public static final String MENU_AUTH =
            " 3 - расширить перечень типов тренировок\n" +
            " 4 - добавить новую тренировку\n" +
            " 5 - добавить дополнительную информацию о тренировке\n" +
            " 6 - посмотреть свои предыдущие тренировки\n" +
            " 7 - редактировать тренировку\n" +
            " 8 - удалить тренировку\n" +
            " 9 - изменить права пользователя\n" +
            " 10 - получить статистику по тренировкам\n" +
            " 11 - просмотр Аудит действий\n" +
            " 12 - выход (разлогиниться)\n" +
            " 13 - посмотреть тренировки всех людей ";
    public static final String UNKNOWN_COMMAND = "Неизвестная команда, попробуйте еще раз.";
    public static final String GOODBYE = "Приложение успешно завершилось. \n" +
                                         "Благодарим вас за использование нашего консольного приложения. \n" +
                                         "Если у вас есть какие-либо вопросы, не стесняйтесь обращаться к нам https://t.me/orifov_nur. \n" +
                                         "Удачи!";
}
