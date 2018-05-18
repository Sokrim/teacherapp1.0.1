package kit.c_learning.teacherapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.BulletinBoard;

public class Bulletin_Board_List extends AppCompatActivity {

    Toolbar bulletinBoardToolbar;
    private RecyclerView recyclerView;
    private BulletinBoardAdapter adapter;
    private BulletinBoardAdapter_test mAdapter;
    private List<BulletinBoard> bulletinBoardList;

    EditText bbNameeditText;
    FloatingActionButton plusButton;

    TextView requiredMsg;

    String bbName1,bbapplicable1,bbarticle1,bbanonymous1; //dialog
   // static String ccName,applicable,article,anonymous;
    public static String[] ccID = null,ccBBName = null,ccStuWrite = null,ccAnonymous = null,ccStuRange = null,ccLastDate = null,
            ccItemNum=null,ccCharNum = null, ccTotalSize = null, ccStuNum= null;
    static JSONObject jsonObject = null;

    String articleSubmission = "1";
    String applicableStudent = "2";
    String anonymous1 = "2";
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin__board__list);
        bulletinBoardToolbar = (Toolbar) findViewById(R.id.bulletinBoard_toolbar);
        setSupportActionBar(bulletinBoardToolbar);
        getSupportActionBar().setTitle("Bulletin Board List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plusButton = (FloatingActionButton) findViewById(R.id.plusFloatButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBBDialog();
            }
        });

        setupRecyclerView();
        requestBullentinBoard();
    }

    protected void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        bulletinBoardList = new ArrayList<>();
       // adapter = new BulletinBoardAdapter(this,bulletinBoardList);
        mAdapter = new BulletinBoardAdapter_test(this,bulletinBoardList);
        recyclerView.setAdapter(mAdapter);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Portrait Mode
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            // Landscape Mode
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            System.out.println("Landscape Mode");
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        BulletinBoardAdapter_test mAdapter1 = new BulletinBoardAdapter_test(this,bulletinBoardList);
        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((BulletinBoardAdapter_test) mAdapter1).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter1);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    static void executeAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ctID", "c398223976");
        data.put("caID", "id1");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myUrl = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/BBL", "POST").get();
            jsonObject = new JSONObject(myUrl);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            ccID = new String[jsonArray.length()];
            ccBBName = new String[jsonArray.length()];
            ccStuWrite = new String[jsonArray.length()];
            ccAnonymous = new String[jsonArray.length()];
            ccStuRange = new String[jsonArray.length()];
            ccLastDate = new String[jsonArray.length()];
            ccItemNum = new String[jsonArray.length()];
            ccCharNum = new String[jsonArray.length()];
            ccTotalSize = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                ccID[i] = row.getString("ccID");
                ccBBName[i] = row.getString("ccName");
                ccStuWrite[i] = row.getString("ccStuWrite");
                ccAnonymous[i] = row.getString("ccAnonymous");
                ccStuRange[i] = row.getString("ccStuRange");
                ccLastDate[i] = row.getString("ccLastDate");
                ccItemNum[i] = row.getString("ccItemNum");
                ccCharNum[i] = row.getString("ccCharNum");
                ccTotalSize[i] = row.getString("ccTotalSize");
            }
        }catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }

    private  void requestBullentinBoard(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ctID", "c398223976");
        data.put("caID", "id1");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myUrl = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/BBL", "POST").get();
            jsonObject = new JSONObject(myUrl);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            ccID = new String[jsonArray.length()];
            ccBBName = new String[jsonArray.length()];
            ccStuWrite = new String[jsonArray.length()];
            ccAnonymous = new String[jsonArray.length()];
            ccStuRange = new String[jsonArray.length()];
            ccLastDate = new String[jsonArray.length()];
            ccItemNum = new String[jsonArray.length()];
            ccCharNum = new String[jsonArray.length()];
            ccTotalSize = new String[jsonArray.length()];
            ccStuNum = new String[jsonArray.length()];

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject row = jsonArray.getJSONObject(i);
                ccID[i] = row.getString("ccID");
                ccBBName[i] = row.getString("ccName");
                ccStuWrite[i] =row.getString("ccStuWrite");
                ccAnonymous[i] =row.getString("ccAnonymous");
                ccStuRange[i] = row.getString("ccStuRange");
                ccLastDate[i] = row.getString("ccLastDate");
                ccItemNum[i] = row.getString("ccItemNum");
                ccCharNum[i] = row.getString("ccCharNum");
                ccTotalSize[i] = row.getString("ccTotalSize");
                ccStuNum[i] = row.getString("ccStuNum");
              //  System.out.println("+++++++++++++++++++++"+ ccLastDate[i]);
                prepareQuestion(ccStuRange[i],ccBBName[i],ccAnonymous[i],ccLastDate[i],ccItemNum[i],ccCharNum[i],ccTotalSize[i]);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

    }


    private void createBBDialog(){
        final Dialog d = new Dialog(this);
       // d.setTitle("New creation of Bulletin Board");
        d.setContentView(R.layout.activity_create__bulletin_board);
        Toolbar hideToolbar = d.findViewById(R.id.creationToolbar);
        hideToolbar.setVisibility(View.GONE);
        /*setSupportActionBar(hideToolbar);
        getSupportActionBar().hide();*/

        requiredMsg = d.findViewById(R.id.requiredMsg);
        bbNameeditText = (EditText) d.findViewById(R.id.createBulletinBoard_edt);

        RadioGroup radioGroup1, radioGroup2,radioGroup3 ;
        RadioButton radioFailed,radioFair,radioAnonymous,radioOnlyInstructor,radioRegistered,radioNone,radioChoice,radioAll;

        radioGroup1 = d.findViewById(R.id.radioGroup1);
        radioGroup2 = d.findViewById(R.id.radioGroup2);
        radioGroup3 = d.findViewById(R.id.radioGroup3);

        radioGroup1.setOnCheckedChangeListener(listener1);
        radioGroup2.setOnCheckedChangeListener(listener2);
        radioGroup3.setOnCheckedChangeListener(listener3);

        radioFailed = d.findViewById(R.id.radioButton1);
        radioFair = d.findViewById(R.id.radioButton2);
        radioAnonymous = d.findViewById(R.id.radioButton3);
        radioOnlyInstructor = d.findViewById(R.id.radioButton4);
        radioRegistered = d.findViewById(R.id.radioButton5);
        radioNone = d.findViewById(R.id.radioButton6);
        radioChoice = d.findViewById(R.id.radioButton7);
        radioAll = d.findViewById(R.id.radioButton8);

        Button create = d.findViewById(R.id.createButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bbName1 = bbNameeditText.getText().toString();
              //  System.out.println("|||||||||||||||||||||||||||||||||||||" + bbName1);
                bbarticle1 = articleSubmission;
              //  System.out.println("|||||||||||||||||||||||||||||||||||||" + bbarticle1);
                bbanonymous1 = anonymous1;
              //  System.out.println("|||||||||||||||||||||||||||||||||||||" + bbanonymous1);
                bbapplicable1 = applicableStudent;
               // System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" + bbapplicable1);
                if (bbName1.equals("")){
                    requiredMsg.setVisibility(View.VISIBLE);
                }
                else {
                    ArrayMap<String,String> header = new ArrayMap<>();
                    ArrayMap<String,String> data = new ArrayMap<>();
                    data.put("ctID", "c398223976");
                    data.put("cc_name", bbName1);
                   // System.out.println("|||||||||||||||||||||||||||||||||||||" + bbName1);
                    data.put("cc_stuwrite", bbarticle1);
                 //   System.out.println("|||||||||||||||||||||||||||||||||||||" + bbarticle1);
                    data.put("cc_anonymous", bbanonymous1);
                  //  System.out.println("|||||||||||||||||||||||||||||||||||||" + bbanonymous1);
                    data.put("cc_sturange", bbapplicable1 );
                 //   System.out.println("|||||||||||||||||||||||||||||||||||||" + bbapplicable1);
                    HttpRequestAsync myHttp = new HttpRequestAsync(header,data);

                    try {
                        String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/cateCreate", "POST").get();
                        System.out.println(",," + myUrl);
                        JSONObject jsonObject = new JSONObject(myUrl);
                        System.out.println("|||||||||||||||||||||||||||||||||||||" + myUrl);
                        String mes = jsonObject.getString("mes");
                        System.out.println(":::::::::::::::::::::::::::::::::::::" + mes);
                        executeAPI();
                        if (bbapplicable1.equals("1")){
                            Intent intent = new Intent(Bulletin_Board_List.this, Choice_Stu_add.class);
                            executeAPI();
                            intent.putExtra("ccID", ccID[0]);
                            startActivity(intent);
                            d.dismiss();

                        }else {
                            d.dismiss();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }

                       /* Intent intent = new Intent(Bulletin_Board_List.this, Bulletin_Board_List.class);
                        startActivity(intent);*/
                        //requestBullentinBoard();

                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        d.show();
        Window window = d.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radioButton1:
                    articleSubmission = "0";
                    break;
                case R.id.radioButton2:
                    articleSubmission = "1";
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radioButton3:
                    anonymous1 = "0";
                    break;
                case R.id.radioButton4:
                    anonymous1 = "1";
                    break;
                case R.id.radioButton5:
                    anonymous1 = "2";
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radioButton6:
                    applicableStudent = "0";
                    break;
                case R.id.radioButton7:
                    applicableStudent = "1";
                    break;
                case R.id.radioButton8:
                    applicableStudent = "2";
                    break;
            }
        }
    };


    private void prepareQuestion2(String applicable,String name, String anonymous) {

        BulletinBoard a = new BulletinBoard (applicable,name, "2018/02/20 09:38", "", anonymous, "", "");
        bulletinBoardList.add(a);
        adapter.notifyDataSetChanged();
    }

    private void prepareQuestion(String applicable, String name, String anonymous,String date, String article, String wordCount, String capacity) {
        BulletinBoard a = new BulletinBoard (applicable,name, date, capacity, anonymous, article,wordCount);
        bulletinBoardList.add(a);
        mAdapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
