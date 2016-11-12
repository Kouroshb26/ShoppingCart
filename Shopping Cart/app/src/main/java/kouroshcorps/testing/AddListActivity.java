package kouroshcorps.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

public class AddListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_save2:
                EditText ed = (EditText) findViewById(R.id.listTitle);
                String title = ed.getText().toString();

                ed = (EditText) findViewById(R.id.listContent);
                String [] itemsNames = ed.getText().toString().split(",");

                ArrayList<Item> items = new ArrayList<>();
                for(String name: itemsNames){
                    items.add(new Item(name,false));
                }

                List list = new List(title,items);

                TextFile tf = TextFile.getInstance(null);
                tf.addList(title,list);

                Intent intent = new Intent(AddListActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
