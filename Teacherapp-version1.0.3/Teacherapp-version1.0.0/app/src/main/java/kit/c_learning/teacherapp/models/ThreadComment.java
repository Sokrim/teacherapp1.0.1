package kit.c_learning.teacherapp.models;

/**
 * Created by sokrim on 3/16/2018.
 */

public class ThreadComment {
    public enum ItemType {
        ONE_ITEM, TWO_ITEM;
    }

    String CommentUserName, UserComment, commentTime;
    private ItemType type;

    public ThreadComment(String userName, String comment, String time, ItemType type) {
        this.CommentUserName = userName;
        this.UserComment = comment;
        this.commentTime = time;
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getCommentUserName() {
        return CommentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        CommentUserName = commentUserName;
    }

    public String getUserComment() {
        return UserComment;
    }

    public void setUserComment(String userComment) {
        UserComment = userComment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
