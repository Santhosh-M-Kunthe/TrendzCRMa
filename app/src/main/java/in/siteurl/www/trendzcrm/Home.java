package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    ListView ls;FlatListAdapter flatListAdapter;ArrayList<FlatContents> arrayList=new ArrayList<>();
    String wholeResponcepassedToHome="";
    RecyclerView hsv;
    ArrayList<DocumentsContent> PicsArrayList=new ArrayList<>();
    ViewPager offerviewpager;
    ArrayList<ViewPageOfferContentds> viewPageOfferContentdsArrayList=new ArrayList<>();
    SharedPreferences loginpref;
    SharedPreferences.Editor editor;

    Boolean run=true;
    String[] offerImageURLlist;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.chngpswd:
                Intent intent2=new Intent(Home.this,ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3=new Intent(Home.this,Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //================================================Offer view pager uncommetn these
        getResponseParseItAndgetArraylist();

       /* ViewPager offerviewpager=(ViewPager) findViewById(R.id.offerviewpager);
        //viewPageOfferContentdsArrayList=getResponseParseItAndgetArraylist();
        ViewPageOfferAdapter viewPageOfferAdapter=new ViewPageOfferAdapter(Home.this,offerImageURLlist);
        Toast.makeText(Home.this, "232323232\n"+offerImageURLlist.toString(), Toast.LENGTH_SHORT).show();
        offerviewpager.setAdapter(viewPageOfferAdapter);
       */ //================================================Offer view pager
        loginpref = getApplicationContext().getSharedPreferences("LoginPref", MODE_PRIVATE);
        editor = loginpref.edit();

        wholeResponcepassedToHome=getIntent().getExtras().getString("response");
        //Toast.makeText(Home.this, "▻ Click on My Documents to view your documents◅ ", Toast.LENGTH_SHORT).show();
        // TECHSUPPORT SNACKBAR FLOATING BUTTON'
        FloatingActionButton techsup=findViewById(R.id.techsupport);
        techsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
                Intent goToTechSuppo=new Intent(Home.this,TechSupport.class);
                startActivity(goToTechSuppo);
            }
        });


        // FLATS/UNITS LISTVIEW
        ls=(ListView) findViewById(R.id.myunitslistview);
        arrayList=getArrayList();
        flatListAdapter=new FlatListAdapter(Home.this,R.layout.flat_contents,arrayList);
        ls.setAdapter(flatListAdapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              /*  String flatDetailsString=
                        "Unit name:"+arrayList.get(i).getUnitName()+"\n"+
                        "Value:    "+arrayList.get(i).getValue() +"\n"+
                        "Size:     "+arrayList.get(i).getSize()+"\n"+
                        "Sale value:"+arrayList.get(i).getSaleValue() +"\n"+
                        "Balance amount:"+arrayList.get(i).getBalanceAmaount() +"\n"+
                        "Recieved amount:"+arrayList.get(i).getRecievedAmount() +"\n"+
                        "Floor:          "+arrayList.get(i).getFloor() +"\n"+
                        "Block ID:       "+arrayList.get(i).getBlockID() +"\n"+
                        "Project ID:     "+arrayList.get(i).getProjectId() +"\n"+
                        "Status of unit: "+arrayList.get(i).getUnitStatus() +"\n";*/

              String[] flatDetailsString={arrayList.get(i).getUnitName(),arrayList.get(i).getValue(),
                      arrayList.get(i).getSize(),arrayList.get(i).getSaleValue(),arrayList.get(i).getBalanceAmaount()
                      ,arrayList.get(i).getRecievedAmount(),arrayList.get(i).getFloor(),arrayList.get(i).getProjectId()
                      ,arrayList.get(i).getBlockID(),arrayList.get(i).getUnitStatus()};
                /*
Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
intent.putExtra("EXTRA_SESSION_ID", sessionId);
startActivity(intent);
Access that intent on next activity

String s = getIntent().getStringExtra("EXTRA_SESSION_ID");*/

                //intent to start Flatdetails
                Intent flatDet=new Intent(Home.this,FlatDetails.class);
                flatDet.putExtra("flatDetails",flatDetailsString);
                flatDet.putExtra("flatposition",i);
                flatDet.putExtra("unitid",arrayList.get(i).getUnitId());
                flatDet.putExtra("responseFromHome",wholeResponcepassedToHome);
                startActivity(flatDet);
            }
        });

        // DOCUMENTS GRIDVIEW number based on screen orientation
        hsv=(RecyclerView) findViewById(R.id.hsv);
        if (Home.this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            hsv.setLayoutManager(new GridLayoutManager(Home.this,1));
        if (Home.this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            hsv.setLayoutManager(new GridLayoutManager(Home.this,1));
            getSupportActionBar().hide();
        }
        getArrayListBottomHome();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw=new DocumentsAdapterRcyclrvw(Home.this,PicsArrayList);
        hsv.setAdapter(documentsAdapterRcyclrvw);
    }

