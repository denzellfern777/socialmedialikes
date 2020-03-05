package com.app.socialmedialikes;

public class Data {

    String timestamp;
    String points;
    private String username;
    private String transaction;
    private String approval;

    public Data(String timestamp, String username, String points, String transaction, String approval) {
        this.timestamp = timestamp;
        this.username = username;
        this.points = points;
        this.transaction = transaction;
        this.approval = approval;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }


    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }


}
