package in.siteurl.www.trendzcrm;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    EditText loginED,passwordED;
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    String storedEmail;String storedPassword;
    SharedPreferences prefs;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new SpotsDialog(Login.this,"Signing you in. . .");

        loginED=findViewById(R.id.edloginemail);
        passwordED=findViewById(R.id.edloginpassword);
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        storedEmail=prefs.getString("emaillogin",null);
        storedPassword= prefs.getString("passwordlogin",null);
        if (storedEmail!=null && storedPassword!=null)
            gotoToSigninAPI();



        FloatingActionButton info=findViewById(R.id.loginquesmark);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar snack=Snackbar.make(view,"Enter Email and Password sent by Admin",Snackbar.LENGTH_LONG);
                CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                snack.show();*/

                //To show snackbar
                CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordsumney);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter Email and Password sent by Admin", Snackbar.LENGTH_LONG);
                View viewa = snackbar.getView();

                CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)viewa.getLayoutParams();
                params.gravity = Gravity.TOP;
                viewa.setLayoutParams(params);
                snackbar.show();

            }
        });
        CheckBox showPassword=findViewById(R.id.checkboxtoshowpassword);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //show or hide password based on on checked
                if (!b)
                    passwordED.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else passwordED.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });
    }

    public void useForgotPasswordAPIMethod(View view) {
        if (loginED.getText().toString()==null||loginED.getText().toString().length()<3)
            Toast.makeText(Login.this, "Kndly enter a valid email", Toast.LENGTH_SHORT).show();

        if (!(loginED.getText().toString().contains("@")&&loginED.getText().toString().contains(".")))
            Toast.makeText(Login.this, "Enter valid email", Toast.LENGTH_LONG).show();
            StringRequest changePswd=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/forgot",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                    params.put("email",loginED.getText().toString());
                    params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                return params;
            }
        };
        SingleTon.getInnstance(Login.this).addREquest(changePswd);
    }

    public void useSignInAPIMethod(View view) {

        dialog.show();
      //  Toast.makeText(Login.this, prefs.getString("emaillogin",null), Toast.LENGTH_LONG).show();
        //Toast.makeText(Login.this, prefs.getString("passwordlogin",null), Toast.LENGTH_LONG).show();
        gotoToSigninAPI();
   }

    private void gotoToSigninAPI() {


        //call API
        StringRequest loginAPI=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/userlogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        parseJSON(response);
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                if (storedEmail==null && storedPassword==null){
                    params.put("email",loginED.getText().toString());
                    params.put("password",passwordED.getText().toString());
                    params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                }
                else {
                    params.put("email",prefs.getString("emaillogin",null));
                    params.put("password",prefs.getString("passwordlogin",null));
                    params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                }
                return params;
            }
        };
        SingleTon.getInnstance(Login.this).addREquest(loginAPI);

        loginAPI.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        */

    }

    private void parseJSON(String response) {
        try {
            JSONObject responseWhole=new JSONObject(response);
            String sucessORfailure=responseWhole.getString("Error");
            if (sucessORfailure.contains("false"))
            {
               if (storedEmail==null || storedPassword==null)
               {
                   editor.putString("emaillogin",loginED.getText().toString());
                   editor.putString("passwordlogin",passwordED.getText().toString());
                   editor.commit();
               }
                // Got to home screen if login credentials are correct
                Intent intent=new Intent(Login.this,Home.class);
                intent.putExtra("response",response);
                startActivity(intent);
            }
            else if (sucessORfailure.contains("true")){

                Toast.makeText(Login.this, responseWhole.getString("Message").toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
