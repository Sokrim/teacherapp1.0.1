package kit.c_learning.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kit.c_learning.teacherapp.models.Course_List;

/**
 * Created by sokrim on 2/22/2018.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context mContext;
    private List<Course_List> courserList;
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.MyViewHolder holder, int position) {
        final Course_List course = courserList.get(position);
        final int pos = position;
        holder.course_Name.setText(course.getTitle_Course());
        holder.course_Code.setText(course.getCourse_Code());
        holder.semester_Period.setText(course.getSemester_Period());
        holder.numOf_Student.setText(course.getNumOfStudent()+"");

        holder.course_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Function.class);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return courserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView course_Name, course_Code, semester_Period, numOf_Student, assistant;

        public MyViewHolder(View view) {
            super(view);
            course_Name = view.findViewById(R.id.title_Course);
            course_Code = view.findViewById(R.id.course_Code);
            semester_Period = view.findViewById(R.id.semester_Period);
            numOf_Student = view.findViewById(R.id.numberOfStudent);
            assistant = view.findViewById(R.id.assistant);

        }
    }

    public CourseAdapter(Context mContext, List<Course_List> courseList) {
        this.mContext = mContext;
        this.courserList = courseList;
    }
}
