package kit.c_learning.teacherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Reply_Thread extends AppCompatActivity {
    TextView reply;
    Toolbar replyToolbar;
    RecyclerView recyclerView;
    TextView userName, userComment, createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_thread);
        reply = (TextView) findViewById(R.id.tv_reply);
        reply.setVisibility(View.GONE);
        replyToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(replyToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpRecyclerView();
        initPost();
    }

    protected void setUpRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.reply_recyclerview);
      //  threadComments = new ArrayList<>();
     //   adapter = new ThreadCommentAdapter(this,threadComments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      //  recyclerView.setAdapter(adapter);

    }
    protected void initPost(){
        userName = (TextView) findViewById(R.id.tv_username);
        userComment = (TextView) findViewById(R.id.tv_comment);
        createTime  = (TextView) findViewById(R.id.tv_time);
    }
}
