package in.siteurl.www.trendzcrm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Offers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        Toast.makeText(Offers.this, "Image number of image you clicked was", Toast.LENGTH_LONG).show();
        Toast.makeText(Offers.this,String.valueOf( getIntent().getExtras().getInt("itno")), Toast.LENGTH_SHORT).show();

        StringRequest getOffersInOffers=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/alloffers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject Wholeoffer=new JSONObject(response);
                            // Toast.makeText(Home.this, Wholeoffer.toString(7), Toast.LENGTH_SHORT).show();
                            JSONArray offerdateArray=Wholeoffer.getJSONArray("data");

                            for (int o=0;o<offerdateArray.length();o++) {
                                JSONObject everySingleOffer = offerdateArray.getJSONObject(o);
                                ((TextView)findViewById(R.id.offerName)).setText(everySingleOffer.getString("offer_name"));
                                ((TextView)findViewById(R.id.offerDesc)).setText(everySingleOffer.getString("description"));
                                String urrl;
                                if (o%2==0)//0000000000000000000000000000000000000000 comment these two lines
                                    urrl="http://media.jpegmini.com/images/JPEGmini_mac_300.jpg";
                                else
                                    urrl=everySingleOffer.getString("offer_image");

                                Glide.with(Offers.this).load(urrl).thumbnail(0.5f).into(((ImageView)findViewById(R.id.oggerImageview)));

                            }
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }}
                    , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params=new HashMap<>();
                params.put("admin_id","1");
                params.put("sid","");
                params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                return params;
            }
        };
        SingleTon.getInnstance(Offers.this).addREquest(getOffersInOffers);
    }

    public void finishh(View view) {
        finish();
    }
}
