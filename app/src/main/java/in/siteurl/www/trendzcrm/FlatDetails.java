package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

public class FlatDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_details);

        getSupportActionBar().setHomeButtonEnabled(true);

        String[] flatlistdetailsarray=(String[])getIntent().getSerializableExtra("flatDetails");
        ((TextView)findViewById(R.id.unitname)).setText(flatlistdetailsarray[0]);
        ((TextView)findViewById(R.id.value)).setText(flatlistdetailsarray[1]);
        ((TextView)findViewById(R.id.size)).setText(flatlistdetailsarray[2]);
        ((TextView)findViewById(R.id.salevalue)).setText(flatlistdetailsarray[3]);
        ((TextView)findViewById(R.id.balamnt)).setText(flatlistdetailsarray[4]);
        ((TextView)findViewById(R.id.recamnt)).setText(flatlistdetailsarray[5]);
        ((TextView)findViewById(R.id.floor)).setText(flatlistdetailsarray[6]);
        ((TextView)findViewById(R.id.blckname)).setText(flatlistdetailsarray[7]);
        ((TextView)findViewById(R.id.projname)).setText(flatlistdetailsarray[8]);
        ((TextView)findViewById(R.id.sou)).setText(flatlistdetailsarray[9]);

        Button goTOAddTicket=findViewById(R.id.flattoat);
        goTOAddTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAT=new Intent(FlatDetails.this,AddTicket.class);
                toAT.putExtra("unitid",getIntent().getExtras().getString("unitid"));
                startActivity(toAT);
            }
        });

    }

    public void finish(View view) {
        finish();
    }

    public void passFlatDocsToMyDocs(View view) {

        //open My Documents related too that particular Flat only
        Intent intent=new Intent(FlatDetails.this,MyDocuments.class);
        intent.putExtra("flatposition",getIntent().getExtras().getInt("flatposition"));
        intent.putExtra("responseFromHome",getIntent().getExtras().getString("responseFromHome"));
        startActivity(intent);
    }
}
