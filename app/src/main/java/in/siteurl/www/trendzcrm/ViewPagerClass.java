package in.siteurl.www.trendzcrm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ViewPagerClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        final ViewPager gallery=(ViewPager) findViewById(R.id.viewPager);

       final String[] filelist=(String[]) getIntent().getSerializableExtra("FILES_TO_SEND");

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(ViewPagerClass.this,filelist);
        gallery.setAdapter(viewPagerAdapter);
        gallery.setCurrentItem(1+getIntent().getExtras().getInt("position"));

        //========================================================================
/*        FloatingActionButton fab=findViewById(R.id.share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = null;
                try {
                    URL url = new URL(filelist[gallery.getCurrentItem()]);
                    icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch(IOException e) {
                    System.out.println(e);
                }
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });*/
        //==========================================================================================
       gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               //Toast.makeText(ViewPagerClass.this, String.valueOf(positionOffset), Toast.LENGTH_LONG).show();
           }

           @Override
           public void onPageSelected(int position) {

               //Toast.makeText(ViewPagerClass.this, String.valueOf(position)+"---"+String.valueOf(filelist.length), Toast.LENGTH_SHORT).show();
               if (position==(filelist.length-1))
                   gallery.setCurrentItem(1);
               if (position==0)
                   gallery.setCurrentItem(filelist.length-2);
           }

           @Override
           public void onPageScrollStateChanged(int state) {
               //Toast.makeText(ViewPagerClass.this, String.valueOf(state), Toast.LENGTH_SHORT).show();
           }
       });
        //===================================================================================================
    }
}
