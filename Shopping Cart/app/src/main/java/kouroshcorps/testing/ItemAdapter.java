package kouroshcorps.testing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kourosh on 2016-03-28.
 */
public class ItemAdapter extends ArrayAdapter<String> {

    private View.OnTouchListener mTouchListener;
    private String [] pictureNames = {"apple","bacon","beef","bread","chicken","chips","eggs","milk","orange","pop","rice","water"};

    private int [] pictureId = {R.drawable.apple,R.drawable.bacon,R.drawable.beef,R.drawable.bread,R.drawable.chicken,R.drawable.chips,R.drawable.eggs,R.drawable.milk,R.drawable.orange,R.drawable.pop,R.drawable.rice,R.drawable.water};

    int listPos;
    public ItemAdapter(Context context, ArrayList<String> values,View.OnTouchListener listener,int listPos){
        super(context,R.layout.item, values);
        mTouchListener = listener;
        this.listPos = listPos;
    }
    public View getView(int position,View convertView, ViewGroup parent){


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.item, parent, false);
        TextView text = (TextView) v.findViewById(R.id.item_tv);
        String name = getItem(position);
        text.setText(name);

        TextFile tf = TextFile.getInstance(null);
        List list = tf.getList(listPos);

        if (list.items.get(position).purchased){
            text.setTextColor(v.getResources().getColor(R.color.colorAccent));
        }
        ImageView iv = (ImageView) v.findViewById(R.id.item_img);

        for (int i=0; i<pictureNames.length;i++) {
            if (name.equalsIgnoreCase(pictureNames[i])){
                iv.setImageBitmap(decodeSampledBitmapFromResource(v.getResources(),pictureId[i],50,50));
            }
        }
        v.setOnTouchListener(mTouchListener);
        return v;

    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
