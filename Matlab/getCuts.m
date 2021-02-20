function[cuts] = getCuts(vObj)
%Return a list of detected shot cuts
nFrames = vObj.NumberOfFrames;

%Init
i = 2;
result = zeros(nFrames, 1);

%Dilation using a square
se = strel('square', 3);

%Init i = 1 for optimization
vFrame = read(vObj, 1);
BW2 = edge(rgb2gray(vFrame), 'canny');
iBW2 = 1 - BW2;
s2 = size(find(BW2), 1);
dBW2 = imdilate(BW2, se);

%Source: http://www.cis.temple.edu/~latecki/Courses/CIS750-03/Lectures/_vti_cnf/
while(i < nFrames)
vFrame2 = read(vObj, i);

%Canny edge detection using the gray scale image
BW1 = BW2;
BW2 = edge(rgb2gray(vFrame2), 'canny');

%Inverted images
iBW1 = iBW2;
iBW2 = 1 - BW2;

s1 = s2;
s2 = size(find(BW2), 1);

%Dilation
dBW1 = dBW2;
dBW2 = imdilate(BW2, se);

imIn = dBW1 & iBW2;
imOut = dBW2 & iBW1;

ECRIn = size(find(imIn), 1) / s2;
ECROut = size(find(imOut), 1) / s1;

diff = max(ECRIn, ECROut);

result(i) = diff;

i = i + 1;
%Display current state in percentage
%disp(sprintf('%i%% complete\n', round(100 * i / nFrames)));
end


%Constant thresholding
i = 1;
threshold = 4;
cuts = [];

while(i < max(size(result)) - 1)
if(result(i) > threshold)
  cuts = [cuts; i, result(i)];
  end
 i = i + 1;
end