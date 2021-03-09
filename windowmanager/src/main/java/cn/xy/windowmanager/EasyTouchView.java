package cn.xy.windowmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class EasyTouchView{

    public static final String TAG = "EasyTouch";
    private static EasyTouchView mEasyTouchView;
    private Context mContext;

    public static EasyTouchView getInstance() {
        if (mEasyTouchView == null) {
            mEasyTouchView = new EasyTouchView();
        }
        return mEasyTouchView;
    }

    private WindowManager mWindowManager;
    private int width, height;
    private double stateHeight;
    private WindowManager.LayoutParams layoutParams;
    private Button iconView;
    private float startX = 0, startY = 0;
    private float startRawX = 0, startRawY = 0;
    private int iconViewX = 0, iconViewY = 0;
    private boolean isIconView = false;

    private DisplayMetrics mDisplayMetrics;

    public void initEasyTouch(Context mContext, DisplayMetrics displayMetrics) {
        this.mDisplayMetrics = displayMetrics;
        this.mContext = mContext;
        width = mDisplayMetrics.widthPixels;
        height = mDisplayMetrics.heightPixels;
        stateHeight = Math.ceil(25 * displayMetrics.density);
        createWM();
    }

    private void createWM() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutParams.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

    }

    @SuppressLint("ClickableViewAccessibility")
    public void addIconView() {
        if (isIconView) {
            return;
        }
        isIconView = true;
        if (iconView == null) {
            iconView = new Button(mContext);
            iconView.setBackgroundResource(R.mipmap.ic_launcher);
            iconView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float rawX = event.getRawX();
                    float rawY = (float) (event.getRawY() - stateHeight);

                    int sumX = (int) (rawX - startRawX);
                    int sumY = (int) (event.getRawY() - startRawY);

                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("Log", "Action_Down");
                        startX = event.getX();
                        startY = event.getY();
                        startRawX = event.getRawX();
                        startRawY = event.getRawY();

                        layoutParams.alpha = 1f;
                        mWindowManager.updateViewLayout(iconView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
/*                        layoutParams.alpha = 0.6f;
                        mWindowManager.updateViewLayout(iconView, layoutParams);
                        if (sumX > -10 && sumX < 10 && sumY > -10 && sumY < 10) {
                            removeIcon();
                        } else {
                            float endRawX = rawX - startX;
                            float endRawY = rawY - startY;
                            if (endRawX < width / 2) {
                                if (endRawX > endRawY) {
                                    if (rawY > iconView.getHeight() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);
                                    } else {
                                        updateIconViewPosition(endRawX, 0);
                                    }
                                } else if (endRawX > height - event.getRawY() - 98) {
                                    if ((float) (height - stateHeight - endRawY - 98) > iconView.getHeight() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);
                                    } else {
                                        updateIconViewPosition(endRawX, (float) (height - stateHeight - 98));

                                    }

                                } else {
                                    if (endRawX > iconView.getWidth() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);

                                    } else {
                                        updateIconViewPosition(0, endRawY);

                                    }

                                }
                            } else {
                                if (width - endRawX - 98 > endRawY) {
                                    if (rawY > iconView.getHeight() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);
                                    } else {
                                        updateIconViewPosition(endRawX, 0);

                                    }

                                } else if (width - endRawX - 98 > height - event.getRawY() - 98) {
                                    if ((float) (height - stateHeight - endRawY - 98) > iconView.getHeight() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);
                                    } else {
                                        updateIconViewPosition(endRawX, (float) (height - stateHeight - 98));
                                    }

                                } else {
                                    if (width - endRawX - 98 > iconView.getWidth() * 2) {
                                        updateIconViewPosition(endRawX, endRawY);

                                    } else {
                                        updateIconViewPosition(width, endRawY);
                                    }

                                }
                            }
                        }
                        startX = 0;
                        startY = 0;
                        startRawX = 0;
                        startRawY = 0;*/
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (sumX < -10 || sumX > 10 || sumY < -10 || sumY > 10) {
                            updateIconViewPosition(rawX - startX, rawY - startY);
                        }
                        break;
                    default:
                        break;
                    }
                    return true;
                }
            });
        }
        layoutParams.alpha = 0.5f;
        layoutParams.x = iconViewX;
        layoutParams.y = iconViewY;
        layoutParams.width = 170;
        layoutParams.height = 170;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager.addView(iconView, layoutParams);
    }

    private void updateIconViewPosition(float x, float y) {
        iconViewX = (int) x;
        iconViewY = (int) y;
        layoutParams.x = (int) x;
        layoutParams.y = (int) y;
        mWindowManager.updateViewLayout(iconView, layoutParams);
    }

    public void removeIcon() {
        if (isIconView && iconView != null) {
            mWindowManager.removeView(iconView);
            isIconView = false;
        }
    }
}
