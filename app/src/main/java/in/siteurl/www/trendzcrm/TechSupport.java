package in.siteurl.www.trendzcrm;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TechSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_support);

        Snackbar.make(findViewById(R.id.snackcoord),"Kindly leave your message. . . Our tech support team will get in touch with you soon. . .",Snackbar.LENGTH_LONG).show();
    }

    public void finishGo(View view) {
        Toast.makeText(TechSupport.this, "Your message has been sent successfully. . .", Toast.LENGTH_SHORT).show();
        finish();
    }
}
