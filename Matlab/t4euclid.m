%Calculate Euclidean distance of 2 vectors of arbitrary length
function distance = euclid(vector1, vector2)

size = min(length(vector1), length(vector2));
distance = 0;

for i = 1:1:size
  distance = distance + (vector1(i) - vector2(i)) .^ 2;
end

distance = sqrt(distance);
