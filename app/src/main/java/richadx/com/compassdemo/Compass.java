package richadx.com.compassdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;


import com.jraska.falcon.Falcon;
import com.squareup.okhttp.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import github.nisrulz.lantern.Lantern;


import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by Goku on 3/26/2018.
 */

public class Compass extends AppCompatActivity implements SensorEventListener, LocationListener {
    private ImageView compass_img, txtSetting, imgDoiLaBan, imgPhongThuy;
    private TextView txt_compass;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;


    //adrichadx
    private PublisherAdView mPublisherAdView;
    private ImageView imChucNang2, popup;
    PublisherInterstitialAd publisherInterstitialAd;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    private TextView tvdiachi1;
    private Button click;


    private TextView txtKinhdo, txtVido;

    public static final String KEY_PREF_LANGUAGE = "key_font";
    //CustomToggle
    private static final int REQUEST_CODE = 50;
    private Lantern lantern;
    private String tenLuuTru = "trangThaiSetting";

    public static final int MY_PERMISSIONS_REQUEST_LOCATION=222;
    //

    String url = "http://www.androidtutorialpoint.com/";
    File imagePath;
    Bitmap bitmap;
    //Capture
    private ImageView Capture;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mPublisherAdView = new PublisherAdView(this);
        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.img_compass);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        txtSetting = findViewById(R.id.txtSetting);
        imChucNang2 = findViewById(R.id.imChucNang2);
        tvdiachi1 = findViewById(R.id.imChucNang1);
        txtVido = findViewById(R.id.txtVido);
        txtKinhdo = findViewById(R.id.txtKinhdo);


        Capture = findViewById(R.id.Capture);


        dfp();
        checkPermission();

        popup = findViewById(R.id.imChucNang2);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Compass.this, PopupThongTin.class);
                startActivity(intent1);
            }
        });

        xuLySetting();
        start();
        interestitalAD();
        //preference


        //Flash
       xuLysToggle();


        //Location and permission
        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            setupLocation();
        }

        //phongthuy
        imgDoiLaBan = findViewById(R.id.imgDoiLaBan);
        imgPhongThuy = findViewById(R.id.imgPhongThuy);
        addEvents();


        //tạo shortcut
        if (!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)) {
            criarAtalho();
            getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).commit();
        }


        Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("aa", "onClick: " + v);
                takeScreenshot1();


            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(Compass.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) + ContextCompat
                .checkSelfPermission(Compass.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (Compass.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (Compass.this, Manifest.permission.CAMERA)) {

                Snackbar.make(Compass.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
        }
    }

    /**
     * Flashlight setup
     */
    @SuppressLint("MissingPermission")
    private void xuLysToggle() {
        ToggleButton toggleButton = findViewById(R.id.switch_flash);
        lantern = new Lantern(this)

                .observeLifecycle(this);

        if (!lantern.initTorch()) {
            // Request if permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // true
                    lantern.alwaysOnDisplay(true)

                            .enableTorchMode(true);



                } else {
                    //false
                    lantern.alwaysOnDisplay(false)

                            .enableTorchMode(false).pulse(false);
                }
            }
        });
    }
    public void takeScreenshot1() {
        File screenshotFile = getScreenshotFile();

        Falcon.takeScreenshot(this, screenshotFile);

        String message = "Chụp màn hình tới " + screenshotFile.getAbsolutePath();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();




        Uri uri = Uri.fromFile(screenshotFile);

        Log.d("dich", "takeScreenshot1: "+uri);
        Intent intent = new Intent(Compass.this,ScreenshotActivity.class);
        intent.putExtra("bitmap",uri.toString());
        startActivity(intent);
    }

    protected File getScreenshotFile() {
        File screenshotDirectory;
        try {
            screenshotDirectory = getScreenshotsDirectory(getApplicationContext());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS", Locale.getDefault());

        String screenshotName = dateFormat.format(new Date()) + ".png";
        return new File(screenshotDirectory, screenshotName);
    }

    private static File getScreenshotsDirectory(Context context) throws IllegalAccessException {
        String dirName = "screenshots_" + context.getPackageName();

        File rootDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            rootDir = context.getDir("screens", MODE_PRIVATE);
        }

        File directory = new File(rootDir, dirName);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalAccessException("Unable to create screenshot directory " + directory.getAbsolutePath());
            }
        }

        return directory;
    }


    /**
     * Sqlite setup labanphongthuy
     *
     */


    private void addEvents() {

        imgPhongThuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Compass.this , PhongThuyActivity.class);
                startActivity(intent2);
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();

        start();

    }

    /**
     * Ngôn ngũ
     */


    public void startSettingsActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    /**
     * Định vị kinh độ vĩ độ address
     * @param requestCode set quyen flash and location
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

       /* if (requestCode == REQUEST_CODE) {
            if (!lantern.initTorch()) { setupLocation();
                Toast.makeText(Compass.this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }*/
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        // write your logic here
                        setupLocation();
                        xuLysToggle();
                    } else {
                        Snackbar.make(Compass.this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
            }
        }
    }
    @SuppressLint({"MissingPermission", "WrongConstant"})
    private void setupLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, (LocationListener) this);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location == null) {
            Toast.makeText(getApplicationContext(), "GPS signal not found",
                    3000).show();
        }
        if (location != null) {
            Log.e("locatin", "location--" + location);

            Log.e("latitude at beginning",
                    "@@@@@@@@@@@@@@@" + location.getLatitude());
            onLocationChanged(location);
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        DecimalFormat df = new DecimalFormat("0.00");
        txtKinhdo.setText("Kinh độ : "+df.format(longitude) +"°");
        txtVido.setText("Vĩ độ : "+ df.format(latitude) +"°");


        Log.e("latitude", "latitude--" + latitude);

        try {
            Log.e("latitude", "inside latitude--" + latitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);





            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                if (address!=null){

                    tvdiachi1.setText(address + " " );

                }else {
                    tvdiachi1.setText("");
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void interestitalAD() {
        publisherInterstitialAd=new PublisherInterstitialAd(this);
        publisherInterstitialAd.setAdUnitId("/112517806/320561521086816");

        publisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
    }

    private void dfp() {

        mPublisherAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("admob", "onAdFailedToLoad: "+i);

            }
        });
    }

    private void xuLySetting() {
        txtSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publisherInterstitialAd.isLoaded()) {
                    publisherInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                startSettingsActivity();
            }
        });
    }

    /**
     * Compass setup
     */
    private void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);

        String where = getString(R.string.nw);

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = getString(R.string.n);
        if (mAzimuth < 350 && mAzimuth > 280)
            where = getString(R.string.nw);
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = getString(R.string.w);
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = getString(R.string.sw);
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = getString(R.string.s);
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = getString(R.string.se);
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = getString(R.string.e);
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = getString(R.string.ne);



        txt_compass.setText(mAzimuth + "° " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void stop() {
        if(haveSensor && haveSensor2){
            mSensorManager.unregisterListener(this,mAccelerometer);
            mSensorManager.unregisterListener(this,mMagnetometer);
        }
        else{
            if(haveSensor)
                mSensorManager.unregisterListener(this,mRotationV);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Tạo shortcut app
     */
    private void criarAtalho() {
        Intent shortcutIntent = new Intent(getApplicationContext(), Compass.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "nomeDaApp");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_coppy));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }





}
