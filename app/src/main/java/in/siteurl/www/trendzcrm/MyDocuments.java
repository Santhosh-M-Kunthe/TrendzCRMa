package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyDocuments extends AppCompatActivity {

    ArrayList<DocumentsContent> PicsArrayList=new ArrayList<>();
    ArrayList<DocumentsContent> DocsArrayList=new ArrayList<>();
    int total,timePassed,tp2=100;ProgressBar bar;
    String responceFromHome="";
    RecyclerView docPicsRecyclerView;RecyclerView docDocsRecyclerView;
    TextView pictv,doctv;

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
                Intent intent2=new Intent(MyDocuments.this,ChangePassword.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent3=new Intent(MyDocuments.this,Logout.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_documents);


        //get response from home
        responceFromHome=getIntent().getExtras().getString("responseFromHome");
        pictv   = new TextView(getApplicationContext());
        doctv = new TextView(getApplicationContext());

        //=====================================================
        bar = (ProgressBar) findViewById(R.id.progress);
       /* if(MyDocuments.this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            bar.getLayoutParams().height=20;
            bar.getLayoutParams().width=20;

        }*/

        final CheckBox flatcb=findViewById(R.id.flatchkbox);
        final CheckBox blockcb=findViewById(R.id.blockchkbox);
        final CheckBox projcb=findViewById(R.id.projectchkbox);
        final CheckBox mydoccb=findViewById(R.id.mydocschkbox);

        flatcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    blockcb.setChecked(false);
                    projcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("units");
                }
                else
                    putAll();
            }
        });

        blockcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {

                    flatcb.setChecked(false);
                    projcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("Blocks");
                }
                else
                    putAll();
            }
        });

        projcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    blockcb.setChecked(false);
                    flatcb.setChecked(false);
                    mydoccb.setChecked(false);
                    filterFor("projects");

                }
                else
                    putAll();
            }
        });

        mydoccb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    blockcb.setChecked(false);
                    flatcb.setChecked(false);
                    projcb.setChecked(false);
                    filterFor("mydocs");

                }
                else
                    putAll();
            }
        });


        //==============================================
        bar.setProgress(total);
        int oneMin= 5* 1000;
        //tp2=timePassed;
        CountDownTimer cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((timePassed/ 60) * 100);
                bar.setProgress(total);
            }

            public void onFinish() {
                bar.setVisibility(View.GONE);
            }
        }.start();
        //===============================================
        docPicsRecyclerView=(RecyclerView)findViewById(R.id.picsDocumentsREcyclerView);
        docDocsRecyclerView=(RecyclerView)findViewById(R.id.docsDocumentsRecyclerView);
