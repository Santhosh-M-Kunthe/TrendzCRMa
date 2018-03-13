package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiteURL on 2/26/2018.
 */

public class FlatListAdapter extends ArrayAdapter<FlatContents> {

    Context contextOfAdapter;
    int resourceOfAdapter;
    ArrayList<FlatContents> objectsOfAdapter;
    public FlatListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FlatContents> objects) {
        super(context, resource, objects);
        contextOfAdapter=context;
        resourceOfAdapter=resource;
        objectsOfAdapter=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(contextOfAdapter).inflate(resourceOfAdapter,parent,false);
        TextView flatname=(TextView) view.findViewById(R.id.contentflatname);
        flatname.setText(objectsOfAdapter.get(position).getUnitName());

        ImageView img = (ImageView)view.findViewById(R.id.swiperightatoz);
        img.setBackgroundResource(R.drawable.swiperightatoz);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();//-=-=====----=-=-=_+_+-+-+-=_+-=_=_+-=_+-=_=-=_=_+-+_=_=_+-+-+-=_=_+_+-=_=-+_=_=_+-=_
        TextView flatbalnace=(TextView) view.findViewById(R.id.contentflatbbalance);
        flatbalnace.setText(objectsOfAdapter.get(position).getProjectId()+" - "+objectsOfAdapter.get(position).getBlockID());
        return view;
    }

}
