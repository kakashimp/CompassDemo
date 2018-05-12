package richadx.com.compassdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ScreenshotActivity extends AppCompatActivity{
    String uri;
    ImageView imageView,whiteArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_screenshot);
        Intent getintent = getIntent();
        imageView=findViewById(R.id.saveimage);
        whiteArrow=findViewById(R.id.imBack);

        uri=getintent.getStringExtra("bitmap");
        Log.d("aaa", "onCreate: "+uri);
        Picasso.get().load(uri).into(imageView);

        whiteArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
