package richadx.com.compassdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PhongThuy2Activity extends AppCompatActivity {
    String DATABASE_NAME="amlich.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private  TextView txt1,txt2;
    private FloatingActionButton btnHelp;
    SQLiteDatabase database=null;
    private String namsinh,tuoi,menh,cung,quetrach,sinhkhi,thieny
            ,diennien,phucvi,tuyetmenh,nguquy,lucsat,hoahai,ketluan;
    private TextView txtnamsinh,txttuoi,txtmenh,txtcung,txtquetrach,txtsinhkhi,
            txtthieny,txtdiennien,txtphucvi,txttuyetmenh,txtnguquy,txtlucsat,txthoahai,txtketluan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_thuy2);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/iCiel Alina.otf");
        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);
        init();
        getIntent();
        namsinh = getIntent().getStringExtra("namsinh");
        processCopy();
        getEmployeeName(namsinh);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PhongThuy2Activity.this , HelpActivity.class);
                startActivity(i);
            }
        });
    }

    private void init() {
        txtnamsinh = findViewById(R.id.txtNamSinh);
        txttuoi = findViewById(R.id.txtTuoi);
        txtmenh = findViewById(R.id.txtMenh);
        txtcung = findViewById(R.id.txtCung);
        txtquetrach = findViewById(R.id.txtQueTrach);
        txtsinhkhi = findViewById(R.id.txtSinhKhi);
        txtthieny = findViewById(R.id.txtThienY);
        txtdiennien = findViewById(R.id.txtDienNien);
        txtphucvi = findViewById(R.id.txtPhucVi);
        txttuyetmenh = findViewById(R.id.txtTuyetMenh);
        txtnguquy = findViewById(R.id.txtNguQuy);
        txtlucsat = findViewById(R.id.txtLucSat);
        txthoahai = findViewById(R.id.txtHoaHai);
        txtketluan = findViewById(R.id.txtKetLuan);
        btnHelp= findViewById(R.id.btnHelp);
    }

    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset()
    {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmployeeName(String namsinh) {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        //SQLiteDatabase database =PhongThuy2Activity.this.reada();
        Cursor cursor = null;
        namsinh =this.namsinh;
        try {
            cursor = database.rawQuery("SELECT * FROM AMLICH WHERE namsinh=?", new String[]{namsinh});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                tuoi = cursor.getString(cursor.getColumnIndex("tuoi"));
                menh = cursor.getString(cursor.getColumnIndex("menh"));
                cung = cursor.getString(cursor.getColumnIndex("cung"));
                quetrach = cursor.getString(cursor.getColumnIndex("quetrach"));
                sinhkhi = cursor.getString(cursor.getColumnIndex("sinhkhi"));
                thieny = cursor.getString(cursor.getColumnIndex("thieny"));
                diennien = cursor.getString(cursor.getColumnIndex("diennien"));
                phucvi = cursor.getString(cursor.getColumnIndex("phucvi"));
                tuyetmenh = cursor.getString(cursor.getColumnIndex("tuyetmenh"));
                nguquy = cursor.getString(cursor.getColumnIndex("nguquy"));
                lucsat = cursor.getString(cursor.getColumnIndex("lucsat"));
                hoahai = cursor.getString(cursor.getColumnIndex("hoahai"));
                ketluan = cursor.getString(cursor.getColumnIndex("ketluan"));
                setTextGiaoDien();

            }
            Log.d("trang", "trang: "+quetrach+"-"+ketluan+"-"+hoahai);

            return null;

        } finally {
            cursor.close();
        }


    }

    private void setTextGiaoDien() {
        txtnamsinh.setText(namsinh);
        txttuoi.setText(tuoi);
        txtmenh.setText(menh);
        txtcung.setText(cung);
        txtquetrach.setText(quetrach);
        txtsinhkhi.setText(sinhkhi);
        txtthieny.setText(thieny);
        txtdiennien.setText(diennien);
        txtphucvi.setText(phucvi);
        txttuyetmenh.setText(tuyetmenh);
        txtnguquy.setText(nguquy);
        txtlucsat.setText(lucsat);
        txthoahai.setText(hoahai);
        txtketluan.setText(ketluan);
    }

}
