%Uncomment the one to be processed
nFrames = vObj.NumberOfFrames;

%Init
i = 1
result = zeros(nFrames, 1);

%Source: http://en.wikipedia.org/wiki/Sum_of_absolute_differences
while(i < nFrames)
vFrame = read(vObj, i);
vFrame2 = read(vObj, i + 1);

%Calculate and store the HISTOGRAM differences
red1 = getHistogram(vFrame(:, :, 1));
red2 = getHistogram(vFrame2(:, :, 1));
green1 = getHistogram(vFrame(:, :, 2));
green2 = getHistogram(vFrame2(:, :, 2));
blue1 = getHistogram(vFrame(:, :, 3));
blue2 = getHistogram(vFrame2(:, :, 3));


diff = red2 - red1;
difflist = reshape(diff,1,[]);
result(i) = sum(abs(difflist));

diff = green2 - green1;
difflist = reshape(diff,1,[]);
result(i) = result(i) + sum(abs(difflist));

diff = blue2 - blue1;
difflist = reshape(diff,1,[]);
result(i) = result(i) + sum(abs(difflist));

i = i + 1
end

%Constant thresholding
i = 1;
threshold = 3000;
cuts = [];

while(i < max(size(result)) - 1)
if(result(i) > threshold)
  disp(sprintf('cut detected at %i', i));
  cuts = [cuts; i, result(i)];
  end
 i = i + 1;
end

%Plot the result
plot(result)
hold all
%Plot the cuts
plot(cuts(:, 1), cuts(:, 2), 'x', 'Color', 'red');
plot([1 max(size(result)) - 1], [threshold threshold], 'Color', 'black');
hold off
