package kit.c_learning.teacherapp.models;

/**
 * Created by sokrim on 2/22/2018.
 */

public class Course_List {

    private String title_Course, course_Code, semester_Period;
    private int numOfStudent;

    public Course_List(String name, String code, String period, int numStudent){
        this.title_Course = name;
        this.course_Code = code;
        this.semester_Period = period;
        this.numOfStudent = numStudent;
    }

    public String getTitle_Course() {
        return title_Course;
    }

    public String getCourse_Code() {
        return course_Code;
    }

    public String getSemester_Period() {
        return semester_Period;
    }

    public void setTitle_Course(String title_Course) {
        this.title_Course = title_Course;
    }

    public void setCourse_Code(String course_Code) {
        this.course_Code = course_Code;
    }

    public void setSemester_Period(String semester_Period) {
        this.semester_Period = semester_Period;
    }

    public void setNumOfStudent(int numOfStudent) {
        this.numOfStudent = numOfStudent;
    }

    public int getNumOfStudent() {
        return numOfStudent;
    }

}
