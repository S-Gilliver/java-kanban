package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void calculateStartTime() {
        LocalDateTime epicStart = LocalDateTime.MAX;
        int nullCounter = 0;
        for (Subtask subTask : subtasks) {
            if (subTask.getStartTime() == null) {
                nullCounter++;
            } else if (subTask.getStartTime().isBefore(epicStart)) {
                epicStart = subTask.getStartTime();
            }
        }
        if (nullCounter == subtasks.size()) {
            epicStart = null;
        }
        setStartTime(epicStart);
    }

    public void calculateDuration() {
        int duration = 0;
        for (Subtask subTask : subtasks) {
            duration += subTask.getDuration();
        }
        setDuration(duration);
    }

    public void calculateEndTime() {
        LocalDateTime epicEnd = LocalDateTime.MIN;
        int nullCounter = 0;
        for (Subtask subTask : subtasks) {
            if (subTask.getEndTime() == null) {
                nullCounter++;
            } else if (subTask.getEndTime().isAfter(epicEnd)) {
                epicEnd = subTask.getEndTime();
            }
        }
        if (nullCounter == subtasks.size()) {
            epicEnd = null;
        }
        endTime = epicEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                ", duration'" + getDuration() + '\'' +
                ", startTime'" + getStartTime() + '\'' +
                ", endTime'" + getEndTime() + '\'' +
                '}';
    }

}
