package kit.c_learning.teacherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Create_BulletinBoard extends AppCompatActivity implements View.OnClickListener {

    public static String board;
    Toolbar creationToolbar;
    EditText createBulletinBoard;
    Button createButton;

    String articleSubmission = "1";
    String applicableStudent = "2";
    String anonymous = "2";

    TextView requiredMsg;
    RadioGroup radioGroup1, radioGroup2,radioGroup3 ;
    RadioButton radioFailed,radioFair,radioAnonymous,radioOnlyInstructor,radioRegistered,radioNone,radioChoice,radioAll;

    String mes;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__bulletin_board);
        creationToolbar = (Toolbar) findViewById(R.id.creationToolbar);
        setSupportActionBar(creationToolbar);
        getSupportActionBar().setTitle("New Creation of Bulletin Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        requiredMsg = (TextView) findViewById(R.id.requiredMsg);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);

        radioGroup1.setOnCheckedChangeListener(listener1);
        radioGroup2.setOnCheckedChangeListener(listener2);
        radioGroup3.setOnCheckedChangeListener(listener3);

        radioFailed = (RadioButton) findViewById(R.id.radioButton1);
        radioFair = (RadioButton) findViewById(R.id.radioButton2);
        radioAnonymous = (RadioButton) findViewById(R.id.radioButton3);
        radioOnlyInstructor = (RadioButton) findViewById(R.id.radioButton4);
        radioRegistered = (RadioButton) findViewById(R.id.radioButton5);
        radioNone = (RadioButton) findViewById(R.id.radioButton6);
        radioChoice = (RadioButton) findViewById(R.id.radioButton7);
        radioAll = (RadioButton) findViewById(R.id.radioButton8);


        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(Create_BulletinBoard.this);

        createBulletinBoard = (EditText) findViewById(R.id.createBulletinBoard_edt);
        createBulletinBoard.setText("Bulletin Board for all participants");
        createBulletinBoard.setSelection(createBulletinBoard.getText().length());
        createBulletinBoard.setFocusable(false);
        createBulletinBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                createBulletinBoard.setFocusableInTouchMode(true);
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("bbName")){
            getSupportActionBar().setTitle("");
            createBulletinBoard.setText(intent.getExtras().getString("bbName"));
            createButton.setText("Update");
            if(intent.getExtras().getString("bbStuWrite").equals("0")){
                radioFailed.setChecked(true);
            }else if(intent.getExtras().getString("bbStuWrite").equals("1")) {
                radioFair.setChecked(true);
            }
            if (intent.getExtras().getString("bbAnonymous").equals("0")){
                radioAnonymous.setChecked(true);
            }else if(intent.getExtras().getString("bbAnonymous").equals("1")){
                radioOnlyInstructor.setChecked(true);

            }else if(intent.getExtras().getString("bbAnonymous").equals("2")){
                radioRegistered.setChecked(true);
            }

            if (intent.getExtras().getString("bbStuRange").equals("0")){
                radioNone.setChecked(true);
            }else if(intent.getExtras().getString("bbStuRange").equals("1")){
                radioChoice.setChecked(true);

            }else if(intent.getExtras().getString("bbStuRange").equals("2")){
                radioAll.setChecked(true);
            }
            System.out.println("|||||||||||||||||||||||||||||||||||||" + intent.getExtras().getString("bbID"));
        }

    }


     private RadioGroup.OnCheckedChangeListener listener1 = (group, checkedId) -> {
         switch (checkedId){
             case R.id.radioButton1:
                 articleSubmission = "0"; //Failed
             //    Toast.makeText(getApplication(),"Failed", Toast.LENGTH_LONG).show();
                 break;
             case R.id.radioButton2:
                 articleSubmission = "1"; //fair
            //     Toast.makeText(getApplication(),"Fair", Toast.LENGTH_LONG).show();
                 break;
         }
     };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radioButton3:
                    anonymous = "0"; //anonymous
                //    Toast.makeText(getApplication(),"Anonymous", Toast.LENGTH_LONG).show();
                    break;
                case R.id.radioButton4:
                    anonymous = "1"; //Only instructor
                //    Toast.makeText(getApplication(),"Failed", Toast.LENGTH_LONG).show();
                    break;
                case R.id.radioButton5:
                     anonymous = "2"; //Registered
                //    Toast.makeText(getApplication(),"Failed", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radioButton6:
                    applicableStudent = "0"; //None
              //      Toast.makeText(getApplication(),"None", Toast.LENGTH_LONG).show();
                    break;
                case R.id.radioButton7:
                    applicableStudent = "1"; //Choice
                 //   Toast.makeText(getApplication(),"Choice", Toast.LENGTH_LONG).show();
                    break;
                case R.id.radioButton8:
                    applicableStudent = "2"; //All
               //     Toast.makeText(getApplication(),"All", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void editBulletinBoardAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("cc_name", board);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + board);
        data.put("cc_stuwrite", articleSubmission);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + articleSubmission);
        data.put("cc_anonymous", anonymous);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + anonymous);
        data.put("cc_sturange", applicableStudent );
        System.out.println("|||||||||||||||||||||||||||||||||||||" + applicableStudent);
        data.put("ccID",getIntent().getExtras().getString("bbID"));

        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);

        try {
            String myUrl = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/cateedit", "POST").get();
            JSONObject jsonObject = new JSONObject(myUrl);
            System.out.println("|||||||||||||||||||||||||||||||||||||" + myUrl);
           // JSONObject mes = jsonObject.getJSONObject("mes");

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    //Insert bulletin Board
    private void createBulletinBoard(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("ctID", "c398223976");
        data.put("cc_name", board);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + board);
        data.put("cc_stuwrite", articleSubmission);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + articleSubmission);
        data.put("cc_anonymous", anonymous);
        System.out.println("|||||||||||||||||||||||||||||||||||||" + anonymous);
        data.put("cc_sturange", applicableStudent );

        System.out.println("|||||||||||||||||||||||||||||||||||||" + applicableStudent);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);

        try {
            String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/cateCreate", "POST").get();
            JSONObject jsonObject = new JSONObject(myUrl);
            System.out.println("|||||||||||||||||||||||||||||||||||||" + myUrl);
            mes = jsonObject.getString("mes");
            System.out.println(":::::::::::::::::::::::::::::::::::::" + mes);

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        board = createBulletinBoard.getText().toString();

        if (createButton.getText().toString().equals("Create")){
            if (board.equals("")){
                requiredMsg.setVisibility(View.VISIBLE);
            }
            else {
                createBulletinBoard();
           /* if (mes.equals("success")){*/
                Intent intent = new Intent(Create_BulletinBoard.this, Bulletin_Board_List.class);
               /* prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefEditor = prefs.edit();*/
                intent.putExtra("Name", board);
                intent.putExtra("Article",articleSubmission);
                intent.putExtra("Anonymous",anonymous);
                intent.putExtra("Applicable",applicableStudent);
                /*prefEditor.apply();*/
                startActivity(intent);
         /*   }
            else {
                Toast.makeText(getApplication(),"Error, can't create_thread bulletin Board", Toast.LENGTH_LONG).show();
            }*/

            }
        } else {
            if (board.equals("")){
                requiredMsg.setVisibility(View.VISIBLE);
            }
            else {
                editBulletinBoardAPI();
                Intent intent = new Intent(Create_BulletinBoard.this, Bulletin_Board_List.class);
                startActivity(intent);
            }
        }

    }
}
