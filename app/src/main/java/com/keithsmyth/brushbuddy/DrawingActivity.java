package com.keithsmyth.brushbuddy;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.flexbox.FlexboxLayout;

public class DrawingActivity extends AppCompatActivity {

    private View rootView;
    private DrawingView drawingView;
    private NestedScrollView bottomSheetLayout;
    private FlexboxLayout flexboxLayout;
    private FloatingActionButton bottomSheetFab;

    private BottomSheetBehavior<NestedScrollView> bottomSheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        rootView = findViewById(R.id.activity_drawing);
        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        flexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        bottomSheetLayout = (NestedScrollView) findViewById(R.id.bottom_sheet_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetFab = (FloatingActionButton) findViewById(R.id.bottom_sheet_fab);
        bottomSheetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int state =  bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED
                        ? BottomSheetBehavior.STATE_EXPANDED
                        : BottomSheetBehavior.STATE_COLLAPSED;
                bottomSheetBehavior.setState(state);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //rootView.post(new EnsureBottomSheetHeightRunnable(rootView, bottomSheetLayout));
        initBottomSheet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawing_menu, menu);
        final Drawable undoDrawable = menu.findItem(R.id.action_undo).getIcon();
        undoDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_undo) {
            drawingView.undo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBottomSheet() {
        flexboxLayout.setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);
        flexboxLayout.setFlexWrap(FlexboxLayout.FLEX_WRAP_WRAP);
        final int buttonSize = getResources().getDimensionPixelSize(R.dimen.drawing_button_size);
        final int[] colors = new int[]{Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.WHITE, Color.CYAN, Color.DKGRAY};
        for (final int color : colors) {
            final Button button = new Button(this);
            button.setHeight(buttonSize);
            button.setWidth(buttonSize);
            button.setBackgroundColor(color);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setColor(color);
                    bottomSheetFab.setColorFilter(color);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });
            flexboxLayout.addView(button);
//            final FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) button.getLayoutParams();
//            params.flexGrow =
        }
    }

    private static class EnsureBottomSheetHeightRunnable implements Runnable {

        private final View rootView;
        private final View bottomSheetView;

        EnsureBottomSheetHeightRunnable(View rootView, View bottomSheetView) {
            this.rootView = rootView;
            this.bottomSheetView = bottomSheetView;
        }

        @Override
        public void run() {
            bottomSheetView.getLayoutParams().height = (int) (rootView.getHeight() * 0.4);
            bottomSheetView.requestLayout();
        }
    }
}