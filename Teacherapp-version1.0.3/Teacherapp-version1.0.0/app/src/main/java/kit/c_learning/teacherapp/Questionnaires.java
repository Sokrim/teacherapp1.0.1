package kit.c_learning.teacherapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.Question;


public class Questionnaires extends AppCompatActivity {
    Toolbar questionnairesToolbar;
    Button quickButton,yesNoButton, yesNoButtonComment,
            agreeDisagree,agreeDisagreeComment,
            twoChoice,twoChoiceComment,
            threeChoice,threeChoiceComment,
            fourChoice,fourChoiceComment,fiveChoice,
            fiveChoiceComment,commentOnly, okay;

    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private List<Question> questionList;
    String[] questionTitle = null,publishDate=null,qbID = null;
    public static String[] questionType = null, questionID = null, questionComment = null, qbTitle = null, qbDate = null;
    //    JSONObject jsonObject =null,success=null;
    String[] qbMode =null;
    JSONObject jsonObject =null,success1=null,success2= null,success3=null;
    int i ;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaires);
        questionnairesToolbar = (Toolbar) findViewById(R.id.questionnaires_toolbar);
        setSupportActionBar(questionnairesToolbar);
        getSupportActionBar().setTitle("Questionnaires");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        questionList = new ArrayList<>();
        adapter = new QuestionAdapter(this,questionList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //request api
        requestApiQuestion();

        //When click on quick questionnaires button & create_thread questionnaires
        quickButton = (Button) findViewById(R.id.quick_btn);
        quickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayQuickDialog();
            }
        });

    }

    //check internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private  void displayQuickDialog(){
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.test);
        d.show();
        Window window = d.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        yesNoButton = d.findViewById(R.id.yesNo);
        yesNoButtonComment = d.findViewById(R.id.yesNoComment);
        agreeDisagree = d.findViewById(R.id.agreeDisagree);
        agreeDisagreeComment = d.findViewById(R.id.agreeDisagreeComment);
        twoChoice = d.findViewById(R.id.twoChoice);
        twoChoiceComment = d.findViewById(R.id.twoChoiceComment);
        threeChoice = d.findViewById(R.id.threeChoice);
        threeChoiceComment = d.findViewById(R.id.threeChoiceComment);
        fourChoice = d.findViewById(R.id.fourChoice);
        fourChoiceComment = d.findViewById(R.id.fourChoiceComment);
        fiveChoice = d.findViewById(R.id.fiveChoice);
        fiveChoiceComment = d.findViewById(R.id.fiveChoiceComment);
        commentOnly = d.findViewById(R.id.commentOnly);
        okay = d.findViewById(R.id.okay);

        yesNoButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","22");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","Yes/No"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        yesNoButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","23");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","Yes/No*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        agreeDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","24");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","Agree/Disagree"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        agreeDisagreeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","25");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","Agree/Disagree*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        twoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","20");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","TwoChoice"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();

            }
        });

        twoChoiceComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","21");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","TwoChoice*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        threeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","30");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","ThreeChoice"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        threeChoiceComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","31");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","ThreeChoice*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        fourChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","40");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","FourChoice"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        fourChoiceComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","41");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);

                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","FourChoice*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        fiveChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","50");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","FiveChoice"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        fiveChoiceComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","51");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Pie_Chart_Quick.class);
                        startActivity(intent.putExtra("qbTitle","FiveChoice*"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });

        commentOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayMap<String, String> headers = new ArrayMap<>();
                ArrayMap<String, String> data = new ArrayMap<>();
                data.put("ctID","c398223976");
                data.put("qbMode","1");
                HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
                if (isNetworkAvailable()){
                    try {
                        String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                        jsonObject = new JSONObject(text);
                        Intent intent = new Intent(Questionnaires.this,Comment_Only.class);
                        startActivity(intent.putExtra("qbTitle","CommentOnly"));
                        finish();
                    } catch (InterruptedException | JSONException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }


    @SuppressLint("LongLogTag")
    private void requestApiQuickQuestionaires() {
        ArrayMap<String, String> headers = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("qbMode","22");
        data.put("qbMode","23");

        HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);

        if(isNetworkAvailable()) {
            try {
                String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Insert", "POST").get();
                jsonObject = new JSONObject(text);
                Log.d("mer qbmode ======================-",jsonObject.toString());
                success1 = jsonObject.getJSONObject("res");
                success2 = success1.getJSONObject("bent");
                JSONArray jsonArray = success2.getJSONArray("ALL");
                qbMode = new String[jsonArray.length()];
//            per = new String[jsonArray.length()];

                for(i = 1; i< jsonArray.length(); i++){
                    JSONObject row = jsonArray.getJSONObject(i);
                    qbMode[i] = row.getString("qbMode");
                    Log.d("mer qbmode from api ========================",qbMode[i]);
//                per[i] = row.getString("per");
//                num[1] = row.getString("num");
//                num[2] = row.getString("num");
//                Log.d(num[1],num[2]);
                }
            } catch (InterruptedException | JSONException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("LongLogTag")
    private void requestApiQuestion(){
        android.util.ArrayMap<String, String> headers = new android.util.ArrayMap<>();
        android.util.ArrayMap<String, String> data = new android.util.ArrayMap<>();

        if(isNetworkAvailable()) {
            HttpRequestAsync myHttp = new HttpRequestAsync(headers);
            try {
                String text = myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Question", "GET").get();
                jsonObject = new JSONObject(text);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                questionTitle = new String[jsonArray.length()];
                publishDate = new String[jsonArray.length()];
                qbID = new String[jsonArray.length()];
                questionType = new String[jsonArray.length()];
                questionID = new String[jsonArray.length()];
                questionComment = new String[jsonArray.length()];
                qbTitle = new String[jsonArray.length()];
                qbDate = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    questionTitle[i] = row.getString("qbTitle");
                    publishDate[i] = row.getString("qbDate");
                    qbID[i] = row.getString("qbID");
                    questionType[i] = row.getString("qbQuickMode");
                    questionID[i] = row.getString("qbID");
                    questionComment[i] = row.getString("qbComment");
                    qbTitle[i] = row.getString("qbTitle");
                    qbDate[i] = row.getString("qbDate");
                    prepareQuestion(questionTitle[i], publishDate[i]);
                }

            } catch (InterruptedException | JSONException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void prepareQuestion(String questionTitle,String publishDate) {
        Question a = new Question ("Now Public", questionTitle, publishDate, "Non-disclosure", "Anonymous", 0);
        questionList.add(a);
        adapter.notifyDataSetChanged();
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

