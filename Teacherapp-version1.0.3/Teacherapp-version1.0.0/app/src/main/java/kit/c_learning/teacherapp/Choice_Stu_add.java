package kit.c_learning.teacherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.Student_Lists;

public class Choice_Stu_add extends AppCompatActivity {

    Toolbar addStuToolbar;
    RecyclerView recyclerView, studentList;
    List<Student_Lists> student_lists;
    StudentListsAdapter adapter;
    Student_adapter studentAdpater;
    JSONObject object;
    JSONArray arrayData;
    static String[] stName, stDept, stNO, stID;
    private ViewGroup mSelectedImagesContainer;
    TextView student_name, student_Count;
    View imageHolder;
    String[] name ;
    ImageView imageView;
    static boolean selected_state;

    int count;
    String stuID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_stu_add);
        addStuToolbar = (Toolbar) findViewById(R.id.addStuToolbar);
        setSupportActionBar(addStuToolbar);
        getSupportActionBar().setTitle("Choices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

        imageHolder = LayoutInflater.from(this).inflate(R.layout.student_name_layout, null);
        student_name = (TextView) imageHolder.findViewById(R.id.student_name);
        imageView = imageHolder.findViewById(R.id.item_img);
        student_Count = findViewById(R.id.okay_textView);
        student_Count.setOnClickListener(v -> {
            Intent intent = new Intent(Choice_Stu_add.this, Bulletin_Board_List.class);
            submitStu();
            startActivity(intent);
        });

        setUpRecyclerView();
        getStudentAPI();
        setUpBroadCastReceiver();

    }

    protected void setUpBroadCastReceiver(){
        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String name = intent.getStringExtra("stuName");
                stuID = new String();
                stuID = intent.getStringExtra("stuID");
                System.out.println("Student ID \t" + stuID);
                studentAdpater.addData(name);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("student-name"));

        BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                count = intent.getIntExtra("selected-student", 0);
                student_Count.setText("OK" + "(" + count + ")");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,
                new IntentFilter("selected-Student"));

        BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                count = intent.getIntExtra("unselected-student", 0);
                student_Count.setText("OK" + "(" + count + ")");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver3,
                new IntentFilter("unSelected-Student"));
    }

    protected void submitStu(){
        String[] ID = new String[]{"s761867826", "s566481447", "s407189013","s566481448" };
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("ct","c398223976");
        data.put("cc",getIntent().getExtras().getString("ccID"));
        for (int i= 0 ; i < ID.length; i++) {
          //  data.put("st", ID[i]);
            /*// Log.v("Student ID", data.put("st", ID[i]));
            System.out.println("Student ID \t" + ID[i]);*/
             data.put("st",stuID);
            //  System.out.println("Student ID \t" + stuID);
            HttpRequestAsync myHttp = new HttpRequestAsync(header, data);
            try {
                String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/CoopAdd", "POST").get();
                JSONObject jsonObject = new JSONObject(myURL);
                String jsonData = jsonObject.getString("res");
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

   /* public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selected_state = intent.getBooleanExtra("selected_state", true);
            System.out.println("____"+ selected_state);
            String name = intent.getStringExtra("stuName");
           // int position = intent.getIntExtra("pos", -1);
            String student_name = intent.getStringExtra("student_name");
           // int checkedPos = intent.getIntExtra("checked_pos", -1);
           *//* int count = intent.getIntExtra("selected_student", 0);
            student_Count.setText(String.valueOf(count));*//*
            studentAdpater.addData(name);
         *//* //  if (position != -1){
               // studentAdpater.clearData(position);
            }
//            mSelectedImagesContainer.removeAllViews();
//            student_name.setText(name);
//            mSelectedImagesContainer.addView(imageHolder);

          *//*  for (int i = 0; i < student_lists.size(); i++){

                System.out.println("---------" + student_lists.size());
            }
           // Toast.makeText(Choice_Stu_add.this,name,Toast.LENGTH_SHORT).show();
        }
    };
*/
    protected  void setUpRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        studentList = (RecyclerView) findViewById(R.id.student_list);

        student_lists = new ArrayList<>();
        adapter = new StudentListsAdapter(this,student_lists);
        studentAdpater = new Student_adapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerView.LayoutManager studentListLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(mLayoutManager);
        studentList.setLayoutManager(studentListLayout);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        studentList.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        studentList.setAdapter(studentAdpater);
    }

    protected void getStudentAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("ctID","c398223976");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/stuadd", "POST").get();
            object = new JSONObject(myURL);
            arrayData = object.getJSONArray("data");

            stID = new String[arrayData.length()];
            stName = new String[arrayData.length()];
            stDept = new String[arrayData.length()];
            stNO = new String[arrayData.length()];

            for (int i = 0; i < arrayData.length(); i++){
                JSONObject row = arrayData.getJSONObject(i);
                stID[i] = row.getString("stID");
                stName[i] = row.getString("stName");
                stDept[i] = row.getString("stClass");
                stNO[i] = row.getString("stNO");
                prepareStudent(stName[i],stDept[i],stNO[i]);
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    protected void prepareStudent(String name, String dept, String id){
        Student_Lists stuLists = new Student_Lists(name, dept, id, false);
        student_lists.add(stuLists);
        adapter.notifyDataSetChanged();
    }
}
