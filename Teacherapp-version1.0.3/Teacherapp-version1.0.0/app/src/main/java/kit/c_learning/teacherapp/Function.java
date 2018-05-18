package kit.c_learning.teacherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by sokrim on 2/2/2018.
 */

public class Function extends AppCompatActivity implements View.OnClickListener{

    Toolbar myCourseToolbar;
    private CardView questionCard, quizCard,attendance, bulletinBoardCard,messageCard;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function);

        myCourseToolbar = (Toolbar) findViewById(R.id.myCourse_toolbar);
        setSupportActionBar(myCourseToolbar);
        getSupportActionBar().setTitle("Java2");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionCard = (CardView) findViewById(R.id.question_card);
        questionCard.setOnClickListener(this);

        quizCard = (CardView) findViewById(R.id.quiz_card);
        quizCard.setOnClickListener(this);

        bulletinBoardCard = (CardView) findViewById(R.id.bulletinBoard_card);
        bulletinBoardCard.setOnClickListener(this);


        attendance = (CardView) findViewById(R.id.attendanceTracking);
        attendance.setOnClickListener(this);

        messageCard = findViewById(R.id.message_card);
        messageCard.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.question_card : intent = new Intent(this, Questionnaires.class);
                startActivity(intent);
                break;
            case R.id.quiz_card : intent = new Intent(this, Create_BulletinBoard.class);
                startActivity(intent);
                break;

            case R.id.bulletinBoard_card : intent = new Intent(this,Bulletin_Board_List.class);
                startActivity(intent);
                break;

            case R.id.attendanceTracking : /*intent = new Intent(this,Choice.class);
                startActivity(intent);*/
                break;
            case R.id.message_card : intent = new Intent(this,PostActivity.class);
                startActivity(intent);
                break;

            default:break;

        }
    }

}
