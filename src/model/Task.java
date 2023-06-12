package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class Task {

    protected int id;
    protected String taskName;
    protected String taskDescription;
    protected Status status;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = Status.NEW;
    }

    public Task(String taskName, String taskDescription, Status status, int duration, String startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
        this.startTime =  LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(
                "HH:mm - dd.MM.yyyy"));
    }

    public Task(int id, String taskName, String taskDescription, Status status, int duration, LocalDateTime startTime) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, String taskDescription, Status status, int duration, String startTime) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
        this.startTime =  LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(
                "HH:mm - dd.MM.yyyy"));
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm", Locale.getDefault()));
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription) && status == task.status
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskName, taskDescription, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status=" + status + '\'' +
                ", duration=" + duration + '\'' +
                ", startTime=" + startTime + '\'' +
                ", endTime=" + getEndTime() +
                '}';
    }
}

