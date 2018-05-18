package kit.c_learning.teacherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.ThreadComment;

public class PostActivity extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 1;
    static Uri mSelectedUri;
    static String imgDecodableString;
    Toolbar threadToolBar;
    private RecyclerView recyclerView;
    private Context mContext;
    private List<ThreadComment> threadComments;
    private ThreadCommentAdapter adapter;

    static String threadNo, threadID;
    JSONArray jsonArray;
    JSONObject object;

    ImageView postOwnerDisplayImageView,  postDisplayImageVIew,postDisplayImageVIew2,postDisplayImageVIew3, more_Option;
    TextView postOwnerUsernameTextView, postTimeCreatedTextView , postTextTextView , postBodyTextView, postNumReadsTextView, postNumCommentsTextView ;

    CardView commentCardView, importFileCardView;
    static EditText comment;
    ImageView send, camera,gallery, dropBox;
    String fid1,fid2,fid3, userCommentInput;

    static String[] ccID,cNO,cRoot,cBranch,cParent,cText, cDate,ttName,stName, fID1,fID2,fID3,
                    cAlreadyNum, cIDComment, cIDReply, textComment;
    static  String[] ttNameReply, stNameReply, ttNameComment, stNameComment, fID1Comment, fID2Comment, fID3Comment,
                     fID1Reply, fID2Reply, fID3Reply;

    ArrayList<Integer> comment_ReplyRow = new ArrayList<>();
    ArrayList<Integer> comment_Row = new ArrayList<>();
    ArrayList<Integer> reply_Row = new ArrayList<>();
    ArrayList<Integer> wantedIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        threadToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(threadToolBar);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("cTitle"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();
        initPost();
        getAllCommentAPI();
        commentAndReplyIndex();
    }
    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        threadComments = new ArrayList<>();
        adapter = new ThreadCommentAdapter(this,threadComments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ThreadCommentAdapter mAdapter1 = new ThreadCommentAdapter(this,threadComments);
        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((ThreadCommentAdapter) mAdapter1).setMode(Attributes.Mode.Single);
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
        recyclerView.setAdapter(adapter);
    }

    public  void getAllCommentAPI(){
        ArrayMap<String, String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID", getIntent().getExtras().getString("ccID"));
        data.put("ttID", MainActivity.ttID);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try{
            String myUrl = myHttp.execute("http://kit.c-learning.jp/t/ajax/coop/list", "POST").get();
            object = new JSONObject(myUrl);
            jsonArray = object.getJSONArray("data");
            ccID = new String[jsonArray.length()];
            cNO = new String[jsonArray.length()];
            cRoot = new String[jsonArray.length()];
            cBranch = new String[jsonArray.length()];
            cParent= new String[jsonArray.length()];
            cText= new String[jsonArray.length()];
            cDate = new String[jsonArray.length()];
            ttName = new String[jsonArray.length()];
            stName = new String[jsonArray.length()];
            fID1 = new String[jsonArray.length()];
            fID2 = new String[jsonArray.length()];
            fID3 = new String[jsonArray.length()];
            cAlreadyNum = new String[jsonArray.length()];

            cIDComment = new String[jsonArray.length()];
            cIDReply = new String[jsonArray.length()];
            textComment = new String[jsonArray.length()];
            ttNameComment = new String[jsonArray.length()];
            stNameComment = new String[jsonArray.length()];
            ttNameReply = new String[jsonArray.length()];
            stNameReply = new String[jsonArray.length()];
            fID1Reply = new String[jsonArray.length()];
            fID2Reply = new String[jsonArray.length()];
            fID3Reply = new String[jsonArray.length()];
            fID1Comment = new String[jsonArray.length()];
            fID2Comment = new String[jsonArray.length()];
            fID3Comment = new String[jsonArray.length()];
            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject row = jsonArray.getJSONObject(i);
                ccID[i]= row.getString("ccID");
                cNO[i] = row.getString("cNO");
                cRoot[i] = row.getString("cRoot");
                cBranch[i] = row.getString("cBranch");
                cParent[i] = row.getString("cParent");
                cText[i] = row.getString("cText");
                cDate[i] = row.getString("cDate");
                stName[i] = row.getString("stName");
                ttName[i] = row.getString("ttName");
                fID1[i] = row.getString("fID1");
                fID2[i] = row.getString("fID2");
                fID3[i] = row.getString("fID3");
                cAlreadyNum[i] = row.getString("cAlreadyNum");
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void initPost(){
        threadID = getIntent().getExtras().getString("ccID");
        threadNo = getIntent().getExtras().getString("cNO");

        more_Option = (ImageView) findViewById(R.id.more_option);
        more_Option.setVisibility(View.GONE);

        postNumReadsTextView = (TextView) findViewById(R.id.tv_likes);
        postNumReadsTextView.setText(getIntent().getExtras().getString("cAlreadyNum"));

        postTextTextView = (TextView) findViewById(R.id.tv_post_text);
        postTextTextView.setText(getIntent().getExtras().getString("cTitle"));

        postNumCommentsTextView = findViewById(R.id.tv_comments);

        postOwnerUsernameTextView = (TextView) findViewById(R.id.tv_post_username);
        postOwnerUsernameTextView.setText(getIntent().getExtras().getString("ttName"));
        if (Make_Thread.bbAnonymous.equals("0") || Make_Thread.bbAnonymous.equals("1")){
            if (!postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)){
                postOwnerUsernameTextView.setText("Anonymous");
            }
            else if(postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)){
                postOwnerUsernameTextView.setText(getIntent().getExtras().getString("ttName"));
            }
        }else if(Make_Thread.bbAnonymous.equals("2")){
            if (postOwnerUsernameTextView.getText().toString().equals("null")){
                postOwnerUsernameTextView.setText(getIntent().getExtras().getString("stName"));
            }
            else postOwnerUsernameTextView.setText(getIntent().getExtras().getString("ttName"));
        }

        postBodyTextView = (TextView) findViewById(R.id.tv_post_body);
        postBodyTextView.setText(getIntent().getExtras().getString("cText"));

        postTimeCreatedTextView = (TextView) findViewById(R.id.tv_time);
        postTimeCreatedTextView.setText(getIntent().getExtras().getString("cDate"));

        postOwnerDisplayImageView = (ImageView) findViewById(R.id.iv_post_owner_display);
        if(postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)){
            Picasso.with(mContext).load(getIntent().getExtras().getString("userProfileComment"))
                    .into(postOwnerDisplayImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onError() {}
                    });
        } else if(!postOwnerUsernameTextView.getText().toString().equals(MainActivity.ttName)){
            postOwnerDisplayImageView.setImageResource(R.drawable.facebook_avatar);
        }

        postDisplayImageVIew = (ImageView) findViewById(R.id.iv_post_display);
        fid1 = getIntent().getExtras().getString("fID1");
        if (!fid1.equals("")){
            postDisplayImageVIew.setVisibility(View.VISIBLE);
            Picasso.with(this).load(getIntent().getExtras().getString("imageFile"))
                    .into(postDisplayImageVIew, new com.squareup.picasso.Callback()
                    {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onError() {}
                    });
        } else if (fid1.equals("")){
            postDisplayImageVIew.setVisibility(View.GONE);
        }
        postDisplayImageVIew2 = (ImageView) findViewById(R.id.iv_post_display2);
        fid2 = getIntent().getExtras().getString("fID2");
        if (!fid2.equals("")){
            postDisplayImageVIew2.setVisibility(View.VISIBLE);
            Picasso.with(this).load(getIntent().getExtras().getString("imageFile2"))
                    .into(postDisplayImageVIew2, new com.squareup.picasso.Callback()
                    {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onError() {}
                    });
        } else if (fid2.equals("")){
            postDisplayImageVIew2.setVisibility(View.GONE);
        }

        postDisplayImageVIew3 = (ImageView) findViewById(R.id.iv_post_display3);
        fid3 = getIntent().getExtras().getString("fID3");
        if (!fid3.equals("")){
            postDisplayImageVIew3.setVisibility(View.VISIBLE);
            Picasso.with(this).load(getIntent().getExtras().getString("imageFile3"))
                    .into(postDisplayImageVIew3, new com.squareup.picasso.Callback()
                    {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onError() {}
                    });
        } else if (fid3.equals("")){
            postDisplayImageVIew3.setVisibility(View.GONE);
        }

        comment = (EditText) findViewById(R.id.et_comment);
        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    importFileCardView.setVisibility(View.VISIBLE);

                }else {
                    importFileCardView.setVisibility(View.GONE);
                }
            }
        });
        importFileCardView = (CardView) findViewById(R.id.importFileCardView);
        commentCardView = (CardView) findViewById(R.id.commentCardView);

        send = (ImageView) findViewById(R.id.iv_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCommentInput = comment.getText().toString();
                PostActivity.BackgroundTask task = new PostActivity.BackgroundTask(PostActivity.this);
                task.execute();
            }
        });
        gallery = findViewById(R.id.iv_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
            }
        });
    }

    private void commentAndReplyIndex(){
        for (int i= 0; i < jsonArray.length(); i++){
            if(threadNo.equals(cRoot[i])){
                comment_ReplyRow.add(i);
            }
        }
       // System.out.println("-----Comment & Reply Index-----" + comment_ReplyRow);
        if(comment_ReplyRow.size() > 0){
            commentORReplyIndex();
            wantedIndex();
            displayCommentAndReply();
        }
    }

    private void commentORReplyIndex(){
        for (int i = 0; i < comment_ReplyRow.size(); i++){
            if(threadNo.equals(cParent[comment_ReplyRow.get(i)]) && cBranch[comment_ReplyRow.get(i)].equals("0")){
                comment_Row.add(comment_ReplyRow.get(i));
               // System.out.println("-----Comment Index-----" + comment_Row);
            }
            else {
                reply_Row.add(comment_ReplyRow.get(i));
               // System.out.println("-----Reply Index-----" + reply_Row);
            }
        }
    }

    private void wantedIndex(){
        if (comment_Row.size() >0){
            for (int i = 0 ; i < comment_Row.size(); i ++){
                wantedIndex.add(comment_Row.get(i));
                if(reply_Row.size() > 0){
                    for (int j = 0 ; j < reply_Row.size(); j++){
                        if(cNO[comment_Row.get(i)].equals(cParent[reply_Row.get(j)])){
                            wantedIndex.add(reply_Row.get(j));
                        }
                    }
                }
            }
            System.out.println("-----Wanted Index-----" + wantedIndex);
        }
    }

    private void displayCommentAndReply(){
        for (int i =0; i<comment_ReplyRow.size(); i++){
            if(threadNo.equals(cParent[wantedIndex.get(i)]) && cBranch[wantedIndex.get(i)].equals("0")){
                prepareDisplayComment(ttName[wantedIndex.get(i)],cText[wantedIndex.get(i)],cDate[wantedIndex.get(i)]);
                cIDComment[i] =  cNO[wantedIndex.get(i)];
                textComment[i]= cText[wantedIndex.get(i)];
                stNameComment[i]= stName[wantedIndex.get(i)];
                ttNameComment[i]= ttName[wantedIndex.get(i)];
                fID1Comment[i] = fID1[wantedIndex.get(i)];
                fID2Comment[i] = fID1[wantedIndex.get(i)];
                fID3Comment[i] = fID1[wantedIndex.get(i)];
            }
            else {
                prepareDisplayReply(ttName[wantedIndex.get(i)],cText[wantedIndex.get(i)],cDate[wantedIndex.get(i)]);
                cIDReply[i]  = cNO[wantedIndex.get(i)];
                stNameReply[i]= stName[wantedIndex.get(i)];
                ttNameReply[i] = ttName[wantedIndex.get(i)];
                fID1Reply[i] = fID1[wantedIndex.get(i)];
                fID2Reply[i] = fID1[wantedIndex.get(i)];
                fID3Reply[i] = fID1[wantedIndex.get(i)];
            }
        }
        postNumCommentsTextView.setText(""+adapter.getItemCount());
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(PostActivity activity) {
            dialog = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            if(comment.getHint().toString().equals("Write a reply...")){
                dialog.setMessage("Sending reply...");
            } else if(comment.getHint().toString().equals("Write a comment...")){
                dialog.setMessage("Sending comment...");
            }
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                if(comment.getHint().toString().equals("Write a reply...")){
                    System.out.println("*******************");
                    replyThreadAPI();
                    threadComments.clear();
                    comment_ReplyRow.clear();
                    comment_Row.clear();
                    reply_Row.clear();
                    wantedIndex.clear();
                    getAllCommentAPI();
                    commentAndReplyIndex();
                    dialog.dismiss();
                    comment.setText("");
                } else if(comment.getHint().toString().equals("Write a comment...")){
                    System.out.println("%%%%%%%%%%%%%%%%%%%");
                    commentThreadAPI();
                    threadComments.clear();
                    comment_ReplyRow.clear();
                    comment_Row.clear();
                    reply_Row.clear();
                    wantedIndex.clear();
                    getAllCommentAPI();
                    commentAndReplyIndex();
                    dialog.dismiss();
                    comment.setText("");
                }
               /* commentThreadAPI();
                dialog.dismiss();
                comment.setText("");*/
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //check internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void replyThreadAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("m","input");
        data.put("ct","c398223976");
        data.put("cc", getIntent().getExtras().getString("ccID"));
        data.put("cn",cIDComment[ThreadCommentAdapter.index]);
        data.put("ttName",MainActivity.ttName);
        data.put("ttID",MainActivity.ttID);
        data.put("c_text",userCommentInput);

        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        if (isNetworkAvailable()){
            try {
                String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/CoopReply","POST").get();
               // prepareComment(MainActivity.ttName,userCommentInput);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
    }

    private void commentThreadAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("m","input");
        data.put("ct","c398223976");
        data.put("cc", getIntent().getExtras().getString("ccID"));
        data.put("cn",getIntent().getExtras().getString("cNO"));
        data.put("ttName",MainActivity.ttName);
        data.put("ttID",MainActivity.ttID);
        data.put("c_text",userCommentInput);

        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        if (isNetworkAvailable()){
            try {
                String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/CoopReply","POST").get();
              //  prepareComment(MainActivity.ttName,userCommentInput);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }else Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();

    }

    public void prepareComment(String username, String text){
        if (comment.getHint().toString().equals("Write a reply...")){
            ThreadComment reply = new ThreadComment(username,text, "", ThreadComment.ItemType.TWO_ITEM);
           // threadComments.add(reply);
            threadComments.add(ThreadCommentAdapter.index + 1,reply);
            adapter.notifyItemChanged(ThreadCommentAdapter.index + 1);
            adapter.notifyItemRangeChanged(ThreadCommentAdapter.index +1, adapter.getItemCount());
        }
        else if(comment.getHint().toString().equals("Write a comment...")){
            ThreadComment comment = new ThreadComment(username,text, "", ThreadComment.ItemType.ONE_ITEM);
            System.out.println("---455------" + username);
            threadComments.add(comment);
            adapter.notifyDataSetChanged();
        }

    }

    public void prepareDisplayReply(String username, String text, String date){
        ThreadComment reply = new ThreadComment(username,text, date, ThreadComment.ItemType.TWO_ITEM);
        threadComments.add(reply);
        adapter.notifyDataSetChanged();
    }

    public void prepareDisplayComment(String username, String text, String date){
        ThreadComment comment = new ThreadComment(username,text, date, ThreadComment.ItemType.ONE_ITEM);
        threadComments.add(comment);
        adapter.notifyDataSetChanged();
    }

    private void isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.v("LOG", "Permission granted");
               selectImage();
            } else {
                Log.v("LOG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }else {
            Log.v("LOG", "Permission granted");
            selectImage();
        }
    }
    private void selectImage() {
      /*  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);*/
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RC_PHOTO_PICKER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                mSelectedUri = data.getData();
                String[] filePath =  { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(mSelectedUri, filePath, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
            }
        }
    }
}
