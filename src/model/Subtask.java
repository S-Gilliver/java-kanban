package model;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int epicId, String taskName, String taskDescription, Status status) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public Subtask(int epicId, String taskName, String taskDescription, Status status,
                   Integer duration, String startTime) {
        super(taskName, taskDescription, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + getDuration() + '\'' +
                '}';
    }
}
