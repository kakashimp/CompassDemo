package richadx.com.compassdemo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.whilu.library.CustomButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class PhongThuyActivity extends AppCompatActivity {
    private  TextView txt1,txt2;
    private  EditText edNamSinh;
    private CustomButton btnTimHuong;
    private String namsinh ="";
    private PublisherAdView publisherAdView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_thuy);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        edNamSinh = findViewById(R.id.edNamSinh);
        btnTimHuong = findViewById(R.id.btnTimHuong);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/iCiel Alina.otf");
        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);
        btnTimHuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namsinh = edNamSinh.getText().toString();
                if (namsinh.length() == 4){
                    int a= Integer.parseInt(namsinh.toString());
                    if (a>1964&&a<2011){
                        Intent i = new Intent(PhongThuyActivity.this,PhongThuy2Activity.class);
                        i.putExtra("namsinh",namsinh);
                        startActivity(i);
                    }else {
                        Toast.makeText(PhongThuyActivity.this, "Chỉ nhập năm sinh từ 1965-2011", Toast.LENGTH_SHORT).show();
                    }

                }



                
            }
        });
//        publisherAdView2=findViewById(R.id.publisherAdView2);
//        PublisherAdRequest adRequest=new PublisherAdRequest.Builder().build();
//        publisherAdView2.loadAd(adRequest);
//        Log.d("kiemtraa", "onCreate: "+adRequest);
//        publisherAdView2.setAdListener(new AdListener(){
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                PublisherAdRequest adRequest=new PublisherAdRequest.Builder().build();
//                publisherAdView2.loadAd(adRequest);
//            }
//        });



    }
}
