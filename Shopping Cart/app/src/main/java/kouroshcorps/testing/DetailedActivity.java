package kouroshcorps.testing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    private int listPos;
    private ListView lv;
    private ArrayList<String> items;
    private TextFile tf;

    //Prompt
    final Context context = this;

    //Swiping
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private static final int MOVE_DURATION = 150;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onResume(){
        super.onResume();

        listPos = getIntent().getIntExtra("listPos", 0);
        tf = TextFile.getInstance(null);
        List list = tf.getList(listPos);


        items = list.getItemsNames();
        TextView tv = (TextView) findViewById(R.id.nameText);


        tv.setText(list.getTitle());

        ArrayAdapter<String> adapter = new ItemAdapter(DetailedActivity.this, items, mTouchListener, listPos);
        lv = (ListView) findViewById(R.id.listViewDetailed);
        lv.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.add_item, null);

                AlertDialog.Builder aDB = new AlertDialog.Builder(context);
                aDB.setView(promptsView);

                final EditText ed = (EditText) promptsView.findViewById(R.id.itemName);

                aDB
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                tf.addItem(listPos, new Item(ed.getText().toString(),false));
                                onResume();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog aD = aDB.create();
                aD.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id) {
            case R.id.action_delete:
                TextView title = (TextView) findViewById(R.id.nameText);
                TextFile tf = TextFile.getInstance(null);
                tf.deleteList(listPos);
                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        float mDownX;
        private int mSwipeSlop = -1;
        boolean swiped;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(DetailedActivity.this).getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Doesn't allow swiping two items at same time
                        return true;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    swiped = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE: {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);

                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) // tells if user is actually swiping or just touching in sloppy manner
                        {
                            mSwiping = true;
                            lv.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (mSwiping && !swiped) // Need to make sure the user is both swiping and has not already completed a swipe action (hence mSwiping and swiped)
                    {
                        v.setTranslationX((x - mDownX)); // moves the view as long as the user is swiping and has not already swiped

                        if (deltaX < -1 * (v.getWidth() / 2)) // swipe to left
                        {

                            v.setEnabled(false); // need to disable the view for the animation to run

                            // stacked the animations to have the pause before the views flings off screen
                            v.animate().setDuration(0).translationX(-v.getWidth() / 2).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    v.animate().setDuration(300).alpha(0).translationX(-v.getWidth()).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSwiping = false;
                                            mItemPressed = false;
                                            animateRemoval(lv, v);
                                        }
                                    });
                                }
                            });
                            mDownX = x;
                            swiped = true;
                            return true;

                        }
                        else if (deltaX > v.getWidth() / 2) // swipe to right
                        {
                            mDownX = x;
                            swiped = true;
                            mSwiping = false;
                            mItemPressed = false;


                            v.animate().setDuration(300).translationX(v.getWidth() / 3); // could pause here if you want, same way as delete
                            TextView tv = (TextView) v.findViewById(R.id.item_tv);

                            for (int i = 0; i < lv.getChildCount(); ++i) {
                                View child = lv.getChildAt(i);
                                if (child == v) {
                                    List list = tf.getList(listPos);
                                    if (list.items.get(i).purchased){
                                        list.items.get(i).purchased=false;
                                        tv.setTextColor(getResources().getColor(R.color.colorBlack));
                                    }
                                    else{
                                        list.items.get(i).purchased=true;
                                        tv.setTextColor(getResources().getColor(R.color.colorAccent));
                                    }
                                    tf.save();
                                }
                            }


                            return true;
                        }
                    }

                }
                break;
                case MotionEvent.ACTION_UP: {
                    if (mSwiping) // if the user was swiping, don't go to the and just animate the view back into position
                    {
                        v.animate().setDuration(300).translationX(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mSwiping = false;
                                mItemPressed = false;
                                lv.setEnabled(true);
                            }
                        });
                    }
                }
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listView, View viewToRemove) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        final ArrayAdapter adapter = (ArrayAdapter) lv.getAdapter();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            View child = listView.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = listView.getAdapter().getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
            else{
                TextFile tf = TextFile.getInstance(null);
                tf.deleteItem(listPos,i);
            }
        }

        adapter.remove(adapter.getItem(listView.getPositionForView(viewToRemove)));

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i) {
                    final View child = listView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mSwiping = false;
                                        lv.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mSwiping = false;
                                    lv.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }
}
