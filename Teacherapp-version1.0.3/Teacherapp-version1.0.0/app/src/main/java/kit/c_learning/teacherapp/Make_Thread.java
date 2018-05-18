package kit.c_learning.teacherapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.models.ThreadList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Make_Thread extends AppCompatActivity {
    Toolbar toolBar;
    FloatingActionButton createIcon;
    String threadTitle, threadBody;

    private static final int RC_PHOTO_PICKER = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    static Uri mSelectedUri;
    private ImageView mPostDisplay;

    EditText titleThread, bodyThread;
    TextView requiredMsg, requiredMsg_body;

    private RecyclerView recyclerView;
    private CreateThreadAdapter adapter;
    private List<ThreadList> threadList;
    static JSONObject mesObj;
    static JSONArray mes;
    public static String[] cTitle, cText, cName,cAlreadyNum, cDate, fContentType1,
                           fContentType2,fContentType3, cNo, fID1, fID2, fID3, ccID , ttName, cID,stName;
    SearchView searchView;
    public  static String threadID,bbAnonymous;
    static String imgDecodableString;
    Service service;
    JSONObject object;
    JSONArray jsonArray;
    static String[] cNOList;
    String hval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_thread);
        toolBar = (Toolbar) findViewById(R.id.threadToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("ccName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SDCARDChecker.checkWeatherExternalStorageAvailableOrNot(Make_Thread.this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        service = new Retrofit.Builder().baseUrl("https://kit.c-learning.jp").client(client).build().create(Service.class);

        createIcon = (FloatingActionButton) findViewById(R.id.fab);
        createIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createThread();
            }
        });
       setupRecyclerView();
       getAllThreadAPI();
       threadID = getIntent().getExtras().getString("ccID");
       bbAnonymous =  getIntent().getExtras().getString("ccAnonymous");
    }

    static void  executeAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID",threadID);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/thread", "POST").get();
            mesObj = new JSONObject(myURL);
            mes = mesObj.getJSONArray("mes");

            stName = new String[mes.length()];
            cID = new String[mes.length()];
            ccID = new String[mes.length()];
            fID1 = new String[mes.length()];
            fID2 = new String[mes.length()];
            fID3 = new String[mes.length()];
            ttName = new String[mes.length()];
            cNo = new String[mes.length()];
            cName = new String[mes.length()];
            cText = new String[mes.length()];
            cTitle = new String[mes.length()];
            cAlreadyNum = new String[mes.length()];
            cDate = new String[mes.length()];
            fContentType1 = new String[mes.length()];
            fContentType2 = new String[mes.length()];
            fContentType3 = new String[mes.length()];

            for (int i = 0 ; i <mes.length(); i++){
                JSONObject row = mes.getJSONObject(i);
                stName[i] = row.getString("stName");
                cID[i] = row.getString("cID");
                ccID[i] = row.getString("ccID");
                fID1[i] = row.getString("fID1");
                fID2[i] = row.getString("fID2");
                fID3[i] = row.getString("fID3");
                ttName[i] = row.getString("ttName");
                cNo[i] = row.getString("cNO");
                cName[i] = row.getString("cName");
                cTitle[i] = row.getString("cTitle");
                cText[i] = row.getString("cText");
                cAlreadyNum[i]= row.getString("cAlreadyNum");
                cDate[i] = row.getString("cDate");
                fContentType1[i] = row.getString("fContentType1");
                fContentType2[i] = row.getString("fContentType2");
                fContentType3[i] = row.getString("fContentType3");
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

    }

    private void getAllThreadAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID", getIntent().getExtras().getString("ccID", null));
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/thread", "POST").get();
            mesObj = new JSONObject(myURL);
            mes = mesObj.getJSONArray("mes");
          //  System.out.println(mes);
            stName = new String[mes.length()];
            cID = new String[mes.length()];
            ccID = new String[mes.length()];
            fID1 = new String[mes.length()];
            fID2 = new String[mes.length()];
            fID3 = new String[mes.length()];
            ttName = new String[mes.length()];
            cNo = new String[mes.length()];
            cName = new String[mes.length()];
            cText = new String[mes.length()];
            cTitle = new String[mes.length()];
            cAlreadyNum = new String[mes.length()];
            cDate = new String[mes.length()];
            fContentType1 = new String[mes.length()];
            fContentType2 = new String[mes.length()];
            fContentType3 = new String[mes.length()];

            for (int i = 0 ; i <mes.length(); i++){
                JSONObject row = mes.getJSONObject(i);
                stName[i] = row.getString("stName");
                cID[i] = row.getString("cID");
                ccID[i] = row.getString("ccID");
                fID1[i] = row.getString("fID1");
                fID2[i] = row.getString("fID2");
                fID3[i] = row.getString("fID3");
                ttName[i] = row.getString("ttName");
                cNo[i] = row.getString("cNO");
                cName[i] = row.getString("cName");
                cTitle[i] = row.getString("cTitle");
                cText[i] = row.getString("cText");
                cAlreadyNum[i]= row.getString("cAlreadyNum");
                cDate[i] = row.getString("cDate");
                fContentType1[i] = row.getString("fContentType1");
                fContentType2[i] = row.getString("fContentType2");
                fContentType3[i] = row.getString("fContentType3");
                //  threadID = ccID[i];
                prepareThread(ttName[i],cTitle[i],cText[i],cAlreadyNum[i],cDate[i]);
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareThread(String username, String cTitle, String cText, String cAlreadyNum, String date) {
        ThreadList a = new ThreadList(username,cTitle,date, "0", cAlreadyNum, cText);
        threadList.add(a);
        adapter.notifyDataSetChanged();
    }

    private void prepareThread2(String username, String cTitle, String cText, String cAlreadyNum, String date) {
        ThreadList a = new ThreadList(username,cTitle,date, "0", cAlreadyNum, cText);
        threadList.add(0,a);
        adapter.notifyItemInserted(0);
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        System.out.println("00000000000000"+ adapter.getItemCount());
        executeAPI();
    }

    private void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        threadList = new ArrayList<>();
        adapter = new CreateThreadAdapter(this,threadList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void createThread(){
        final Dialog createThread = new Dialog(this);
        createThread.setContentView(R.layout.create_thread_dialog);
        createThread.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createThread.show();
        TextView txtClose = createThread.findViewById(R.id.txtclose);
        titleThread = createThread.findViewById(R.id.thread_Title_edt);
        bodyThread = createThread.findViewById(R.id.thread_Body_edt);
        Button createButton = createThread.findViewById(R.id.btnCreate);
        requiredMsg = createThread.findViewById(R.id.requiredMsg);
        requiredMsg_body = createThread.findViewById(R.id.requiredMsg_body);
        ImageView camera = createThread.findViewById(R.id.iv_camera);
        ImageView uploadImage = createThread.findViewById(R.id.iv_image);
        ImageView dropBoxFile = createThread.findViewById(R.id.iv_dropBox);
        mPostDisplay = createThread.findViewById(R.id.postDisplay);
        titleThread.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleThread.getText().toString().equals("")){
                    requiredMsg.setVisibility(View.VISIBLE);
                    requiredMsg.setText("*Please enter the title.");
                }else requiredMsg.setVisibility(View.GONE);

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bodyThread.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bodyThread.getText().toString().equals("")){
                    requiredMsg_body.setVisibility(View.VISIBLE);
                    requiredMsg_body.setText("*Please enter the body or specify the file.");
                }else requiredMsg_body.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createThread.dismiss();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCameraPermission();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleThread.getText().toString().equals("")){
                    requiredMsg.setVisibility(View.VISIBLE);
                    requiredMsg.setText("*Please enter the title.");
                }
                if (bodyThread.getText().toString().equals("")){
                    requiredMsg_body.setVisibility(View.VISIBLE);
                    requiredMsg_body.setText("*Please enter the body or specify the file.");
                }
                if (titleThread.getText().toString().equals("") && bodyThread.getText().toString().equals("")){
                    requiredMsg.setVisibility(View.VISIBLE);
                    requiredMsg.setText("*Please enter the title.");
                    requiredMsg_body.setVisibility(View.VISIBLE);
                    requiredMsg_body.setText("*Please enter the body or specify the file.");
                }
                else if(!titleThread.getText().toString().equals("") && !bodyThread.getText().toString().equals("")){
                    threadTitle = titleThread.getText().toString();
                    threadBody = bodyThread.getText().toString();
                   // createThreadAPI(threadTitle,threadBody);
                    createThreadWithFile(threadTitle,threadBody,hval);
                    System.out.println(threadTitle);
                    System.out.println(threadBody);
                    System.out.println(hval);
                    createThread.dismiss();
                }
            }
        });
    }

    private void createThreadAPI(String threadTitle, String threadBody) {
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("ttID", MainActivity.ttID);
        data.put("ttName", MainActivity.ttName);
        data.put("ttMail", MainActivity.ttEmail);
        data.put("mode", "pcreate");
        data.put("ccID",  getIntent().getExtras().getString("ccID"));
        data.put("c_title", threadTitle);
        data.put("c_text", threadBody);
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/res", "POST").get();
            JSONObject jsonObject = new JSONObject(myUrl);
            prepareThread2(MainActivity.ttName,threadTitle,threadBody,"","");
            executeAPI();
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void createThreadWithFile(String threadTitle, String threadBody, String file){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("ttID", MainActivity.ttID);
        data.put("ttName", MainActivity.ttName);
        data.put("c_no", "0");
        data.put("mode", "pcreate");
        data.put("sID",  getIntent().getExtras().getString("ccID"));
        data.put("c_title", threadTitle);
        data.put("c_text", threadBody);
        data.put("fileID1", file);
        data.put("fileID2", "");
        data.put("fileID3", "");
        data.put("ct", "c398223976");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/make_a_thread", "POST").get();
            threadList.clear();
            getAllThreadAPI();
            executeAPI();

        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thread_option_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search_file);
        searchView = (SearchView) myActionMenuItem.getActionView();
       // changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                final List<ThreadList> filterModeList = filter(threadList,newText);
              /*  adapter.edit()
                        .replaceAll(filterModeList)
                        .commit();*/
                adapter.setFilter(filterModeList);
                return true;
            }
        });
        return true;
    }

    private List<ThreadList> filter (List<ThreadList> pl, String query){
        query = query.toLowerCase();
        final List<ThreadList> filteredModeList = new ArrayList<>();
        for(ThreadList model : pl){
            final String title = model.getThreadTitle().toLowerCase();
            final String body = model.getThreadBody().toLowerCase();
            if(title.contains(query) /*|| body.startsWith(query)*/){
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }

   /* private void changeSearchViewTextColor(View view){
        if(view != null){
            if(view instanceof TextView){
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if(view instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) view;
                for(int i = 0; i<viewGroup.getChildCount(); i++){
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.showMore) {
            return true;
        } else if(id == R.id.moreInfo){
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
       /* Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RC_PHOTO_PICKER);*/
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
                mPostDisplay.setVisibility(View.VISIBLE);
                // Set the Image in ImageView after decoding the String
                mPostDisplay.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                File file = new File(imgDecodableString);
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                RequestBody prefix = RequestBody.create(MediaType.parse("text/plain"), "_coop_");
                retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, prefix);
                req.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String data = null;
                        JSONObject jsonObject;
                        try {
                            data = response.body().string();
                          //  System.out.println(data+ "--------");
                            jsonObject = new JSONObject(data);
                            System.out.println(jsonObject+ "\t" + "--------");
                            hval = jsonObject.getString("hval");
                            System.out.println(hval);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    private void initCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Permission to use Camera", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
            } else {
                initCameraIntent();
            }
        }
    }
    private void initCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File file = getOutputMediafile(2);
      //  mSelectedUri = Uri.fromFile(file);
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectedUri);
        startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA_PERMISSION);
        }
    }
    private File getOutputMediafile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name)
        );
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

     /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image");
            startActivityForResult(intent, requestCode);
        }
    }
*/

}
