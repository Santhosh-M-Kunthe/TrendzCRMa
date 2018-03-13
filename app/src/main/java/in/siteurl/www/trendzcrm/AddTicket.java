package in.siteurl.www.trendzcrm;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class AddTicket extends AppCompatActivity {


    SharedPreferences loginpref;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    MaterialEditText sub,msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        sub=findViewById(R.id.sub);
        msg=findViewById(R.id.msg);
    }

    public void addTicketReqst(View view) {
        StringRequest addticketreqst=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/addticket",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().contains("false"))
                        {Toast.makeText(AddTicket.this, "Your ticket has been added successfully. . .", Toast.LENGTH_SHORT).show();
                        finish();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTicket.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params=new HashMap<>();
                params.put("customer_id", prefs.getString("customer_id","you got nothing"));
                params.put("sid", prefs.getString("sid","you got nothing"));
                Log.d("opopop",prefs.getString("sid","you got nothing"));
                params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                params.put("unit_id",getIntent().getExtras().getString("unitid"));
                params.put("subject",sub.getText().toString());
                params.put("message",msg.getText().toString());
                //Toast.makeText(AddTicket.this, params.toString(), Toast.LENGTH_SHORT).show();
                return params;

            }
        };
        SingleTon.getInnstance(AddTicket.this).addREquest(addticketreqst)
        ;
    }
}