//
        if (MyDocuments.this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
        {
            docDocsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this,2));
            docPicsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this,2));
        }
        if (MyDocuments.this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            docDocsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this,3));
            docPicsRecyclerView.setLayoutManager(new GridLayoutManager(MyDocuments.this,3));
        }
        //====================================
        int flatPosition=getIntent().getExtras().getInt("flatposition");
        if (flatPosition!=351)
            getArrayListForUnits(flatPosition);
        else getArrayList();

        //=========================================
        //getArrayList();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw=new DocumentsAdapterRcyclrvw(MyDocuments.this,PicsArrayList);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw=new DocumentPDFadapterRecyclrvw(MyDocuments.this,DocsArrayList);

        if (PicsArrayList.isEmpty())
        {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.fornopics);

            // Create a TextView programmatically.

            // Create a LayoutParams for TextView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

            rl.removeView(pictv);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            pictv.setLayoutParams(lp);
            pictv.setText("No related images available");
            rl.addView(pictv);
        }
        else pictv.setText("");

        if (DocsArrayList.isEmpty()){
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.fornodocs);

            // Create a TextView programmatically.

            // Create a LayoutParams for TextView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

            rl.removeView(doctv);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            doctv.setLayoutParams(lp);
            doctv.setText("No related documents available");
            rl.addView(doctv);
        }else doctv.setText("");


        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);
    }

    private void getArrayListForUnits(int flatPosition) {
        JSONObject docList= null;
        try {
            docList = new JSONObject(responceFromHome);
            JSONArray UnitBlockProjDocuments=docList.getJSONArray("Units Documents");
                JSONObject oneDoc=UnitBlockProjDocuments.getJSONObject(flatPosition);
                JSONArray unitQ=oneDoc.getJSONArray("unit_document");
                getDocsPathTypeNameID(unitQ);
                JSONArray blockQ=oneDoc.getJSONArray("block_document");
                getDocsPathTypeNameID(blockQ);
                JSONArray projQ=oneDoc.getJSONArray("project_document");
                getDocsPathTypeNameID(projQ);


        } catch (JSONException e) {
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }/*
        PicsArrayList=PicarrayList;
        DocsArrayList=DOcarrayList;
        return PicarrayList;*/
    }

    private void putAll() {
        bar.setProgress(total);
        int oneMin= 5* 1000;

        CountDownTimer cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((tp2/ 60) * 100);
                bar.setProgress(total);
            }

            public void onFinish() {
                bar.setVisibility(View.GONE);
            }
        }.start();
        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw=new DocumentsAdapterRcyclrvw(MyDocuments.this,PicsArrayList);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw=new DocumentPDFadapterRecyclrvw(MyDocuments.this,DocsArrayList);



        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);
    }

    private void filterFor(String units) {
        bar.setProgress(total);
        int oneMin= 5* 1000;

        CountDownTimer cdt = new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {

                total = (int) ((tp2/ 60) * 100);
                bar.setProgress(total);
            }

            public void onFinish() {
                bar.setVisibility(View.GONE);
            }
        }.start();
        ArrayList<DocumentsContent> newPicArray=new ArrayList<>();
        ArrayList<DocumentsContent> newDocArray=new ArrayList<>();

        for (int i=0;i<PicsArrayList.size();i++)
            if (PicsArrayList.get(i).getTableName().contains(units))
                newPicArray.add(PicsArrayList.get(i));

        for (int i=0;i<DocsArrayList.size();i++)
            if (DocsArrayList.get(i).getTableName().contains(units))
                newDocArray.add(DocsArrayList.get(i));

        DocumentsAdapterRcyclrvw documentsAdapterRcyclrvw=new DocumentsAdapterRcyclrvw(MyDocuments.this,newPicArray);
        DocumentPDFadapterRecyclrvw documentPDFadapterRecyclrvw=new DocumentPDFadapterRecyclrvw(MyDocuments.this,newDocArray);


        if (newPicArray.isEmpty())
        {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.fornopics);

            // Create a TextView programmatically.

            // Create a LayoutParams for TextView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

            rl.removeView(pictv);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            pictv.setLayoutParams(lp);
            pictv.setText("No related images available");
            rl.addView(pictv);
        }
        else pictv.setText("");

        if (newDocArray.isEmpty()){
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.fornodocs);

            // Create a TextView programmatically.

            // Create a LayoutParams for TextView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView

            rl.removeView(doctv);

            lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            doctv.setLayoutParams(lp);
            doctv.setText("No related documents available");
            rl.addView(doctv);
        }else doctv.setText("");


        docDocsRecyclerView.setAdapter(documentPDFadapterRecyclrvw);
        docPicsRecyclerView.setAdapter(documentsAdapterRcyclrvw);

    }

    private void getArrayList() {
       // ArrayList<DocumentsContent> PicarrayList=new ArrayList<>();
        //ArrayList<DocumentsContent> DOcarrayList=new ArrayList<>();

        /*for (int i=0;i<9;i++){
            arrayList.add(new DocumentsContent(String.valueOf(i)));
        }*/
        JSONObject docList= null;
        try {
            docList = new JSONObject(responceFromHome);
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
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            Toast.makeText(MyDocuments.this, "-0=--0=-=-=0=-0=0==-=0=0=-0=-=0=0=0", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }/*
        PicsArrayList=PicarrayList;
        DocsArrayList=DOcarrayList;
        return PicarrayList;*/
    }

    private void getDocsPathTypeNameID(JSONArray unitQ) {
        for (int i=0;i<unitQ.length();i++)
            try {
                JSONObject singleDocQ=unitQ.getJSONObject(i);
                if (singleDocQ.getString("doc_path").contains(".pdf"))
                    DocsArrayList.add(new DocumentsContent(singleDocQ.getString("name"),singleDocQ.getString("doc_path"),singleDocQ.getString("association_id"),singleDocQ.getString("table_name"),singleDocQ.getString("doc_type")));
                else
                    PicsArrayList.add(new DocumentsContent(singleDocQ.getString("name"),singleDocQ.getString("doc_path"),singleDocQ.getString("association_id"),singleDocQ.getString("table_name"),singleDocQ.getString("doc_type")));
            } catch (JSONException e) {

                e.printStackTrace();
            }
    }

}
