function [result] = t1normamp(f)

f(:, 1) = f(:, 1) / max(abs(f(:, 1)));
f(:, 2) = f(:, 2) / max(abs(f(:, 2)));
result = f;
