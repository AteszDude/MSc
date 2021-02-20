function [dist] = getColorDistance(resultset1, set2)
dist = zeros(max(size(resultset1(:,192))), 1);
 for i = 1:1:max(size(resultset1(:,192)))
 for j = 1:1:max(size(set2(:,192)))
 d = sum(abs(resultset1(i, :) - set2(j, :)));
 if(d < dist(i))
   dist(i) = d;
 elseif(j == 1)
   dist(i) = d;
 end
 end
 end
 