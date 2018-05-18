package kit.c_learning.teacherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import kit.c_learning.teacherapp.models.Student_Lists;

/**
 * Created by sokrim on 3/30/2018.
 */

public class StudentListsAdapter extends RecyclerView.Adapter<StudentListsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Student_Lists> student_lists;
    Student_adapter studentAdapter;
    String name, stuName;

    public StudentListsAdapter(Context mContext, List<Student_Lists> student_lists) {
        this.mContext = mContext;
        this.student_lists = student_lists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_stuadd, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       public TextView stu_Name, stu_Dept, stu_ID;
        TextView semiColon;
        CheckBox mCheckBox;
        public MyViewHolder(View view) {
            super(view);
            stu_Name = view.findViewById(R.id.student_Name);
            stu_Dept = view.findViewById(R.id.student_Dept);
            stu_ID = view.findViewById(R.id.student_ID);
            semiColon = view.findViewById(R.id.semiColon);
            mCheckBox = view.findViewById(R.id.checkbox);

            BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    stuName = intent.getStringExtra("stuName");
                    if (stuName.equals(stu_Name.getText().toString())){
                        mCheckBox.setChecked(false);
                    }
                }
            };
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver,
                    new IntentFilter("removeItem"));
        }
    }

        @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Student_Lists student = student_lists.get(position);
        final int pos = position;
        holder.stu_Name.setText(student.getName());
        holder.stu_Dept.setText(student.getDept());
        holder.stu_ID.setText(student.getStuID());

        if (Choice_Stu_add.stDept[pos].equals("null") || Choice_Stu_add.stDept[pos].equals("")){
            holder.stu_Dept.setText("");
            holder.semiColon.setVisibility(View.GONE);
        } else if(Choice_Stu_add.stNO[pos].equals("null") || Choice_Stu_add.stDept[pos].equals("")){
            holder.stu_ID.setText("");
            holder.semiColon.setVisibility(View.GONE);
        } else if(Choice_Stu_add.stDept[pos].equals("null") || Choice_Stu_add.stNO[pos].equals("null")){
            holder.stu_Dept.setText("");
            holder.stu_ID.setText("");
            holder.semiColon.setVisibility(View.GONE);
        } else if (Choice_Stu_add.stNO[pos].equals("null") || Choice_Stu_add.stNO[pos].equals("")
                && !Choice_Stu_add.stDept[pos].equals("") && !Choice_Stu_add.stDept[pos].equals("null")){
            holder.stu_ID.setText("");
            holder.semiColon.setVisibility(View.GONE);
        }
        holder.mCheckBox.setChecked(student.isSelected());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.mCheckBox.setEnabled(false);
                    name = holder.stu_Name.getText().toString();
                    Intent intent = new Intent("student-name");
                    intent.putExtra("stuName", name);
                    intent.putExtra("stuID", Choice_Stu_add.stID[pos]);
                   /* intent.putExtra("checked_pos", pos);*/
                    LocalBroadcastManager .getInstance(mContext).sendBroadcast(intent);
                }
                else {
                    holder.mCheckBox.setEnabled(true);
                    name = holder.stu_Name.getText().toString();
                    Intent intent = new Intent("unselected_Stu");
                    intent.putExtra("unChecked_Stu", holder.stu_Name.getText().toString());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return student_lists.size();
    }
}
