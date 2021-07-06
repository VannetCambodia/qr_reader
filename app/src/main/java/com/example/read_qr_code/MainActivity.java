package com.example.read_qr_code;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.read_qr_code.databinding.ActivityMainBinding;
import org.jetbrains.annotations.NotNull;
import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private ScannerLiveView scannerLiveView;
    private TextView scanTV;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (checkPermission()){
            // if permission is already granted display a toast message
            Toast.makeText(getApplicationContext(),"Permission granted...",Toast.LENGTH_SHORT).show();
        }else{
            requestPermission();
        }

        scannerLiveView = mBinding.camera;
        scanTV = mBinding.tvScanned;

        scannerLiveView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // Method is called when scanner is started
                Toast.makeText(getApplicationContext(),"Scanner started",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // Method is called whe scanner is stop
                Toast.makeText(getApplicationContext(),"Scanner stopped",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // Method is called when scanner gives me erro
                Toast.makeText(getApplicationContext(),"Scanner Erro",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {
                // Method is called when camera scans
                // the qr code and the data from qr code is
                // stored in data in string format
                scanTV.setText(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        //0.5 is the area where we have to place red marker for scanning
        decoder.setScanAreaPercent(0.8);

        //below method will set decode to camera
        scannerLiveView.setDecoder(decoder);
        scannerLiveView.startScanner();
    }

    @Override
    protected void onPause() {
        // On app pause the camera will stop scanning
        scannerLiveView.stopScanner();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // This method is called when user allows the permission to use camera
        if(grantResults.length>0){
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if(cameraAccepted && vibrateAccepted){
                Toast.makeText(getApplicationContext(),"Permission granted...",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Permission Denied \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission(){
        // Here we are checking two permission that is vibrate
        // and Camera which is granted by user and not
        // if permission is granted then we are returning
        // true otherwise false
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(),VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void requestPermission(){
        // This method is to request the runtime permission
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA,VIBRATE},PERMISSION_REQUEST_CODE);
    }
}