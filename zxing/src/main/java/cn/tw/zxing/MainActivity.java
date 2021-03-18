package cn.tw.zxing;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView tv_sn1,tv_sn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_sn1 = findViewById(R.id.tv_sn1);
        tv_sn2 = findViewById(R.id.tv_sn2);
    }

    //生成条形码
    public void btuon(View v){
        Ecoad ecc=new Ecoad(tv_sn1.getWidth(), tv_sn1.getHeight());
        try {
            Bitmap bitm=ecc.BarCode(String.valueOf(123456789));
            tv_sn1.setImageBitmap(bitm);
            tv_sn1.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //生成二维码
    public void btuon1(View v){
        Ecoad ec=new Ecoad(tv_sn2.getWidth(), tv_sn2.getHeight());
        Bitmap bitmap;
        try {
            bitmap = ec.QrCode(String.valueOf(123456789));
            tv_sn2.setImageBitmap(bitmap);
            tv_sn2.setBackground(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
