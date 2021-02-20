%Read audio file
[f, sr] = wavread('01_DevilsAdvocate_04.wav');
N = length(f(:, 1));
t = linspace(0, N/sr, N);

subplot(2, 1, 1);
plot(t, f(:, 1))
axis([0 2 0 0.1])
title('Before')

%Downsample to 8000
newsr = 8000;
[f, sr] = t1resample(f, newsr, sr);

%Normalise amplitudes
f = t1normamp(f);

%Convert stereo to mono
f = t1mixchannels(f);


%Plot again to see the difference
subplot(2, 1, 2);
N = length(f);
t = linspace(0, N/sr, N);
plot(t, f)
axis([0 2 0 0.1])
title('After')

