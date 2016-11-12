package kouroshcorps.testing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextFile tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tf = TextFile.getInstance(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, AddListActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();

        ArrayList<String> titles = tf.getTitles();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,titles);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);

                intent.putExtra("listPos", position);
                startActivity(intent);
            }
        });

    }
    public Activity getActivity(){
        return this;
    }
}
