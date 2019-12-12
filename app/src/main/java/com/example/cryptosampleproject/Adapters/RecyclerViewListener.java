package com.example.cryptosampleproject.Adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewListener implements RecyclerView.OnItemTouchListener {
    private RecyclerView.OnItemTouchListener onItemTouchListener;
    private onItemClickListener onItemClickListener;
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child != null && onItemClickListener != null && gestureDetector.onTouchEvent(e)){
            onItemClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }
    GestureDetector gestureDetector;

    public RecyclerViewListener(Context context,final RecyclerView recyclerView,final onItemClickListener listener){
        onItemClickListener = listener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && onItemClickListener != null){
                    onItemClickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
                }
                return false;
            }
        });
    }
    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface onItemClickListener{
        void onClick(View view, int position);
    }
}
