package kouroshcorps.testing;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public ApplicationTest() {


        super(MainActivity.class);
    }

    public void test1() {
        Activity main = getActivity();
        try {
            OutputStreamWriter out = new OutputStreamWriter(main.openFileOutput("list.txt", Context.MODE_PRIVATE));
            out.write("SuperStore Milk,Eggs,Beef \n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test2(){
        Activity main = getActivity();
        ListView lv = (ListView) main.findViewById(R.id.listView);
        int first = lv.getCount();
        assertEquals(1, first);
    }

    public void test3() {
        Activity main = getActivity();
        String[] array = {"Bacon", "Yogourt"};
        List<String> list = Arrays.asList(array);
        TextFile.addList(main, "Safeway", list);
    }
    public void test4() {
        Activity main = getActivity();
        ListView lv = (ListView) main.findViewById(R.id.listView);
        int second = lv.getCount();
        assertEquals(2, second);
    }

    public void test5(){
        Activity main = getActivity();
        TextFile.deleteList(main, "SuperStore");
    }

    public void test6(){

        Activity main = getActivity();
        ListView lv = (ListView) main.findViewById(R.id.listView);
        assertEquals(1,lv.getCount());
    }
}