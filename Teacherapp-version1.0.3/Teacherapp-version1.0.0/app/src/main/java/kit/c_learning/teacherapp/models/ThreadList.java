package kit.c_learning.teacherapp.models;

/**
 * Created by sokrim on 3/9/2018.
 */

public class ThreadList {
    String userName;
    String threadTitle;
    String timeCreated;
    String numComments;
    String numReads;
    String threadBody;

    public ThreadList(String userName, String threadTitle, String timeCreated, String numComments, String numReads, String threadBody) {
        this.userName = userName;
        this.threadTitle = threadTitle;
        this.timeCreated = timeCreated;
        this.numComments = numComments;
        this.numReads = numReads;
        this.threadBody = threadBody;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getNumComments() {
        return numComments;
    }

    public void setNumComments(String numComments) {
        this.numComments = numComments;
    }

    public String getNumReads() {
        return numReads;
    }

    public void setNumReads(String numReads) {
        this.numReads = numReads;
    }

    public String getThreadBody() {
        return threadBody;
    }

    public void setThreadBody(String threadBody) {
        this.threadBody = threadBody;
    }
}
