package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Splash extends AppCompatActivity {

    String loginuserid,sessionid,uid;
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    String storedEmail;String storedPassword;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        storedEmail=prefs.getString("emaillogin",null);
        storedPassword= prefs.getString("passwordlogin",null);
        if (storedEmail!=null && storedPassword!=null)
            gotoToSigninAPI();


        //getting login details in preferences
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        sessionid = loginpref.getString("sessionid", null);
        uid = loginpref.getString("User-id", null);

        Thread timer = new Thread()
        {
            public void run()
            {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if (loginpref.contains("User-id"))
                    {
                        loginuserid = loginpref.getString("User-id", null);
                        if(loginuserid.equals("")||loginuserid.equals(null))
                        {
                            startActivity(new Intent(Splash.this, Home.class));
                        }
                        else
                        {
                            //startActivity(new Intent(Splash.this, MainActivity.class));
                        }
                    }
                    else
                    {
                        if (storedEmail==null && storedPassword==null)
                            startActivity(new Intent(Splash.this,Login.class));
                    }
                }
            }
        };
        timer.start();

    }


    public void useSignInAPIMethod(View view) {

        //Toast.makeText(Splash.this, prefs.getString("emaillogin",null), Toast.LENGTH_LONG).show();
        //Toast.makeText(Splash.this, prefs.getString("passwordlogin",null), Toast.LENGTH_LONG).show();
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                if (storedEmail==null && storedPassword==null){
                    startActivity(new Intent(Splash.this, Login.class));
                }
                else {
                    params.put("email",prefs.getString("emaillogin",null));
                    params.put("password",prefs.getString("passwordlogin",null));
                    params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");

                }
                return params;
            }
        };
        SingleTon.getInnstance(Splash.this).addREquest(loginAPI);

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

                // Got to home screen if login credentials are correct
                Intent intent=new Intent(Splash.this,Home.class);
                intent.putExtra("response",response);
                startActivity(intent);
            }
            else if (sucessORfailure.contains("true")){

                Toast.makeText(Splash.this, responseWhole.getString("Message").toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
