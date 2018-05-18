package kit.c_learning.teacherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Pie_Chart_Quick extends AppCompatActivity {
    private PieChart pieChart;
    private PieData pieData;
    String[] num =null,per=null;
    JSONObject jsonObject =null,success1=null,success2= null,success3=null;
    int i ;
    @SuppressLint("LongLogTag")

    EditText updateQuestion;
    TextView questionTitle;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie__chart__quick);

        Toolbar resultToolbar = (Toolbar) findViewById(R.id.totalResult_toolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner displaySpinner = (Spinner) findViewById(R.id.display_Questionnaires);
        Spinner guestSpinner = (Spinner) findViewById(R.id.guest);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Pie_Chart_Quick.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.displayQuestionnaires));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displaySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Pie_Chart_Quick.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.guest));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guestSpinner.setAdapter(myAdapter2);

        questionTitle = (TextView) findViewById(R.id.qTitle_TextView);
        questionTitle.setText("[Q]" + getIntent().getExtras().getString("qbTitle"));


        ///request api
        ArrayMap<String, String> headers = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("qb", getIntent().getExtras().getString("id"));

        HttpRequestAsync myHttp = new HttpRequestAsync(headers,data);
        try {
            String text= myHttp.execute("https://kit.c-learning.jp/t/ajax/quest/Bent", "POST").get();
            jsonObject = new JSONObject(text);
            success1 = jsonObject.getJSONObject("res");
            success2 = success1.getJSONObject("bent");
            JSONArray jsonArray = success2.getJSONArray("ALL");
            num = new String[jsonArray.length()];
            per = new String[jsonArray.length()];

            for(i = 1; i< jsonArray.length(); i++){
                JSONObject row = jsonArray.getJSONObject(i);
                num[i] = row.getString("num");
                per[i] = row.getString("per");
                num[1] = row.getString("num");
                num[2] = row.getString("num");
                Log.d(num[1],num[2]);
            }
        } catch (InterruptedException | JSONException | ExecutionException e) {
            e.printStackTrace();
        }

//        pieChart = findViewById(R.id.piechart);
//        pieChart.setUsePercentValues(true);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,5);
//        pieChart.setDragDecelerationFrictionCoef(0.15f);
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setHoleColor(Color.WHITE);
//        pieChart.setTransparentCircleRadius(61f);
//
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(Float.parseFloat(per[1]),"Yes"));
//        entries.add(new PieEntry(Float.parseFloat(per[2]),"NO"));
//
//        pieChart.animateY(1000, Easing.EasingOption.EaseInCubic);
//        PieDataSet dataSet = new PieDataSet(entries,"");
//        dataSet.setSliceSpace(3f);
//        dataSet.setColors(rgb("#00C853"), rgb("#CC0000"));
//        PieData pieData = new PieData(dataSet);
//        pieData.setValueTextSize(10f);
//        pieData.setValueTextColor(Color.YELLOW);
//        pieChart.setData(pieData);
//        updateQuestion = findViewById(R.id.updateQuestion);
//        updateQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateQuestion.setCursorVisible(true);
//                updateQuestion.setHint("");
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Pie_Chart_Quick.this,Questionnaires.class);
        startActivity(intent);
    }
}
