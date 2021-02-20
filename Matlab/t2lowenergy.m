function lowenergy = t2lowenergy(f, sr)

%Energy
energy = sum(f .* f);

%Binsize
bins = round((sr / 4) - 0.5);

%Average energy
avg = energy / bins;

lowenergy = 0;

%Go through all bins and check whether it's below average
i = 1;
while i + bins < length(f)
  if(sum (f (i:i + bins)) < avg)
    lowenergy = lowenergy + 1;
  end
  i = i + bins;
end


%Express in percentage

lowenergy = 100 * lowenergy / bins;

