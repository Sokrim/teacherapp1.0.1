package kit.c_learning.teacherapp.models;



/**
 * Created by sokrim on 2/20/2018.
 */

public class BulletinBoard {

    private String bbName, lastPostDate, capacity, anonymous, applicable;
    private String userComment, wordCount;


    public BulletinBoard(String applicable,String bbName, String lastPostDate, String capacity, String anonymous, String userComment, String wordCount){
        this.applicable = applicable;
        this.bbName = bbName;
        this.lastPostDate = lastPostDate;
        this.capacity = capacity;
        this.anonymous = anonymous;
        this.userComment = userComment;
        this.wordCount = wordCount;
    }


    public String getBbName(){
        return bbName;
    }
    public  void setBbName(String bbName){
        this.bbName = bbName;
    }

    public String getLastPostDate(){
        return lastPostDate;
    }
    public  void setLastPostDate(String lastPostDate){
        this.lastPostDate = lastPostDate;
    }

    public String getCapacity(){
        return capacity;
    }
    public  void setCapacity(String capacity){
        this.capacity = capacity;
    }

    public String getAnonymous(){
        return anonymous;
    }
    public  void setAnonymous(String anonymous){
        this.anonymous = anonymous;
    }

    public String getUserComment(){
        return userComment;
    }
    public  void setUserComment(String userComment){
        this.userComment = userComment;
    }

    public String getWordCount(){
        return wordCount;
    }
    public  void setWordCount(String wordCount){
        this.wordCount = wordCount;
    }

    public String getApplicable() {
        return applicable;
    }

    public void setApplicable(String applicable) {
        this.applicable = applicable;
    }
}
