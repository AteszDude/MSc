function[result] = getXYtask3(act, cuts);
%Feature 1: get a representative mean colour: use the Y component of YcBcR
cuts = [0 cuts];
y = 0;

nFrames = act.NumberOfFrames;

%Get midframes
i = 1;
midframes = [];

while(i < max(size(cuts)))
  if(cuts(i + 1) < cuts(i) || cuts(i + 1) == 0)
    break;%Error or end
  end
  midframes = [midframes floor((cuts(i) + cuts(i + 1)) / 2)];
  i = i + 1;
end


for j = 1:1:max(size(midframes))

vFrame = read(act, midframes(j));

YCBCR = rgb2ycbcr(vFrame);
im = YCBCR(:, 1);
y = y + sum(im) / max(size(midframes));
end

%Feature 2: get average shot length
shotlength = double(nFrames / max(size(cuts)));


%Feature 3: get color deviation
 % dev = y;
 % diffs = [];

 % for j = 1:1:max(size(midframes))

 % vFrame = read(act, midframes(j));

 % YCBCR = rgb2ycbcr(vFrame);
 % im = YCBCR(:, 1);
 % diffs = [diffs (dev - sum(im)/ max(size(midframes))) ^ 2];
 
 % end

 % dev = sqrt(sum(diffs) / max(size(midframes)));
 
%Return whatever you like
result = [y shotlength];