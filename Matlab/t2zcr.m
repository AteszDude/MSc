function zerocrossing = t2zcr(f)

%Zero crossing feature
zerocrossing = 0;
for i = 2:1:length(f)
 if f(i) >= 0 && f(i - 1) < 0
   zerocrossing = zerocrossing + 1;
 elseif f(i) < 0 && f(i - 1) >= 0
   zerocrossing = zerocrossing + 1;
end
end