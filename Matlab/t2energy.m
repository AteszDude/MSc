function spow = t2energy(f)

%Energy
energy = sum(f .* f);

%Signal power
spow = energy / length(f);
