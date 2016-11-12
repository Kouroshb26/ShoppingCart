package kouroshcorps.testing;

import android.app.Activity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import java.util.List;

/**
 * Created by kourosh on 2016-03-11.
 */
public class ApplicationTest2 extends ActivityInstrumentationTestCase2<AddListActivity> {
    public ApplicationTest2() {
        super(AddListActivity.class);
    }

    public void test2(){
        Activity add = getActivity();
        add.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity add = getActivity();
                EditText ed = (EditText) add.findViewById(R.id.editText);
                ed.setText("Safeway");
                ActionMenuItemView save = (ActionMenuItemView) add.findViewById(R.id.action_save2);
                save.performClick();

                List<String> titles = TextFile.getTitles(add);

                assertEquals("Safeway", titles.get(titles.size()-1 ));
            }
        });
    }
}

