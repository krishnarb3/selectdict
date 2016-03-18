package dev.noob.pro.rb.selectdict;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.nitt.cse.selectdictionary.DictionaryItem;
import com.nitt.cse.selectdictionary.DictionaryLookup;
import com.nitt.cse.selectdictionary.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rb on 22/11/15.
 */

public class HeadLayer extends View {

    String mData;
    Context context;
    FrameLayout frameLayout;
    View mView;
    WindowManager mWindowManager;
    WindowManager.LayoutParams params;
    ListView mListView;
    ImageView icon;
    ClipboardManager mClipBoardManager;
    ContentResolver mContentResolver;
    Animation mAnimRotate;

    public HeadLayer(Context context) {
        super(context);
        this.context = context;
        this.frameLayout = new FrameLayout(context);
        addtoWindow();
        mAnimRotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        mAnimRotate.setRepeatCount(Animation.INFINITE);
    }

    private void addtoWindow() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.END;
        params.x = 0;
        params.y = 100;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(frameLayout, params);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.head, frameLayout);
        mListView = (ListView) mView.findViewById(R.id.text);
        mListView.setVisibility(INVISIBLE);
        icon = (ImageView) mView.findViewById(R.id.icon);

        icon.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(frameLayout, params);
                        return true;
                }
                return false;
            }
        });

        mContentResolver = context.getContentResolver();
        mClipBoardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipBoardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                frameLayout.setVisibility(VISIBLE);
                mListView.setVisibility(INVISIBLE);
                try {
                    mData = mClipBoardManager.getPrimaryClip()
                            .getItemAt(0).getText().toString();
                    Log.d(Utils.LOGGING, "Executing Asynctask");
                    new AsyncFetchMeaningTask().execute(mData);
                } catch (Exception e) {
                    Log.d(Utils.LOGGING, e + "");
                }
            }
        });
    }

    public void toggleVisibility() {
        if(frameLayout.getVisibility()==VISIBLE)
            frameLayout.setVisibility(INVISIBLE);
        else
            frameLayout.setVisibility(VISIBLE);
    }

    public void destroy() {
        //mWindowManager.removeView(frameLayout);
        frameLayout.setVisibility(INVISIBLE);
    }

    public void clicked() {
        mListView = (ListView) mView.findViewById(R.id.text);
        mListView.setVisibility(VISIBLE);
    }

    public class AsyncFetchMeaningTask extends AsyncTask<String,Void,List<DictionaryItem>> {
        List<String> result=new ArrayList<>();
        List<DictionaryItem> items;

        @Override
        protected void onPreExecute() {
            icon.clearColorFilter();
            super.onPreExecute();
            icon.startAnimation(mAnimRotate);
        }

        @Override
        protected List<DictionaryItem> doInBackground(String... params) {
            List<String> paramsList = new ArrayList<>();
            for(int i=0;i<params.length;i++)
                paramsList.add(params[i]);
            items = DictionaryLookup.getMeaning(paramsList);
            return items;
        }

        @Override
        protected void onPostExecute(List<DictionaryItem> dictionaryItems) {
            icon.clearAnimation();
            super.onPostExecute(dictionaryItems);
            mData = null;
            for(int i=0;i<dictionaryItems.size();i++)
                result.add(dictionaryItems.get(i).getWord()+"\n\n"
                        +dictionaryItems.get(i).getDescription()+"\n");
            Log.d(Utils.LOGGING,result.toString());
            if(!dictionaryItems.get(dictionaryItems.size()-1).getDescription().equals("")) {
                mListView.setVisibility(VISIBLE);
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, result);
                mListView.setAdapter(adapter);
            }
            else {
                result.add("Connection Failure");
                icon.setColorFilter(Color.argb(156, 252, 76, 78));
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, result);
                mListView.setAdapter(adapter);
            }
        }
    }
    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            toggleVisibility();
        }
    });
}
