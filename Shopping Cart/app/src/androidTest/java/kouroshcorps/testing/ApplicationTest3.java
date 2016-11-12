package kouroshcorps.testing;

import android.app.Activity;
import android.content.Context;
import android.support.v7.view.menu.ActionMenuItemView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by kourosh on 2016-03-11.
 */
public class ApplicationTest3 extends ActivityInstrumentationTestCase2<DetailedActivity> {
    public ApplicationTest3() {
        super(DetailedActivity.class);
    }

    public void test(){

        Activity main = getActivity();

        try {
            OutputStreamWriter out = new OutputStreamWriter(main.openFileOutput("list.txt", Context.MODE_PRIVATE));
            out.write("SuperStore Milk,Eggs,Beef \n");
            out.write("Safeway Yogurt,Bacon");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity main = getActivity();
                TextView tv = (TextView) main.findViewById(R.id.nameText);
                tv.setText("Safeway");
                ActionMenuItemView save = (ActionMenuItemView) main.findViewById(R.id.action_delete);
                save.performClick();

                List<String> titles = TextFile.getTitles(main);

                assertEquals(1,titles.size());
            }
        });

    }

}