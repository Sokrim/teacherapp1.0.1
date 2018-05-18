package kit.c_learning.teacherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{


    EditText email, pass;
    TextView emailError, passError, signUpTextView;

    static boolean isValidEmail;
    static boolean isValidPass;


    private TextWatcher emailWatcher;
    private TextWatcher passWatcher;

    JSONObject jsonObject = null, success = null, loginData= null;

   static String ttID = null , ttEmail = null, res, err, msg, teacher, ttName = null, ttLastLoginDate = null, ttHash = null;

    String user_Input_Email, user_Input_Password;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = (EditText) findViewById(R.id.email_input);
        pass = (EditText) findViewById(R.id.password_input);

        emailError = (TextView) findViewById(R.id.email_err);
        passError = (TextView) findViewById(R.id.pass_err);
        emailError.setVisibility(View.INVISIBLE);
        passError.setVisibility(View.INVISIBLE);

        email.setText("loemkimhak@gmail.com");
        pass.setText("kitap2017");

        signUpTextView = findViewById(R.id.textViewSignup);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
               // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        emailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (email.getText().toString().equals("")){
                    emailError.setText("*Please enter your email address.");
                    emailError.setVisibility(View.VISIBLE);
                }else emailError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        passWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pass.getText().toString().equals("")){
                    passError.setText("*Please enter your email address.");
                    passError.setVisibility(View.VISIBLE);
                }else passError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

    }

    private void checkInput(){
          if (user_Input_Email.equals("")){
              AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
              mBuilder.setTitle("Message");
              mBuilder.setMessage("*Please enter your email address or Login ID.");
              mBuilder.setCancelable(false);
              mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                  }
              });
              AlertDialog alertDialog = mBuilder.create();
              alertDialog.show();
              isValidEmail = false;
            }else {
                if (user_Input_Email.equals(ttEmail) || user_Input_Email.equals(ttID)){
                    isValidEmail = true;
                }else{
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    mBuilder.setTitle("Message");
                    mBuilder.setMessage("*This email address was not found.");
                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = mBuilder.create();
                    alertDialog.show();
                    isValidEmail = false;
                }
            }
            if (user_Input_Password.equals("")){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Message");
                mBuilder.setMessage("*Please enter your password..");
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
                isValidPass = false;

            }else if (user_Input_Password.equals(user_Input_Password)){
                    isValidPass = true;

            }

    }


    //check internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //Login request API
    private  void loginAPI(String email, String password){
        ArrayMap<String,String> header = new ArrayMap<>();
        ArrayMap<String,String> data = new ArrayMap<>();
        data.put("login", email);
        data.put("pass", password);
        data.put("os", "android");
        data.put("token", "ff");
        data.put("did", "gg");
        HttpRequestAsync myHttp = new HttpRequestAsync(header,data);
        if(isNetworkAvailable()){
            try {
                String text = myHttp.execute("https://kit.c-learning.jp/t/app/login.json", "POST").get();
                jsonObject = new JSONObject(text);
                err = jsonObject.getString("err");
                if (err.equals("-2")){
                    msg = jsonObject.getString("msg");
                }
                else {
                    res = jsonObject.getString("res");
                    success = new JSONObject(res);
                    teacher = success.getString("Teacher");
                    loginData = new JSONObject(teacher);
                    ttID = loginData.getString("ttID");
                    ttEmail = loginData.getString("ttMail");
                    ttName = loginData.getString("ttName");
                    ttLastLoginDate = loginData.getString("ttLastLoginDate");
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }
    public void login(View view) {
        user_Input_Email = email.getText().toString();
        user_Input_Password = pass.getText().toString();
        if(isNetworkAvailable()) {
            loginAPI(user_Input_Email,user_Input_Password);
            if (user_Input_Email.equals("") || user_Input_Password.equals("")){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Message");
                mBuilder.setMessage("Please enter your email address or password.");
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
            else {
                switch (err){
                    case "-2" :
                        System.out.println("333333333333333333333333333333333333" + err);
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        mBuilder.setTitle("Message");
                        mBuilder.setMessage(msg);
                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = mBuilder.create();
                        alertDialog.show();
                        break;
                    case "0":
                        BackgroundTask task = new BackgroundTask(MainActivity.this);
                        task.execute();
                        Intent intent = new Intent(MainActivity.this,MyCourses.class);
                        prefs = PreferenceManager.getDefaultSharedPreferences(this);
                        prefEditor = prefs.edit();
                        user = prefs.getString("ttMail", ttEmail);
                        intent.putExtra("ttName",ttName);
                        intent.putExtra("ttMail",ttEmail);
                        intent.putExtra("ttLastLoginDate",ttLastLoginDate);
                        prefEditor.apply();
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        }else {
            Toast.makeText(getApplication(),"No Internet Connection!", Toast.LENGTH_LONG).show();
        }
    }


    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public BackgroundTask(MainActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading......");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
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

    boolean isValidEmail(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
