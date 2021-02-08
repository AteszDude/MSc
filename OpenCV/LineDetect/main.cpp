//
//  main.cpp
//  somelines
//
//  Created by Attila Torda on 05/05/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>
#include <fstream>

#include <opencv2/opencv.hpp>
#include <math.h>

#define PI 3.14159265

using namespace cv;
using namespace std;

double lnangle(int x1, int y1, int x2, int y2);

//reference: http://opencv.willowgarage.com/documentation/cpp/imgproc_feature_detection.html

int main(int argc, char** argv)
{
    Mat src, dst, color_dst, customim;
    if( argc != 2 || !(src=imread(argv[1], 0)).data)
        return -1;

    customim = imread(argv[1], 1);
    
    Canny( src, dst, 50, 200, 3 );
    cvtColor( dst, color_dst, CV_GRAY2BGR );
    

    vector<Vec4i> lines;
    HoughLinesP( dst, lines, 1, CV_PI/180, 375, 30, 10 );
    for( size_t i = 0; i < lines.size(); i++ )
    {
        line( color_dst, Point(lines[i][0], lines[i][1]),
             Point(lines[i][2], lines[i][3]), Scalar(0,0,255), 3, 8 );
        line( customim, Point(lines[i][0], lines[i][1]),
             Point(lines[i][2], lines[i][3]), Scalar(0,0,255), 3, 8 );
        if(lnangle(lines[i][0], lines[i][1], lines[i][2], lines[i][3]) < 5.0)
            line( customim, Point(0, lines[i][1]),
                 Point(customim.cols, lines[i][1]), Scalar(0,0,0), 1, 8);

    }
    
    namedWindow( "Source", 1 );
    imshow( "Source", src );
    
    namedWindow( "Detected Lines", 1 );
    imshow( "Detected Lines", color_dst );
    
    namedWindow( "Custom image", 1 );
    imshow( "Custom image", customim );
    
    //Write lines to a file
    ofstream myfile;
    myfile.open ("example.txt");
    for( size_t i = 0; i < lines.size() && i < 20; i++ )
    {
        myfile << "Line " << i << ": " << lines[i][0] << " " << lines[i][1] << " " <<
        lines[i][2] << " " << lines[i][3] << "\n";
    }
    myfile.close();
    
    waitKey(0);
    return 0;
}


double lnangle(int x1, int y1, int x2, int y2)
{
    double div1 = (x2 - x1);
    if(div1 == 0) return 0;
    double angle = atan ((y2 - y1) / div1);
    angle *= 180 / PI;
    
    if(angle < 0) angle = 360 + angle;
    
    if(angle >= 270 && angle <= 360)
        angle = 360 - angle;
    else if(angle < 270 && angle >= 180)
        angle = angle - 180;
    else if(angle < 180 && angle >= 90)
        angle = 180 - angle;

        
    return angle;
}

