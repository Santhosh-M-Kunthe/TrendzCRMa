package in.siteurl.www.trendzcrm;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {


    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    MaterialEditText cp,np1,np2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
/*
        Toast.makeText(Logout.this, prefs.getString("sid","you got nothing"), Toast.LENGTH_LONG).show();
        Toast.makeText(Logout.this, prefs.getString("customer_id","you got nothing"), Toast.LENGTH_LONG).show();
*/

cp=(MaterialEditText)findViewById(R.id.curpswd);
np1=(MaterialEditText)findViewById(R.id.newPswd1);
np2=(MaterialEditText) findViewById(R.id.newPswd2);


    }

    private void changePswd() {
        StringRequest changePswdReqst=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/changepassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject chngpswdrspns=new JSONObject(response);
                            Toast.makeText(ChangePassword.this, chngpswdrspns.getString("Message").toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePassword.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params=new HashMap<>();
                params.put("customer_id", prefs.getString("customer_id","you got nothing"));
                params.put("sid", prefs.getString("sid","you got nothing"));
                params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                params.put("currentpassword",cp.getText().toString());
                if (np1.getText().toString().contains(np2.getText().toString()))
                    params.put("password",np1.getText().toString());
                return params;
            }
        };
        SingleTon.getInnstance(ChangePassword.this).addREquest(changePswdReqst);
    }

    public void changePswdq(View view) {
        if (np1.getText().toString().contains(np2.getText().toString()))
            changePswd();
        else {
            np1.setText("");
            np2.setText("");
            Toast.makeText(ChangePassword.this, "Enter new password in both second and third field. . .", Toast.LENGTH_SHORT).show();
        }
        //changePswd();
    }
}
