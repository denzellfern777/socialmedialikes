package com.app.socialmedialikes;

public class Task {

    private String taskName;
    private String taskDesc;
    private String taskReward;
    private String taskLink;
    private String taskId;

    public Task(String taskName, String taskDesc, String taskReward, String taskLink, String taskId) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskReward = taskReward;
        this.taskLink = taskLink;
        this.taskId = taskId;
    }

    public String getTaskLink() {
        return taskLink;
    }

    public void setTaskLink(String taskLink) {
        this.taskLink = taskLink;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(String taskReward) {
        this.taskReward = taskReward;
    }
}
