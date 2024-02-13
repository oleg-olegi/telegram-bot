package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.service.BotService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class NotificationScheduler {
    @Autowired
    private BotService botService;
    @Autowired
    private TelegramBot telegramBot;

    private Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

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
