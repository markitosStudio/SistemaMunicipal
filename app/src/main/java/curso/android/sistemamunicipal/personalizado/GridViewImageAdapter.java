package curso.android.sistemamunicipal.personalizado;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import curso.android.sistemamunicipal.*;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by markitos on 11/09/16.
 */
public class GridViewImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> listPhoto = new ArrayList<Integer>();

    /** Constructor de clase */
    public GridViewImageAdapter(Context c,ArrayList<Integer> pListPhoto ){
        mContext = c;
        //se cargan las miniaturas
        listPhoto=pListPhoto;
        /*listPhoto.add(R.drawable.cac1);
        listPhoto.add(R.drawable.cac2);
        listPhoto.add(R.drawable.cac3);
        listPhoto.add(R.drawable.cac4);*/

    }

    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public Object getItem(int position) {
        return listPhoto.get(position);
    }



    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource( listPhoto.get(position) );
        imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
        imageView.setLayoutParams( new GridView.LayoutParams(220, 260) );
        return imageView;
    }

}
