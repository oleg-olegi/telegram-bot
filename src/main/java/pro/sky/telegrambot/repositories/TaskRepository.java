package pro.sky.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTask;

public interface TaskRepository extends JpaRepository<NotificationTask, Integer> {
}
