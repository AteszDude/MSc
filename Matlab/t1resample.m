function [result, sr] = t1resample(f, newsr, sr)

result(:, 1) = resample(f(:, 1), newsr, sr);
result(:, 2) = resample(f(:, 2), newsr, sr);
sr = newsr;
