package cn.tw.viewpager2;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;

public class MyPageTransformer implements ViewPager2.PageTransformer {

    private static final float ROT_MAX = 20.0f;
    private float mRot;
    private static float MIN_SCALE = 0.75f;
    private static float MIN_ALPHA = 0.5f;

    /**
     * @param view     滑动中的那个view
     * @param position
     */
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)//This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]Use //the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) { // (0,1]// Fade the page out.
            view.setAlpha(1 - position);
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }


//        int pageWidth = view.getWidth();
//        int pageHeight = view.getHeight();
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0);
//        } else if (position <= 1) { // [-1,1]
//            // Modify the default slide transition to
//            // shrink the page as well
//            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//            if (position < 0) {
//                view.setTranslationX(horzMargin - vertMargin / 2);
//            } else {
//                view.setTranslationX(-horzMargin + vertMargin / 2);
//            }
//            // Scale the page down (between MIN_SCALE and 1)
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//            // Fade the page relative to its size.
//            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
//                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0);
//        }

//        //界面不可见
//        if (position < -1) {
//            ViewCompat.setRotation(view, 0);
//
//        }   else if (position <= 0) {
//            mRot = (ROT_MAX * position);
//            ViewCompat.setPivotX(view, view.getMeasuredWidth() * 0.5f);
//            ViewCompat.setPivotY(view, view.getMeasuredHeight());
//            ViewCompat.setRotation(view, mRot);
//            Log.i("md","1111111111111");
//        }
//        //界面可见
//        else if (position <= 1) {
//            mRot = (ROT_MAX * position);
//            ViewCompat.setPivotX(view, view.getMeasuredWidth() * 0.1f);
//            ViewCompat.setPivotY(view, view.getMeasuredHeight());
//            ViewCompat.setRotation(view, mRot);
//            Log.i("md","2222222222222");
//
//        }
//        //界面不可见
//        else {
//            ViewCompat.setRotation(view, 0);
//        }
    }
}