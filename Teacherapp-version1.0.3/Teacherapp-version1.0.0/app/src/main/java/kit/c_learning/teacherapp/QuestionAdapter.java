package kit.c_learning.teacherapp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kit.c_learning.teacherapp.models.Question;

/**
 * Created by sokrim on 2/8/2018.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder>{

    private Context mContext;
    private List<Question> questionList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Question question = questionList.get(position);
        final int pos = position;
        holder.displayPublic.setText(question.getDisplayPublic());
        holder.title_quickQuestion.setText(question.getTitle_quickQuestion());
        holder.registeredDate.setText(question.getRegisteredDate());
        holder.Guest.setText(question.getGuest());
        holder.openTotalResult.setText(question.getOpenTotalResult());
        holder.Submission.setText(question.getSubmission()+ "");
        holder.title_quickQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("+++++++++++" + pos);
                System.out.println("+++++++++++" + Questionnaires.questionType.length);
                for (int i = 0; i< Questionnaires.questionType.length; i++)
                    Log.e("============= ", Questionnaires.questionType[i]);
                System.out.println("*********** " + Questionnaires.questionType[pos]);

                //Pie Chart No comment
                if (Questionnaires.questionType[pos].equals("22") || Questionnaires.questionType[pos].equals("24")
                        || Questionnaires.questionType[pos].equals("20")){
                   // System.out.println("--------------- 22 --------------------");
                    Intent intent = new Intent(mContext, Pie_Chart.class);
                    intent.putExtra("id", Questionnaires.questionID[pos]);
                    intent.putExtra("comment", Questionnaires.questionComment[pos]);


                    if (Questionnaires.questionType[pos].equals("22")){
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("24")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);

                    }
                    else if (Questionnaires.questionType[pos].equals("20")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }

                    mContext.startActivity(intent);
                }
                //Pie Chart comment
                else if (Questionnaires.questionType[pos].equals("23") || Questionnaires.questionType[pos].equals("25")
                        || Questionnaires.questionType[pos].equals("21")){
                    // System.out.println("--------------- 22 --------------------");
                    Intent intent = new Intent(mContext, Pie_Chart.class);
                    intent.putExtra("id", Questionnaires.questionID[pos]);
                    intent.putExtra("comment", Questionnaires.questionComment[pos]);


                    if (Questionnaires.questionType[pos].equals("23")){
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("25")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("21")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    mContext.startActivity(intent);
                }
                //Choices ThreadComment Bar chart
                else if (Questionnaires.questionType[pos].equals("30") || Questionnaires.questionType[pos].equals("40")
                        || Questionnaires.questionType[pos].equals("50")){
                   // System.out.println("--------------- 31 --------------------");
                    Intent intent = new Intent(mContext, Bar_Chart.class);
                    intent.putExtra("id", Questionnaires.questionID[pos]);
                    intent.putExtra("comment", Questionnaires.questionComment[pos]);

                    if (Questionnaires.questionType[pos].equals("40")){
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                     else if (Questionnaires.questionType[pos].equals("50")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("30")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    mContext.startActivity(intent);
                }

                //Choices No ThreadComment  Bar chart
                else if (Questionnaires.questionType[pos].equals("31") || Questionnaires.questionType[pos].equals("41")
                        || Questionnaires.questionType[pos].equals("51")){
                    // System.out.println("--------------- 31 --------------------");
                    Intent intent = new Intent(mContext, Bar_Chart.class);
                    intent.putExtra("id", Questionnaires.questionID[pos]);
                    intent.putExtra("comment", Questionnaires.questionComment[pos]);

                    if (Questionnaires.questionType[pos].equals("41")){
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("51")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    else if (Questionnaires.questionType[pos].equals("31")) {
                        intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    }
                    mContext.startActivity(intent);
                    mContext.startActivity(intent);
                }
                else if (Questionnaires.questionType[pos].equals("1")){
                    // System.out.println("--------------- 31 --------------------");
                    Intent intent = new Intent(mContext, Comment_Only.class);
                    intent.putExtra("qbTitle", Questionnaires.qbTitle[pos]);
                    mContext.startActivity(intent);
                }

            }
        });

        /*holder.editQuestionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.editQuestionIcon);
            }
        });*/
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.layout);
            }
        });
    }

    /* Showing popup menu when tapping on 3 dots */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_editquestion, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit_questionnairesInfo:
                    Toast.makeText(mContext, "Edit Function Information", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.edit_question:
                    Toast.makeText(mContext, "Edit Function", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.copyQuestion:
                    Toast.makeText(mContext, "copy of the Function", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.downloadCSV:
                    Toast.makeText(mContext, "Download answer CSV", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.reset:
                    Toast.makeText(mContext, "Reset submission status", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.deleteQuestion:
                    Toast.makeText(mContext, "Delete Function", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView displayPublic, registeredDate, title_quickQuestion, Guest, openTotalResult , Submission;
        public ImageView previewIcon;
        public LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            displayPublic = view.findViewById(R.id.display_Public);
            registeredDate = view.findViewById(R.id.registeredDate);
            title_quickQuestion = view.findViewById(R.id.title_quickQuestion);
            Guest = view.findViewById(R.id.Guest);
            openTotalResult = view.findViewById(R.id.openTotalResult);
            Submission = view.findViewById(R.id.Submission);
            previewIcon = view.findViewById(R.id.previewIcon);
            layout = view.findViewById(R.id.edit_questIcon);
           /* layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(layout);
                }
            });*/
        }
    }

    public QuestionAdapter(Context mContext, List<Question> questionList) {
        this.mContext = mContext;
        this.questionList = questionList;
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
