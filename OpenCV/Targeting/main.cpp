//
//  main.cpp
//  targeting
//
//  Created by Attila Torda on 08/05/2012.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include <opencv2/opencv.hpp>
#include <opencv/highgui.h>
#include <math.h>

#define PI 3.14159265

using namespace std;
using namespace cv;

void lineintersection(int *p1, int *p2, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4);
double lnangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4);

int main(int argc, char** argv)
{
    Mat img, gray, img2, ln;
    if( argc != 2 || !(img=imread(argv[1], 1)).data)
        return -1;
    
    
    //1. do the circle
    cvtColor( img, gray, CV_BGR2GRAY );

    //smooth it, otherwise a lot of false circles may be detected
    GaussianBlur( gray, gray, Size(9, 9), 2, 2 );
    vector<Vec3f> circles;
    HoughCircles(gray, circles, CV_HOUGH_GRADIENT,
                 1, gray.rows/4, 200, 100 );
    for( size_t i = 0; i < circles.size(); i++ )
    {
        Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
        int radius = cvRound(circles[i][2]);
        // draw the circle center
        circle( img, center, 3, Scalar(0,255,0), -1, 8, 0 );
        // draw the circle outline
        circle( img, center, radius, Scalar(120,0,255), 3, 8, 0 );
    }
    
    
    //2. do the lines
    img2=imread(argv[1], 0);
    Canny(img2, ln, 50, 200, 3 );
    vector<Vec4i> lines;
    HoughLinesP(ln, lines, 1, CV_PI/180, 300, 30, 10 );
    for( size_t i = 0; i < lines.size(); i++ )
    {
        line( img, Point(lines[i][0], lines[i][1]),
             Point(lines[i][2], lines[i][3]), Scalar(255,40,120), 3, 8 );
        
    }

    if(lines.size() > 1)
    //3. calculate the intersection of two lines
    for(int i = 0; i < lines.size() - 1; i++)
    {
        int x;
        int y;
        Point p1 = Point(lines[i][0], lines[i][1]);
        Point p2 = Point(lines[i][2], lines[i][3]);

    for(int j = i; j < lines.size(); j++)
        {
        Point p3 = Point(lines[j][0], lines[j][1]);
        Point p4 = Point(lines[j][2], lines[j][3]);
        double angle = lnangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
        //if(angle > 15 && angle < 165)
        {
           lineintersection(&x, &y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);

        //lineintersection(&x, &y, lines[0][0], lines[0][1], lines[0][2], lines[0][3],
          //               lines[1][0], lines[1][1], lines[1][2], lines[1][3]);
        circle( img, Point(x, y), 4, Scalar(120,120,30), -1, 8, 0 );
        }
        }

    }

    lnangle(0, 0, 10, 10, 10, 10, 20, 0);//test

    namedWindow( "inbetween", 1 );
    imshow( "inbetween", gray );
    
    namedWindow( "circles", 1 );
    imshow( "circles", img );
    cvWaitKey(0);
    return 0;
}

double lnangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
{
    double angle = 0;
    double div1 = (x2 - x1);
    double div2 = (x4 - x3);
    if(div1 == 0 || div2 == 0) return 0;
    double m1 = atan ((y2 - y1) / div1);
    double m2 = atan ((y4 - y3) / div2);
    m1 *= 180 / PI;
    m2 *= 180 / PI;
    
    if(m1 < 0) m1 = 360 + m1;
    if(m2 < 0) m2 = 360 + m2;
    
    angle = m1 - m2;
    
    if(angle < 0) angle = 360 + angle;
    if(angle > 180) angle -= 180;
        
    return angle;
}


void lineintersection(int *p1, int *p2, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
{
    int det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
    if(det == 0)
        return;
    *p1 = (((x1 * y2 - y1 * x2) * (x3 - x4)) - ((x1 - x2) * (x3 * y4 - y3 * x4))) / det;
    *p2 = (((x1 * y2 - y1 * x2) * (y3 - y4)) - ((y1 - y2) * (x3 * y4 - y3 * x4))) / det;
    return;
}

