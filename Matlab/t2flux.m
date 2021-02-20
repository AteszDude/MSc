function flux = t2flux(m)

%Normalize magnitude
m = m / max(m);

%Spectral flux
flux = 0;
for i = 2:1:length(m)
 flux = flux + (m(i) - m(i - 1)) ^ 2;
end

%Calculate average flux
flux = flux / length(m);
