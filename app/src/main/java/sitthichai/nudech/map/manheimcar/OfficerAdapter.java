package sitthichai.nudech.map.manheimcar;

import android.content.Context;
import android.os.BaseBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by man on 24/10/2559.
 */

public class OfficerAdapter extends BaseAdapter{
    // Explicit
    private Context context;
    private String[] nameStrings, latStrings, lngStrings, imageStrings;
    private TextView nameTextView, latTextView, lngTextView;
    private ImageView imageView;

    public OfficerAdapter(Context context, String[] nameStrings, String[] latStrings, String[] lngStrings, String[] imageStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.latStrings = latStrings;
        this.lngStrings = lngStrings;
        this.imageStrings = imageStrings;
    }


    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Open service
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Binding view
        View view1 = layoutInflater.inflate(R.layout.officer_listview, viewGroup, false);

        // Bind widget
        nameTextView = (TextView) view1.findViewById(R.id.textView2);
        latTextView = (TextView) view1.findViewById(R.id.textView3);
        lngTextView = (TextView) view1.findViewById(R.id.textView4);
        imageView = (ImageView) view1.findViewById(R.id.imageView3);

        // Show view
        nameTextView.setText(nameStrings[i]);
        if (latStrings[i].equals("")) {
            latTextView.setText("NotThing");
        } else {
            latTextView.setText(latStrings[i]);
        }
        if (lngStrings[i].length() == 0) {
            lngTextView.setText("NotThing");
        } else {
            lngTextView.setText(lngStrings[i]);
        }

        // Show Image
        Picasso.with(context).load(imageStrings[i]).into(imageView);

        return view1;
    }
}   // Main class