//    private ArrayList<ViewPageOfferContentds> getResponseParseItAndgetArraylist() {
        private void getResponseParseItAndgetArraylist() {

        //final ArrayList<ViewPageOfferContentds> viewPageOfferContentdsArrayListtemp=new ArrayList<>();

        StringRequest stringRequestforoffer=new StringRequest(Request.Method.POST, "http://apartmentsmysore.in/crm/alloffers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject Wholeoffer=new JSONObject(response);
                           // Toast.makeText(Home.this, Wholeoffer.toString(7), Toast.LENGTH_SHORT).show();
                            JSONArray offerdateArray=Wholeoffer.getJSONArray("data");
                            offerImageURLlist=new String[offerdateArray.length()];

                            for (int o=0;o<offerdateArray.length();o++){
                                JSONObject everySingleOffer=offerdateArray.getJSONObject(o);
                               // Toast.makeText(Home.this, "111111111\n"+everySingleOffer.toString(), Toast.LENGTH_SHORT).show();
                                //viewPageOfferContentdsArrayListtemp.add(new ViewPageOfferContentds(everySingleOffer.getString("offer_id"),everySingleOffer.getString("offer_name"),everySingleOffer.getString("description"),everySingleOffer.getString("offer_image")));
                                if (o%2==0)//0000000000000000000000000000000000000000 comment these two lines
                                    offerImageURLlist[o]="http://media.jpegmini.com/images/JPEGmini_mac_300.jpg";
                                else
                                    offerImageURLlist[o]=everySingleOffer.getString("offer_image");
                                //Toast.makeText(Home.this, "2222222222222\n"+offerImageURLlist[o], Toast.LENGTH_SHORT).show();

                            }
                           offerviewpager=(ViewPager) findViewById(R.id.offerviewpager);
                            //viewPageOfferContentdsArrayList=getResponseParseItAndgetArraylist();
                            ViewPageOfferAdapter viewPageOfferAdapter=new ViewPageOfferAdapter(Home.this,offerImageURLlist);
                           // Toast.makeText(Home.this, "232323232\n"+offerImageURLlist.toString(), Toast.LENGTH_SHORT).show();
                            offerviewpager.setAdapter(viewPageOfferAdapter);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (offerviewpager.getCurrentItem() == 6) {
                                        offerviewpager.setCurrentItem(0);
                                    }
                                    int x=offerviewpager.getCurrentItem();
                                    // Toast.makeText(Home.this, "scrolled", Toast.LENGTH_SHORT).show();
                                    offerviewpager.setCurrentItem(++x, true);
                                }
                            };

                            final Timer timer = new Timer(); // This will create a new Thread
                            timer .schedule(new TimerTask() { // task to be scheduled

                                @Override
                                public void run() {
                                    if (run)
                                        handler.post(Update);
                                    else {
                                        timer.cancel();
                                        timer.purge();
                                    }
                                }
                            }, 90, 5000);


                            offerviewpager.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    run=false;
                                    return false;
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(Home.this, "No offers found", Toast.LENGTH_SHORT).show();
                            RelativeLayout rl = (RelativeLayout) findViewById(R.id.fornooffer);

                            // Create a TextView programmatically.
                            TextView tv = new TextView(getApplicationContext());

                            // Create a LayoutParams for TextView
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

                            lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                            tv.setLayoutParams(lp);
                            tv.setText("No offers available");
                            rl.addView(tv);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonObject;
                HashMap<String,String> params = null;
                try {
                    jsonObject=new JSONObject(wholeResponcepassedToHome);
                
                params=new HashMap<>();
                params.put("admin_id","1");
                params.put("sid",jsonObject.getString("sid"));
                params.put("api_key","4c0c39c32f8339ab25fd7afb05eccf0efd1dba49");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "0909090909", Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };
        SingleTon.getInnstance(Home.this).addREquest(stringRequestforoffer);
       // return viewPageOfferContentdsArrayListtemp;
    }

    private void getArrayListBottomHome() {
        //ArrayList<DocumentsContent> arrayList=new ArrayList<>();
        /*for (int i=0;i<9;i++)
            arrayList.add(new DocumentsContent(String.valueOf(i)));
        return arrayList;*/

        // ArrayList<DocumentsContent> PicarrayList=new ArrayList<>();
        //ArrayList<DocumentsContent> DOcarrayList=new ArrayList<>();

        /*for (int i=0;i<9;i++){
            arrayList.add(new DocumentsContent(String.valueOf(i)));
        }*/

        // parse JSON of Response
        JSONObject docList= null;
        try {
            docList = new JSONObject(wholeResponcepassedToHome);
            JSONArray UnitBlockProjDocuments=docList.getJSONArray("Units Documents");
            for (int i=0;i<UnitBlockProjDocuments.length();i++){
                JSONObject oneDoc=UnitBlockProjDocuments.getJSONObject(i);
                JSONArray unitQ=oneDoc.getJSONArray("unit_document");
                getDocsPathTypeNameID(unitQ);
                JSONArray blockQ=oneDoc.getJSONArray("block_document");
                getDocsPathTypeNameID(blockQ);
                JSONArray projQ=oneDoc.getJSONArray("project_document");
                getDocsPathTypeNameID(projQ);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }/*
        PicsArrayList=PicarrayList;
        DocsArrayList=DOcarrayList;
*/

    }


    private void getDocsPathTypeNameID(JSONArray unitQ) {
        //load only image to gridview
        for (int i=0;i<unitQ.length();i++)
            try {
                JSONObject singleDocQ=unitQ.getJSONObject(i);
                if (!(singleDocQ.getString("doc_path").contains(".pdf")))
                    PicsArrayList.add(new DocumentsContent(singleDocQ.getString("name"),singleDocQ.getString("doc_path"),singleDocQ.getString("association_id"),singleDocQ.getString("table_name"),singleDocQ.getString("doc_type")));

        } catch (JSONException e) {

               e.printStackTrace();
            }
    }

    private ArrayList<FlatContents> getArrayList() {
        ArrayList<FlatContents> ar=new ArrayList<>();
      /*  for (int i=1;i<=9;i++){
            ar.add(new FlatContents(String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i),String.valueOf(i)));

        }*/
        try {
            JSONObject flatList=new JSONObject(wholeResponcepassedToHome);
            JSONArray listOfUnits=flatList.getJSONArray("List of units");
            editor.putString("sid",flatList.getString("sid"));
            editor.commit();

            JSONArray UnitBlockProjDocuments=flatList.getJSONArray("Units Documents");

            for (int i=0;i<listOfUnits.length();i++){
                JSONObject oneFlat=listOfUnits.getJSONObject(i);
                JSONObject oneDoc=UnitBlockProjDocuments.getJSONObject(i);
                JSONArray blockQ=oneDoc.getJSONArray("block_name");
                JSONObject oneBlockName=blockQ.getJSONObject(0);
                JSONArray projQ=oneDoc.getJSONArray("project_name");
                JSONObject oneProjName=projQ.getJSONObject(0);

                editor.putString("customer_id",oneFlat.getString("customer_id"));
                editor.commit();


                ar.add(new FlatContents(oneFlat.getString("unit_id"),oneFlat.getString("unit_name"),
                        oneBlockName.getString("block_name"),oneProjName.getString("name"),oneFlat.getString("floor"),
                        oneFlat.getString("value"),
                        oneFlat.getString("customer_id"),oneFlat.getString("sale_value"),oneFlat.getString("received_amount"),
                        oneFlat.getString("balance_amount"),
                        oneFlat.getString("size"),oneFlat.getString("unit_status"),oneFlat.getString("created_at")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ar;
    }

    public void GoToMyDocsClassMethod(View view) {

        //start MyDocuments intent
       Intent goToDocs=new Intent(Home.this,MyDocuments.class);
       goToDocs.putExtra("responseFromHome",wholeResponcepassedToHome);
       goToDocs.putExtra("flatposition",351);
        //Intent goToDocs=new Intent(Home.this,TestActivity.class);
        startActivity(goToDocs);


    }
}
