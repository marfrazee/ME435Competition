package edu.rosehulman.frazeema.conefinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class ConeFinderActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    public static final String TAG = "ConeFinder";
    /** References to the UI widgets used in this demo app. */
    private TextView mLeftRightLocationTextView, mTopBottomLocationTextView, mSizePercentageTextView;

    /** Constants and variables used by OpenCV4Android. Don't mess with these. ;) */
    private ColorBlobDetector mDetector;
    private Scalar CONTOUR_COLOR = new Scalar(0, 0, 255, 255);
    private CameraBridgeViewBase mOpenCvCameraView;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cone_finder);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLeftRightLocationTextView = findViewById(R.id.left_right_location_value);
        mTopBottomLocationTextView = findViewById(R.id.top_bottom_location_value);
        mSizePercentageTextView = findViewById(R.id.size_percentage_value);

        mOpenCvCameraView = findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            Log.d(TAG, "Everything should be fine with using the camera.");
        } else {
            Log.d(TAG, "Requesting permission to use the camera.");
            String[] CAMERA_PERMISSONS = {
                    Manifest.permission.CAMERA
            };
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSONS, 0);
        }
//        onImageRecComplete(true, -0.123, 0.456, 0.789);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    /** Displays the blob target info in the text views. */
    public void onImageRecComplete(boolean coneFound, double leftRightLocation, double topBottomLocation, double sizePercentage) {
        if (coneFound) {
            mLeftRightLocationTextView.setText(String.format("%.3f", leftRightLocation));
            mTopBottomLocationTextView.setText(String.format("%.3f", topBottomLocation));
            mSizePercentageTextView.setText(String.format("%.5f", sizePercentage));
        } else {
            mLeftRightLocationTextView.setText("---");
            mTopBottomLocationTextView.setText("---");
            mSizePercentageTextView.setText("---");
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mDetector = new ColorBlobDetector();

        // TODO: Add our stuff later to set the target color
    }

    @Override
    public void onCameraViewStopped() {
        //Intentionally left blank. Nothing needed here.
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        mDetector.process(rgba);

        List<MatOfPoint> contours = mDetector.getContours(); // For the outline
        Imgproc.drawContours(rgba, contours, -1, CONTOUR_COLOR);

        // TODO: Add more code later to find our cone!

        return rgba;
    }
}
