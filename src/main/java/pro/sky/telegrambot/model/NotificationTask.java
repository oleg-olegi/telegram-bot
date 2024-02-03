package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class NotificationTask {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "chat_id")
    private int chat_id;
    @Column(name = "notification_task")
    private String notification_task;
    @Column(name = "scheduled_time")
    private LocalDateTime date;

    public NotificationTask() {
    }

    public NotificationTask(int chat_id, String notification_task, LocalDateTime date) {
        this.chat_id = chat_id;
        this.notification_task = notification_task;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getNotification_task() {
        return notification_task;
    }

    public void setNotification_task(String notification_task) {
        this.notification_task = notification_task;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id
                && chat_id == that.chat_id
                && Objects.equals(notification_task, that.notification_task)
                && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat_id, notification_task, date);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chat_id=" + chat_id +
                ", notification_task='" + notification_task + '\'' +
                ", date=" + date +
                '}';
    }
}
