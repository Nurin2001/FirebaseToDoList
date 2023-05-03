package com.assignment.todolistrecycler;

public class Task {

    private String task, startDate, idTask, imgUrl;

    public Task() {
    }

    public Task(String task, String startDate) {
        this.task = task;
        this.startDate = startDate;
    }

    public Task(String task, String startDate, String idTask) {
        this.task = task;
        this.startDate = startDate;
        this.idTask = idTask;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getIdTask() {
        return idTask;
    }
//
//    public void setIdTask(String idTask) {
//        this.idTask = idTask;
//    }
}
