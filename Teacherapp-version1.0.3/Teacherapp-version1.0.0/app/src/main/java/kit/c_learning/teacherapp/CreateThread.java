package kit.c_learning.teacherapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kit.c_learning.teacherapp.databinding.CreateThreadBinding;
import kit.c_learning.teacherapp.models.ThreadList;

import static android.graphics.Color.TRANSPARENT;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by sokrim on 3/9/2018.
 */

public class CreateThread extends AppCompatActivity {
    private static final String LOG_TAG = "CreateThread";

    private static final int RC_PHOTO_PICKER = 1;

    private DisplayMetrics mDisplayMetrics;
    public CreateThreadBinding mBinding;
    private float mStartX;
    private float mStartY;
    private int mBottomY;
    private int mBottomX;

    private boolean mIsCancel;
    private float mBottomListStartY;
    private boolean resetBottomList;

   // SharedPreferences prefs;
    private RecyclerView recyclerView;
    private CreateThreadAdapter adapter;
    private List<ThreadList> threadList;

    boolean status = false;

    String titleThread_Edt;
    String bodyThread_Edt;
    boolean checkInput = false;

    EditText editTextBody;
    EditText editTextTitle;

    JSONObject mesObj;
    JSONArray mes;
    String ccCharNum;
    public static String[] cTitle, cText, cName,cAlreadyNum, cDate, fContentType1, fContentType2,fContentType3, cNo, fID1, ccID , ttName,
                           cID,stName;

    public  static String threadID, threadNo,bbAnonymous;
    SearchView searchView;

