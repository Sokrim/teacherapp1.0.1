package kit.c_learning.teacherapp.models;

/**
 * Created by sokrim on 3/30/2018.
 */

public class Student_Lists {
    String name, dept, stuID;
    boolean isSelected ;

    public Student_Lists(String name, String dept, String id, boolean select) {
        this.name = name;
        this.dept = dept;
        this.stuID = id;
        this.isSelected = select;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }
}
