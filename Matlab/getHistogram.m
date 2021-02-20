function [result] = getHistogram(data)
%Function to calculate a histogram of an image

i = 1;
result = zeros(256);

while(i <= length(data))
 result(data(i) + 1) = result(data(i) + 1) + 1;
 i = i + 1;
 end
