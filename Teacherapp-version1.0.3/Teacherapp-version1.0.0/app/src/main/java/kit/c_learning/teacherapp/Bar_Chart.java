package kit.c_learning.teacherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Bar_Chart extends AppCompatActivity {
    com.github.mikephil.charting.charts.BarChart barChart;
    EditText updateQuestion;
    TextView questionTitle;

    ArrayList<Integer> item_content = new ArrayList<Integer>();
    String item_id, item_comment, result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart);
        item_id = getIntent().getExtras().getString("id");
        if (getIntent().getExtras().getString("comment").equals("")) item_comment = "0";
        else item_comment = "1";

        questionTitle = (TextView) findViewById(R.id.qTitle_TextView);
        questionTitle.setText("[Q]" + getIntent().getExtras().getString("qbTitle"));


        Toolbar resultToolbar = (Toolbar) findViewById(R.id.totalResult_toolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner displaySpinner = (Spinner) findViewById(R.id.display_Questionnaires);
        Spinner guestSpinner = (Spinner) findViewById(R.id.guest);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Bar_Chart.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.displayQuestionnaires));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displaySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Bar_Chart.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.guest));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guestSpinner.setAdapter(myAdapter2);


        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setPinchZoom(false);
        barChart.setVisibleYRangeMaximum(3, YAxis.AxisDependency.LEFT);
        barChart.setDrawValueAboveBar(false);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setSpaceTop(0);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setSpaceTop(0);
        rightAxis.setAxisMinimum(0f);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);

        ArrayMap<String, String> datas = new ArrayMap<>();
        ArrayMap<String, String> headers = new ArrayMap<>();

        datas.put("qb", item_id);
        datas.put("com", item_comment);
        datas.put("mode", "ALL");

        HttpRequestAsync requestAsync = new HttpRequestAsync(headers, datas);
        try {
            result = requestAsync.execute("https://kit.c-learning.jp/t/ajax/quest/Bent", "POST").get();
            JSONObject jsonObject = new JSONObject(result);
            String success_res = jsonObject.getString("res");
            JSONObject bent = new JSONObject(success_res);
            String success_bent = bent.getString("bent");

            JSONObject final_success = new JSONObject(success_bent);
//            JSONObject res = (JSONObject) jsonObject.get("res");
//            JSONObject bent = (JSONObject) res.get("bent");

            for (int i=1; i<final_success.getJSONArray("ALL").length(); i++){
                JSONObject innerJSONObj = new JSONObject(final_success.getJSONArray("ALL").getString(i));
                item_content.add(innerJSONObj.getInt("num"));
            }

            int[] item = new int[item_content.size()];
            for (int i=0; i<item.length; i++) item[i] = item_content.get(i);
            setData(item);
        } catch (InterruptedException | JSONException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setData(int[] size) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        String[] labels = new String[size.length];
        for (int i=0; i<size.length; i++){
            yVals1.add(new BarEntry(i, size[i]));
            labels[i] = String.valueOf(size[i]);
        }
        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "All Display");
            set1.setDrawIcons(false);
            set1.setColors(rgb("#0277BD"),
                    rgb("#00C853"),
                    rgb("#CC0000"),
                    rgb("#FFFF00"),
                    rgb("#87ceeb"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(1.0f);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.setData(data);
            updateQuestion = (EditText) findViewById(R.id.updateQuestion);
            updateQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateQuestion.setCursorVisible(true);
                    updateQuestion.setHint("");
                }
            });
        }
    }
}
