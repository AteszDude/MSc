expectation = [
80
136
411
585
935
1003
1191
1388
1435
1526
];

observation = [
81
137
412
586
936
1527
];

observation = observation - 1;%Only used for edge detection

truepos =  sum(ismember(observation, expectation));
falsepos = max(size(ismember(observation, expectation))) - truepos;

falseneg = max(size(expectation)) - sum(ismember(expectation, observation));


precision = truepos / (truepos + falsepos)
recall = truepos / (truepos + falseneg)
