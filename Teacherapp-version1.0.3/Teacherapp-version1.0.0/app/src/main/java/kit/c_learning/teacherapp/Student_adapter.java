package kit.c_learning.teacherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Student_adapter extends RecyclerView.Adapter<Student_adapter.MyViewHolder>{
    private Context mContext;
    List<String> student_name;
    static int est_length = 0;
    static int row;
    String stuName;

    public Student_adapter(){
        super();
        student_name = new ArrayList<String>();
    }

    public void addData(String name){
     //   ++Student_adapter.est_length;
        student_name.add(name);
        notifyDataSetChanged();
        Intent intent = new Intent("selected-Student");
        intent.putExtra("selected-student", student_name.size());
        LocalBroadcastManager .getInstance(mContext).sendBroadcast(intent);
      //  System.out.println("student name length add " + Student_adapter.est_length);
    }

    public void clearData(int position){
       // --Student_adapter.est_length;
        student_name.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
        Intent intent = new Intent("unSelected-Student");
        intent.putExtra("unselected-student", student_name.size());
        LocalBroadcastManager .getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list, parent, false);
        return new Student_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.studentName.setText(student_name.get(position));
        row = holder.getAdapterPosition();
        holder.cancelICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData(holder.getAdapterPosition());
                Intent intent = new Intent("removeItem");
                intent.putExtra("stuName", holder.studentName.getText().toString());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
        System.out.println("Selected student list" + holder.studentName.getText().toString() + "\t" + Choice_Stu_add.stID[row]);
    }

    @Override
    public int getItemCount() {
        return student_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        ImageView circleImage, cancelICon;
        int index = getAdapterPosition();
        public MyViewHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.student_name);
            circleImage = (ImageView) itemView.findViewById(R.id.item_img);
            cancelICon = (ImageView) itemView.findViewById(R.id.cancel);
        }
    }
}
