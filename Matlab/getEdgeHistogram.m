function[result] = getEdgeHistogram(vFrame)
%Feature 2: Edge Histogram
%Create a histogram of gradients
%http://www.dcc.fc.up.pt/~mcoimbra/lectures/VC_1112/VC_1112_P8_LEH.pdf
BW = int32(rgb2gray(vFrame));

A = size(BW);
imgx = A(1);
imgy = A(2);
clear A;

blocknum = 4;%We want a 4x4 division
blocksizex = floor(imgx / blocknum);
blocksizey =  floor(imgy / blocknum);
blocksize = blocksizex * blocksizey;

result = zeros(5 * 16, 1);%The resulting histogram of edges
rx = 1:1:5 * 16;
i = 1;
srqrt2 = sqrt(2);

for imx = 1:1:blocknum
    for imy = 1:1:blocknum
	%Process a block
	for bx = 1:2:blocksizex
	for by = 1:2:blocksizey
    %Process a subblock
    topleft = BW((imx - 1) * blocksizex + bx, (imy - 1) * blocksizey + by);
    topright = BW((imx - 1) * blocksizex + bx + 1, (imy - 1) * blocksizey + by);
    bottomleft = BW((imx - 1) * blocksizex + bx, (imy - 1) * blocksizey + by + 1);
    bottomright = BW((imx - 1) * blocksizex + bx + 1, (imy - 1) * blocksizey + by + 1);
	
	B = [topleft topright; bottomleft bottomright];
	vertical = abs(topleft - topright + bottomleft - bottomright);
	horizontal = abs(topleft + topright - bottomleft - bottomright);
	dia45 = floor( abs (srqrt2 * double(topleft) - srqrt2 * double(bottomright)) ) ;
	dia135 = floor( abs (srqrt2 * double(topright) - srqrt2 * double(bottomleft)) )  ;
	nond = abs(2 * topleft - 2 * topright - 2 * bottomleft + 2 * bottomright);
	maxvalue = max([vertical horizontal dia45 dia135 nond]);
	
	if vertical == maxvalue
	  result(i) = result(i) + 1;
	elseif horizontal == maxvalue
	  result(i + 1) = result(i + 1) + 1;
	elseif dia45 == maxvalue
	  result(i + 2) = result(i + 2) + 1;
	elseif dia135 == maxvalue
	  result(i + 3) = result(i + 3) + 1;
	else %nond
	  result(i + 4) = result(i + 4) + 1;
	end

	
	%End of subblock
	end
	end
	i = i + 5;
	%End of block
    end
end

%hist(rx, result, 'x') Plotting doesnt work!