package com.example.myimageapp.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior {
    public BottomNavigationBehavior() {
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull @NotNull CoordinatorLayout parent, @NonNull @NotNull View child, @NonNull @NotNull View dependency) {
        boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull @NotNull CoordinatorLayout coordinatorLayout, @NonNull @NotNull View child, @NonNull @NotNull View directTargetChild, @NonNull @NotNull View target, int axes) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull @NotNull CoordinatorLayout coordinatorLayout, @NonNull @NotNull View child, @NonNull @NotNull View target, int dx, int dy, @NonNull @NotNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            if (dy < 0) {
                showBottomNavigationView((BottomNavigationView) child);
            } else if (dy > 0) {
                hideBottomNavigationView((BottomNavigationView) child);
            }
    }

    private void hideBottomNavigationView(BottomNavigationView view){
        view.animate().translationY(view.getHeight());

    }
    private void showBottomNavigationView(BottomNavigationView view){
        view.animate().translationY(0);
    }
}
