function centroid = t2centroid(f, m)

%Centroid
centroid = sum(f .* m) / sum(m);
