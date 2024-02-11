package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotService {
    private final static Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private final TaskRepository repository;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    public BotService(TaskRepository repository) {
        this.repository = repository;
    }

    public boolean addTask(String incomingMessage, Long chat_id) {
        Matcher matcher = PATTERN.matcher(incomingMessage);
        if (matcher.find()) {
            String date = matcher.group(1);
            String task = matcher.group(3);
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                NotificationTask notificationTask = new NotificationTask(chat_id, task, localDateTime);
                repository.save(notificationTask);
                logger.info("Task added successfully: {}", notificationTask);
                return true;
            } catch (DateTimeParseException e) {
                logger.error("Error parsing date and time: {}", e.getMessage());
                return false;
            }
        } else {
            logger.warn("No match found for incoming message: {}", incomingMessage);
            return false;
        }
    }
}
