package com.example.opencvexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    Mat mat;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) {
            Log.i("OpenCV", "Loader SUCCESS");
        }


        ImageView imageView = findViewById(R.id.imageView);


        // Load the bitmap image
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aruco);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ranu);
        imageView.setImageBitmap(bitmap);

        mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

        Utils.matToBitmap(mat, bitmap);
        imageView.setImageBitmap(bitmap);


        Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
        DetectorParameters parameters = DetectorParameters.create();

        Mat ids = new Mat();
        List<Mat> corners = new ArrayList<>();
        Aruco.detectMarkers(mat, dictionary, corners, ids, parameters);

        if (ids.total() > 0) {
            // ArUco marker(s) detected
            Log.i("OpenCV", String.valueOf(ids.total()) + " ArUco marker(s) detected");
        } else {
            // No ArUco marker detected
            Log.i("OpenCV", "No ArUco marker detected");
        }


    }
}