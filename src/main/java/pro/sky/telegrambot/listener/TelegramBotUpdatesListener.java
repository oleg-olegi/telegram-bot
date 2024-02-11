package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.BotService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private BotService botService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            // Process incoming messages
            // return the id of the last processed message or mark all as processed
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if (message == null) {
                logger.error("The provided message is empty " + update);
                return;
            }
            Long chatId = update.message().chat().id();
            if ("/start".equals(message.text())) {
                sendWelcomeMessage(chatId);
                return;
            }
            if ("/info".equals(message.text())) {
                String infoMessage = "Для создания напоминания введите сообщение в формате \"ДД.ММ.ГГГГ ЧЧ:ММ Сообщение\"";
                telegramBot.execute(new SendMessage(chatId, infoMessage));
                logger.info("Sending info message to chat {}: {}", chatId, infoMessage);
                return;
            }
            if (botService.addTask(message.text(), chatId)) {
                String successMessage = "Задание успешно создано";
                telegramBot.execute(new SendMessage(chatId, successMessage));
                logger.info("Sending info message to chat {}: {}", chatId, successMessage);
            } else {
                String unsuccessfullyMessage = "Возможно есть ошибки в формате сообщения";
                telegramBot.execute(new SendMessage(chatId, unsuccessfullyMessage));
                logger.info("Sending info message to chat {}: {}", chatId, unsuccessfullyMessage);
            }
            // Process your updates here
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendWelcomeMessage(long chatId) {
        //приветственное сообщение
        String welcomeMessage = "Добро пожаловать! Я ваш бот. Рад видеть вас!"
                + '\n' + "Для получения информации ведите /info";
        //отправка сообщения в чат
        telegramBot.execute(new SendMessage(chatId, welcomeMessage));
        logger.info("Sending welcome message to chat {}: {}", chatId, welcomeMessage);
    }

    @Scheduled(cron = "0/60 * * * * *")
    public void doScheduling() {
        logger.info("Выполнился метод исполнения по расписанию");
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        botService.getNotifications(localDateTime).forEach(notificationTask -> {
            long chatId = notificationTask.getChat_id();
            String message = notificationTask.getNotification_task();
            logger.info("Готовлюсь отправить напоминание");
            telegramBot.execute(new SendMessage(chatId, message));
            logger.info("Напоминание отправлено");
        });
    }
}
