package cn.tw.pdf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import java.io.File;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.permissions.XPermission;

public class MainActivity extends AppCompatActivity implements
        OnLoadCompleteListener,
        OnPageErrorListener,
        View.OnClickListener {

    private PDFView pdfView;
    private int mPpagerIndex;
    private int mPageCount;
    PDFView.Configurator configurator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XApp.init(getApplication());
        setContentView(R.layout.activity_main);
        //获取动态权限
        getPermission();
        pdfView = (PDFView) findViewById(R.id.pdfView);
        LoadFile();
    }

    /**
     *     .pages(0, 2, 1, 3, 3, 3) // 默认显示所有页面
     *     .enableSwipe(true) // 允许使用滑动来阻止改变页面
     *     .swipeHorizontal(false) //是否横向滑动显示
     *     .enableDoubletap(true)
     *     .defaultPage(0) //默认进入的页面
     *     // 允许在当前页面上画一些东西，通常在屏幕中间可见
     *     .onDraw(onDrawListener)
     *     // 允许在所有页面上画一些东西，单独为每一页。仅对可见页面调用
     *     .onDrawAll(onDrawListener)
     *     .onLoad(onLoadCompleteListener) // 加载文档并开始呈现后调用
     *     .onPageChange(onPageChangeListener)
     *     .onPageScroll(onPageScrollListener)
     *     .onError(onErrorListener)
     *     .onPageError(onPageErrorListener)
     *     .onRender(onRenderListener) // 第一次呈现文档后调用
     *     // 点击一次调用，如果已处理则返回true，如果切换滚动句柄可见性返回false
     *     .onTap(onTapListener)
     *     .onLongPress(onLongPressListener)
     *     .enableAnnotationRendering(false) // 呈现注释(例如注释、颜色或表单)
     *     .password(null)
     *     .scrollHandle(null)
     *     .enableAntialiasing(true) // 在低分辨率屏幕上改进渲染
     *     // dp中页面之间的间距。要定义间距颜色，设置视图背景
     *     .spacing(0)
     *     .autoSpacing(false) // 添加动态间距以适应屏幕上的每个页面
     *     .linkHandler(DefaultLinkHandler)
     *     .pageFitPolicy(FitPolicy.WIDTH) // 模式以适应视图中的页面
     *     .fitEachPage(false) // 将每个页面适应于视图，否则较小的页面相对于最大的页面缩放
     *     .pageSnap(false) // 将页面对齐到屏幕边界
     *     .pageFling(false) // 在ViewPager这样的单一页面上做一个短暂的改变
     *     .nightMode(false) // 切换夜间模式
     */
    private void showPDFUI(){
        configurator.enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(false)
                .fitEachPage(false)
                .autoSpacing(true)
                .pageFitPolicy(FitPolicy.WIDTH)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                    }
                })
                .onPageChange(new OnPageChangeListener() {

                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        mPageCount = pageCount;
                        mPpagerIndex = page;
                    }
                })
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(0)
                .pageSnap(true)
                .nightMode(true)
                .load();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private void getPermission() {
        XPermission.permissionGroup(PERMISSIONS_STORAGE).callback(new XPermission.SimpleCallback() {
            @Override
            public void onGranted() {
                XLog.i();
            }

            @Override
            public void onDenied() {
                XLog.i();
            }
        }).request();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageError(int page, Throwable t) {
        XLog.i(page+"   "+t.getMessage());
    }

    String Path   = "/mnt/sdcard/iNand/TWPDF.pdf"; //客户私版说明书路径
    String Path_  = "/system/etc/TWPDF.pdf"; //公版说明书路径
    public void LoadFile(){
        File newFile = new File(Path);
        File newFile_ = new File(Path_);
        if(newFile!=null && newFile.exists() && newFile.canRead()){
            configurator = pdfView.fromFile(newFile);
        }else if(newFile_!=null && newFile_.exists() && newFile.canRead()){
            configurator = pdfView.fromFile(newFile_);
        }else{
            configurator = pdfView.fromAsset("TWPDF.pdf");
        }
        showPDFUI();
    }

    @Override
    public void onClick(View v) {
//        pdfView.jumpTo(5,true);//跳转指定页面
    }
}
