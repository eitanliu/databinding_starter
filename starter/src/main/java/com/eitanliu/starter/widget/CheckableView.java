package com.eitanliu.starter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;

import com.eitanliu.starter.R;

/**
 * {@link android.widget.Space} that support draw
 */
public class CheckableView extends View implements Checkable {

    private static final int[] DRAWABLE_STATE_CHECKED = new int[]{android.R.attr.state_checked};

    private boolean checked;
    private boolean checkable = true;
    private boolean pressable = true;

    public CheckableView(Context context) {
        this(context, null);
    }

    public CheckableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //noinspection resource
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CheckableView, defStyleAttr, 0);
        boolean checked = attributes.getBoolean(R.styleable.CheckableView_android_checked, isChecked());
        setChecked(checked);
        checkable = attributes.getBoolean(R.styleable.CheckableView_android_checkable, checkable);
        attributes.recycle();

        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityEvent(@NonNull View host, @NonNull AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                event.setChecked(isChecked());
            }

            @Override
            public void onInitializeAccessibilityNodeInfo(@NonNull View host, @NonNull AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCheckable(isCheckable());
                info.setChecked(isChecked());
            }
        });
    }

    /**
     * Compare to: {@link View#getDefaultSize(int, int)}
     * If mode is AT_MOST, return the child size instead of the parent size
     * (unless it is too big).
     */
    private static int getDefaultSize2(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize2(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkable && this.checked != checked) {
            this.checked = checked;
            refreshDrawableState();
            sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressable) {
            super.setPressed(pressed);
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (checked) {
            int[] baseState = super.onCreateDrawableState(extraSpace + DRAWABLE_STATE_CHECKED.length);
            return mergeDrawableStates(baseState, DRAWABLE_STATE_CHECKED);
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    @NonNull
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.checked = checked;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
    }

    /**
     * Sets view to be checkable or not.
     */
    public void setCheckable(boolean checkable) {
        if (this.checkable != checkable) {
            this.checkable = checkable;
            sendAccessibilityEvent(AccessibilityEventCompat.CONTENT_CHANGE_TYPE_UNDEFINED);
        }
    }

    /**
     * Returns whether the view is checkable.
     */
    public boolean isCheckable() {
        return checkable;
    }

    /**
     * Sets view to be pressable or not.
     */
    public void setPressable(boolean pressable) {
        this.pressable = pressable;
    }

    /**
     * Returns whether the view is pressable.
     */
    public boolean isPressable() {
        return pressable;
    }

    static class SavedState extends AbsSavedState {

        boolean checked;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(@NonNull Parcel source, ClassLoader loader) {
            super(source, loader);
            readFromParcel(source);
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(checked ? 1 : 0);
        }

        private void readFromParcel(@NonNull Parcel in) {
            checked = in.readInt() == 1;
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @NonNull
            @Override
            public SavedState createFromParcel(@NonNull Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @NonNull
            @Override
            public SavedState createFromParcel(@NonNull Parcel in) {
                return new SavedState(in, null);
            }

            @NonNull
            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
