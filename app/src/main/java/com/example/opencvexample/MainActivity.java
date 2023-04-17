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
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    Mat mat;
    Bitmap bitmap;

    // Define the size of the marker in centimeters and the size of the marker in pixels
    private static final double MARKER_SIZE_CM = 80.0;
    private double mMarkerSizePixels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "Loader SUCCESS");
        }


        ImageView imageView = findViewById(R.id.imageView);


        // Load the bitmap image
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aruco);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.niloy);
        imageView.setImageBitmap(bitmap);

        mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

//        Utils.matToBitmap(mat, bitmap);
//        imageView.setImageBitmap(bitmap);


        Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_1000);
        DetectorParameters parameters = DetectorParameters.create();

        Mat ids = new Mat();
        List<Mat> corners = new ArrayList<>();
        Aruco.detectMarkers(mat, dictionary, corners, ids, parameters);

        if (ids.total() > 0) {
            // ArUco marker(s) detected
            Log.e("OpenCV", String.valueOf(ids.total()) + " ArUco marker(s) detected");

            // Get the corner points of the marker
            List<Point> markerCorners = new ArrayList<Point>();
            for (int i = 0; i < corners.size(); i++) {
                markerCorners.add(new Point(corners.get(i).get(0, 0)));
                markerCorners.add(new Point(corners.get(i).get(1, 0)));
                markerCorners.add(new Point(corners.get(i).get(2, 0)));
                markerCorners.add(new Point(corners.get(i).get(3, 0)));
            }

            // Calculate the perimeter of the marker in pixels
            mMarkerSizePixels = Imgproc.arcLength(new MatOfPoint2f(markerCorners.toArray(new Point[markerCorners.size()])), true);


//            E/distancea2b:: 640.3381918955014 pixel
//            E/distancec2d:: 357.33037934102384 pixel
//            E/scaleFactor: 0.09625668
//            E/areaOnCanvas: 874800.0
//            E/totalCanvasArea: 2088720.0
//            E/scaleFactor: 0.09818182



            // Calculate the perimeter ratio of the marker in centimeters
            double perimeteRatio = (mMarkerSizePixels * 0.09818182) / MARKER_SIZE_CM;
            Log.e("perimeteRatio ", String.valueOf(perimeteRatio) + " cm");

            double distanceLength = (640.34 * 0.09818182)  / perimeteRatio;
            Log.e("distanceLength ", String.valueOf(distanceLength) + " cm");

            double distanceGrith = (357.33 * 0.09818182)  / perimeteRatio;
            Log.e("distanceGrith ", String.valueOf(distanceGrith) + " cm");

            double cowWeight = (distanceLength * distanceGrith * distanceGrith) / 660;

            int intValue = (int) Math.round(cowWeight * 10000);
            Log.e("cowWeight ", String.valueOf(intValue) + " kg");








        } else {
            // No ArUco marker detected
            Log.e("OpenCV", "No ArUco marker detected");
        }


    }
}