    static Uri mSelectedUri;
    private ImageView mPostDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.create_thread);
        setSupportActionBar(mBinding.toolbar);
       // prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("ccName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SDCARDChecker.checkWeatherExternalStorageAvailableOrNot(CreateThread.this);

        ccCharNum = getIntent().getExtras().getString("ccCharNum");
        if(ccCharNum.equals("null")){
            mBinding.emptyThread.setVisibility(View.VISIBLE);
        }else {
            mBinding.emptyThread.setVisibility(View.GONE);
        }

        setDisplayMetrics();

        mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.sheet)));

        Drawable d = mBinding.bottomListBackground.getBackground();
        final GradientDrawable gd = (GradientDrawable) d;

        gd.setCornerRadius(0f);
        setupRecyclerView();

        editTextTitle = (EditText) findViewById(R.id.thread_Title_edt);
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mBinding.threadTitleEdt.getText().toString().equals("")){
                    mBinding.requiredMsg.setVisibility(VISIBLE);
                    mBinding.requiredMsg.setText("*Please enter the title.");

                }else mBinding.requiredMsg.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       editTextBody = (EditText) findViewById(R.id.thread_Body_edt);
       editTextBody.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if ( mBinding.threadBodyEdt.getText().toString().equals("")){
                   mBinding.requiredMsgBody.setText("*Please enter the body or specify the file.");
                   mBinding.requiredMsgBody.setVisibility(View.VISIBLE);
               }else mBinding.requiredMsgBody.setVisibility(View.GONE);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
       getAllThreadAPI();

       threadID = getIntent().getExtras().getString("ccID");
       bbAnonymous =  getIntent().getExtras().getString("ccAnonymous");
    }


    public boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.v("LOG", "Permission granted");
                return true;
            } else {
                Log.v("LOG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }else {
            Log.v("LOG", "Permission granted");
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thread_option_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search_file);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
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
                adapter.setFilter(filterModeList);
                return true;
            }
        });
        return true;
    }

    private List<ThreadList> filter (List<ThreadList> pl, String query){
        query = query.toLowerCase();
        final List<ThreadList> filteredModeList = new ArrayList<>();
        for(ThreadList model:pl){
            final String title = model.getThreadTitle().toLowerCase();
            final String body = model.getThreadBody().toLowerCase();
            if(title.startsWith(query) /*|| body.startsWith(query)*/){
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }

    private void changeSearchViewTextColor(View view){
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
    }

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

    private void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        threadList = new ArrayList<>();
        adapter = new CreateThreadAdapter(this,threadList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void  executeAPI(){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String, String> data = new ArrayMap<>();
        data.put("ccID", getIntent().getExtras().getString("ccID"));
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        try {
            String myURL = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/thread", "POST").get();
            mesObj = new JSONObject(myURL);
            mes = mesObj.getJSONArray("mes");

            stName = new String[mes.length()];
            cID = new String[mes.length()];
            ccID = new String[mes.length()];
            fID1 = new String[mes.length()];
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

            stName = new String[mes.length()];
            cID = new String[mes.length()];
            ccID = new String[mes.length()];
            fID1 = new String[mes.length()];
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                mSelectedUri = data.getData();
             //   mBinding.postDisplay.setVisibility(View.VISIBLE);
                System.out.println("-----" + mSelectedUri);
              //  mBinding.postDisplay.setImageURI(mSelectedUri);
            }
        }
    }

    public void animate(View view) {
        mBinding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (!mIsCancel) {
            if (mStartX == 0.0f) {
                mStartX = view.getX();
                mStartY = view.getY();

                mBottomX = getBottomFilterXPosition();
                mBottomY = getBottomFilterYPosition();

                mBottomListStartY = mBinding.bottomListBackground.getY();
            }

            final int x = getFinalXPosition();
            final int y = getFinalYPosition();


            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();

                    mBinding.fab.setX(
                            x + (mStartX - x - ((mStartX - x) * v))
                    );

                    mBinding.fab.setY(
                            y + (mStartY - y - ((mStartY - y) * (v * v)))
                    );
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    removeFabBackground();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.fab.animate()
                                    .y(mBottomY)
                                    .setDuration(200)
                                    .start();

                        }
                    },50);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.cancel.setVisibility(VISIBLE);
                            mBinding.cancel.setTranslationX(-(mBottomX - x));

                            mBinding.cancel.animate()
                                    .translationXBy(mBottomX - x)
                                    .setDuration(200)
                                    .start();

                            mBinding.fab.animate()
                                    .x(mBottomX)
                                    .setDuration(200)
                                    .start();

                            mBinding.fab.animate()
                                    .x(mBottomX)
                                    .setDuration(200)
                                    .start();

                            mBinding.sheetTop.setScaleY(0f);
                            mBinding.sheetTop.setVisibility(VISIBLE);

                            mBinding.sheetTop.animate()
                                    .scaleY(1f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                           // mBinding.scroll.setVisibility(VISIBLE);
                                        }
                                    })
                                    .setDuration(200)
                                    .start();
                        }
                    }, 200);

                    if (resetBottomList) {
                        Log.d(LOG_TAG, "onAnimationEnd: reset");
                        resetBottomListBackground();
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.bottomListBackground.animate()
                                    .alpha(1f)
                                    .setDuration(500)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mBinding.fab.setImageResource(R.drawable.cancel);
                                            mBinding.fab.setVisibility(INVISIBLE);
                                            mBinding.fab.setX(mBinding.cancel.getX() - mDisplayMetrics.density * 4);
                                            mBinding.fab.setY(getBottomFilterYPosition());
                                            mBinding.applyFilters.setVisibility(VISIBLE);
                                        }
                                    })
                                    .start();
                        }
                    }, 400);

                    revealFilterSheet(y);
                }
            });
            animator.start();
        } else {
            mBinding.fab.setImageResource(R.drawable.ic_edit_black);
            mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.sheet)));
            mIsCancel = false;
        }
    }

    private void checkCondition(){
        if (titleThread_Edt.equals("")){
            mBinding.requiredMsg.setVisibility(VISIBLE);
            mBinding.requiredMsg.setText("*Please enter the title.");
            checkInput = false;
        }
        if (bodyThread_Edt.equals("")){
            mBinding.requiredMsgBody.setVisibility(VISIBLE);
            mBinding.requiredMsgBody.setText("*Please enter the body or specify the file.");
            checkInput = false;
        }
        if (titleThread_Edt.equals("") && bodyThread_Edt.equals("")){
            mBinding.requiredMsg.setVisibility(VISIBLE);
            mBinding.requiredMsg.setText("*Please enter the title.");
            mBinding.requiredMsgBody.setVisibility(VISIBLE);
            mBinding.requiredMsgBody.setText("*Please enter the body or specify the file.");
            checkInput = false;
        }
        else if(!titleThread_Edt.equals("") && !bodyThread_Edt.equals("")) {
            mBinding.requiredMsg.setVisibility(GONE);
            mBinding.requiredMsgBody.setVisibility(GONE);
            checkInput = true;
        }
    }

    public void acceptFilters(View view) {
        mBinding.emptyThread.setVisibility(View.GONE);
        titleThread_Edt = mBinding.threadTitleEdt.getText().toString();
        bodyThread_Edt = mBinding.threadBodyEdt.getText().toString();
        checkCondition();
        if(checkInput == true){
            System.out.println("++++++++++++++++++++++++" + titleThread_Edt);
            System.out.println("++++++++++++++++++++++++" + bodyThread_Edt);

            ArrayMap<String,String> header = new ArrayMap<>();
            ArrayMap<String,String> data = new ArrayMap<>();
            data.put("ttID", MainActivity.ttID);
            data.put("ttName", MainActivity.ttName);
            data.put("ttMail", MainActivity.ttEmail);
            data.put("mode", "pcreate");
            data.put("ccID",  getIntent().getExtras().getString("ccID"));
            data.put("c_title", titleThread_Edt);
            data.put("c_text", bodyThread_Edt);
            HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
            try {
                String myUrl = myHttp.execute("https://kit.c-learning.jp/t/ajax/coop/res", "POST").get();
                JSONObject jsonObject = new JSONObject(myUrl);
                System.out.println("]]]]]]]]]]]]]]]]]]" + jsonObject);
                prepareThread2(MainActivity.ttName,titleThread_Edt,bodyThread_Edt,"","");
               // executeAPI();

            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
          // executeAPI();
        }
        createThread();
    }

    private void createThread(){
        mBinding.fab.setVisibility(VISIBLE);
        mBinding.list.setVisibility(INVISIBLE);
        mBinding.scroll.setVisibility(INVISIBLE);

        mIsCancel = true;
        final int x = getFinalXPosition();
        final int y = getFinalYPosition();


        mBinding.applyFilters.setVisibility(INVISIBLE);
        mBinding.cancel.setVisibility(INVISIBLE);

        final int startX = (int) mBinding.fab.getX();
        final int startY = (int) mBinding.fab.getY();

        mBinding.sheetTop.setVisibility(INVISIBLE);
        Animator reveal = ViewAnimationUtils.createCircularReveal(
                mBinding.reveal,
                mDisplayMetrics.widthPixels / 2,
                (int) (y - mBinding.reveal.getY()) + getFabSize() / 2,
                mBinding.reveal.getHeight() * .5f,
                getFabSize() / 2);

        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.reveal.setVisibility(INVISIBLE);
                mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(CreateThread
                        .this, R.color.colorAccent)));
                mBinding.fab.setElevation(mDisplayMetrics.density * 4);

            }
        });
        reveal.start();

        animateBottomSheet();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();

                mBinding.fab.setX(
                        x - (x - startX - ((x - startX) * v))
                );

                mBinding.fab.setY(
                        y + (startY - y - ((startY - y) * (v * v)))
                );


            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.fab.animate()
                        .rotationBy(360)
                        .setDuration(1000)
                        .start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        returnFabToInitialPosition();
                        mBinding.bottomListBackground.setVisibility(INVISIBLE);
                    }
                }, 1000);
            }
        });
        animator.start();
        mBinding.threadTitleEdt.setText("");
        mBinding.threadBodyEdt.setText("");
    }

    public void cancelCreateThread(View view) {
        mBinding.fab.setVisibility(VISIBLE);
        mBinding.list.setVisibility(INVISIBLE);
        mBinding.scroll.setVisibility(INVISIBLE);

        mBinding.threadTitleEdt.setText("");
        mBinding.threadBodyEdt.setText("");

        mIsCancel = true;
        final int x = getFinalXPosition();
        final int y = getFinalYPosition();


        mBinding.applyFilters.setVisibility(INVISIBLE);
        mBinding.cancel.setVisibility(INVISIBLE);

        final int startX = (int) mBinding.fab.getX();
        final int startY = (int) mBinding.fab.getY();

        mBinding.sheetTop.setVisibility(INVISIBLE);
        Animator reveal = ViewAnimationUtils.createCircularReveal(
                mBinding.reveal,
                mDisplayMetrics.widthPixels / 2,
                (int) (y - mBinding.reveal.getY()) + getFabSize() / 2,
                mBinding.reveal.getHeight() * .5f,
                getFabSize() / 2);

        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.reveal.setVisibility(INVISIBLE);
                mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(CreateThread
                        .this, R.color.colorAccent)));
                mBinding.fab.setElevation(mDisplayMetrics.density * 4);

            }
        });
        reveal.start();

        animateBottomSheet();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();

                mBinding.fab.setX(
                        x - (x - startX - ((x - startX) * v))
                );

                mBinding.fab.setY(
                        y + (startY - y - ((startY - y) * (v * v)))
                );


            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.fab.animate()
                        .rotationBy(360)
                        .setDuration(1000)
                        .start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        returnFabToInitialPosition();
                        mBinding.bottomListBackground.setVisibility(INVISIBLE);
                    }
                }, 1000);
            }
        });
        animator.start();
    }

    private void animateBottomSheet() {
        Drawable d = mBinding.bottomListBackground.getBackground();
        final GradientDrawable gd = (GradientDrawable) d;


        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                mBinding.bottomListBackground.getLayoutParams();

        final int startWidth = mBinding.bottomListBackground.getWidth();
        final int startHeight = mBinding.bottomListBackground.getHeight();
        final int startY = (int) mBinding.bottomListBackground.getY();


        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                gd.setCornerRadius(mDisplayMetrics.density * 50 * v);

                int i = (int) (startWidth - (startWidth - getFabSize()) * v);
                params.width = i;
                params.height = (int) (startHeight - (startHeight - getFabSize()) * v);
                mBinding.bottomListBackground.setY(getFinalYPosition() + (startY
                        - getFinalYPosition()) - ((startY - getFinalYPosition()) * v));

                mBinding.bottomListBackground.requestLayout();
            }
        });
        animator.start();
    }

    private void returnFabToInitialPosition() {
        final int x = getFinalXPosition();
        final int y = getFinalYPosition();
        resetBottomList = true;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();

                mBinding.fab.setX(
                        x + ((mStartX - x) * v)
                );

                mBinding.fab.setY(
                        (float) (y + (mStartY - y) * (Math.pow(v, .5f)))
                );
            }
        });
        animator.start();
    }

    public int getStatusBarHeight() {
        return (int) (mDisplayMetrics.density * 24);
    }

    private void resetBottomListBackground() {
        resetBottomList = false;
        mBinding.bottomListBackground.setVisibility(VISIBLE);
        Drawable d = mBinding.bottomListBackground.getBackground();
        final GradientDrawable gd = (GradientDrawable) d;
        mBinding.bottomListBackground.setAlpha(0f);
        gd.setCornerRadius(0f);


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBinding.bottomListBackground.getLayoutParams();
        params.width = -1;
        params.height = (int) (mDisplayMetrics.density * 64);
        mBinding.bottomListBackground.setY(mBottomListStartY + mDisplayMetrics.density * 8);
        mBinding.bottomListBackground.requestLayout();
    }

    private int getBottomFilterYPosition() {
        return (int) (
                mBinding.applyFilters.getY()
                        + (mDisplayMetrics.heightPixels - getStatusBarHeight() - mDisplayMetrics.density * 64)
                        - mDisplayMetrics.density * 4);
    }

    private int getBottomFilterXPosition() {
        return (int) (
                mBinding.applyFilters.getX()
                        + mDisplayMetrics.widthPixels / 2
                        - mDisplayMetrics.density * 4);
    }

    private void removeFabBackground() {
        mBinding.fab.setBackgroundTintList(ColorStateList.valueOf(TRANSPARENT));

        mBinding.fab.setElevation(0f);
    }

    private void revealFilterSheet(int y) {
        mBinding.reveal.setVisibility(VISIBLE);

        Animator a = ViewAnimationUtils.createCircularReveal(
                mBinding.reveal,
                mDisplayMetrics.widthPixels / 2,
                (int) (y - mBinding.reveal.getY()) + getFabSize() / 2,
                getFabSize() / 2,
                mBinding.reveal.getHeight() * .7f);
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.list.setVisibility(VISIBLE);
            }
        });
        a.start();
    }

    public int getFinalXPosition() {
        return mDisplayMetrics.widthPixels / 2 - getFabSize() / 2;
    }

    public int getFinalYPosition() {
        int marginFromBottom = getFinalYPositionFromBottom();
        return mDisplayMetrics.heightPixels - marginFromBottom + getFabSize() / 2;
    }

    public void setDisplayMetrics() {
        mDisplayMetrics = getResources().getDisplayMetrics();
    }

    public int getFinalYPositionFromBottom() {
        return (int) (mDisplayMetrics.density * 250);
    }

    public int getFabSize() {
        return (int) (mDisplayMetrics.density * 56);
    }
}
