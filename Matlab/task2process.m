function [returnlabel] = task2process(act, cuts)

load('task2database.mat');

act%Display current video

%Get a list of cuts, also contains the value at each cut
%cuts = [0 getCuts(act)];%0 is needed to get the first midframe
cuts = [0 cuts];

%Get midframes
i = 1;
midframes = [];

while(i < max(size(cuts)))
  if(cuts(i + 1) < cuts(i) || cuts(i + 1) == 0)
    break;%Error or end
  end
  midframes = [midframes floor((cuts(i) + cuts(i + 1)) / 2)];
  i = i + 1;
end


%Init result set
resultset1 = zeros(max(size(midframes)), 192);
resultset2 = zeros(80, 1);

%Do for each midframe
for j = 1:1:max(size(midframes))

vFrame = read(act, midframes(j));

%Get first feature, result: 1x192 double
result1 = getColorLayout(vFrame);
resultset1(j, :) = result1;

%Get second feature, result: 80x1 double
result2 = getEdgeHistogram(vFrame);
resultset2 = resultset2 +  result2(:, 1) / max(size(midframes));

%disp(sprintf('%i%% complete\n', round(100 * j / max(size(midframes)))));
end

%Compare features to each of the one in the database
label = zeros(5, 1);

%Feature 1: Color layouts

dist1 = sum(abs(getColorDistance2(resultset1, devil1))) / min([max(size(resultset1(:,192))) max(size(devil1(:,192)))]);
dist2 = sum(abs(getColorDistance2(resultset1, pulp1))) / min([max(size(resultset1(:,192))) max(size(pulp1(:,192)))]);
dist3 = sum(abs(getColorDistance2(resultset1, shemove1))) / min([max(size(resultset1(:,192))) max(size(shemove1(:,192)))]);
dist4 = sum(abs(getColorDistance2(resultset1, ldance1))) / min([max(size(resultset1(:,192))) max(size(ldance1(:,192)))]);
dist5 = sum(abs(getColorDistance2(resultset1, arc1))) / min([max(size(resultset1(:,192))) max(size(arc1(:,192)))]);

minvalue = sort([dist1 dist2 dist3 dist4 dist5]);

for i = 1:1:5
if(minvalue(i) == dist1)
 label(1) = label(1) + 5 - i;
elseif(minvalue(i) == dist2)
 label(2) = label(2) + 5 - i;
elseif(minvalue(i) == dist3)
 label(3) = label(3) + 5 - i;
elseif(minvalue(i) == dist4)
 label(4) = label(4) + 5 - i;
else%eh5
 label(5) = label(5) + 5 - i;
 end
end

disp('Feature 1:');
 
if(minvalue(1) == dist1)
disp('Devils Advocate');
elseif(minvalue(1) == dist2)
disp('Pulp Fiction');
elseif(minvalue(1) == dist3)
disp('How She Moves');
elseif(minvalue(1) == dist4)
disp('Last Dance');
else%eh5
disp('Arctic Tale');
 end

%Feature 2: Edge histograms
eh1 = sum(abs(resultset2 - devil2));
eh2 = sum(abs(resultset2 - pulp2));
eh3 = sum(abs(resultset2 - shemove2));
eh4 = sum(abs(resultset2 - ldance2));
eh5 = sum(abs(resultset2 - arc2));

minvalue = sort([eh1 eh2 eh3 eh4 eh5]);

for i = 1:1:5
if(minvalue(i) == eh1)
 label(1) = label(1) + 5 - i;
elseif(minvalue(i) == eh2)
 label(2) = label(2) + 5 - i;
elseif(minvalue(i) == eh3)
 label(3) = label(3) + 5 - i;
elseif(minvalue(i) == eh4)
 label(4) = label(4) + 5 - i;
else%eh5
 label(5) = label(5) + 5 - i;
 end
end
 
 disp('Feature 2:');
 
if(minvalue(1) == eh1)
disp('Devils Advocate');
elseif(minvalue(1) == eh2)
disp('Pulp Fiction');
elseif(minvalue(1) == eh3)
disp('How She Moves');
elseif(minvalue(1) == eh4)
disp('Last Dance');
else%eh5
disp('Arctic Tale');
 end
 
%Print result
 maxlabel = max(label);

 if(maxlabel == (label(1)))
 returnlabel = ('Devils Advocate')
 elseif(maxlabel == (label(2)))
 returnlabel = ('Pulp Fiction')
 elseif(maxlabel == (label(3)))
 returnlabel = ('How She Moves')
 elseif(maxlabel == (label(4)))
 returnlabel = ('Last Dance')
 else%eh5
 returnlabel = ('Arctic Tale')
 end

