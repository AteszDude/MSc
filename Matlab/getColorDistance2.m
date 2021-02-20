function [dist] = getColorDistance(resultset1, set2)
minlength = min([max(size(resultset1(:,192))) max(size(set2(:,192)))]);
dist = zeros(minlength, 1);
 for i = 1:1:minlength
 dist(i) = sum(abs(resultset1(i, :) - set2(i, :)));
 end
 