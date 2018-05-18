package kit.c_learning.teacherapp.models;

/**
 * Created by sokrim on 2/8/2018.
 */

public class Question {
    private String displayPublic, title_quickQuestion,registeredDate, openTotalResult, Guest;
    private int submission;


    public Question(String displayPublic, String title_quickQuestion, String registeredDate, String openTotalResult, String Guest, int submission){
        this.displayPublic = displayPublic;
        this.title_quickQuestion = title_quickQuestion;
        this.registeredDate = registeredDate;
        this.openTotalResult = openTotalResult;
        this.Guest = Guest;
        this.submission = submission;
    }

    public String getDisplayPublic(){
        return displayPublic;
    }
    public  void setDisplayPublic(String displayPublic){
        this.displayPublic = displayPublic;
    }

    public String getTitle_quickQuestion(){
        return title_quickQuestion;
    }
    public void setTitle_quickQuestion(String title_quickQuestion){
        this.title_quickQuestion= title_quickQuestion;
    }
    public String getRegisteredDate(){
        return registeredDate;
    }
    public void setRegisteredDate(String registeredDate){
        this.registeredDate= registeredDate;
    }

    public String getOpenTotalResult(){
        return  openTotalResult;
    }
    public void setOpenTotalResult(String openTotalResult){
        this.openTotalResult = openTotalResult;
    }

    public String getGuest(){
        return  Guest;
    }
    public void setGuest(String Guest){
        this.Guest = Guest;
    }

    public int getSubmission(){
        return  submission;
    }
    public void setSubmission(int submission){
        this.submission = submission;
    }

}
