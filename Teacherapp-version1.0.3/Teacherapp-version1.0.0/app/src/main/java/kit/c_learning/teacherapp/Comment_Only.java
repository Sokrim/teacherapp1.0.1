package kit.c_learning.teacherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Comment_Only extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment__only);

        Toolbar resultToolbar = (Toolbar) findViewById(R.id.totalResult_toolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner displaySpinner = (Spinner) findViewById(R.id.display_Questionnaires);
        Spinner guestSpinner = (Spinner) findViewById(R.id.guest);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Comment_Only.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.displayQuestionnaires));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        displaySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Comment_Only.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.guest));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guestSpinner.setAdapter(myAdapter2);
    }
}
