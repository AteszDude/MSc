function[result] = getColorLayout(vFrame)
%Feature 1: deviation in colour histogram
%Return a color layout of the input image/video frame
A = size(vFrame);
imgx = A(1);
imgy = A(2);
clear A;

%Step 1: make it into 8x8 representative pixels
blocknum = 8;
blocksizex = floor(imgx / blocknum);
blocksizey =  floor(imgy / blocknum);
blocksize = blocksizex * blocksizey;
im8x8 = zeros(8, 8, 3);

for bx = 1:1:blocknum
    for by = 1:1:blocknum
	%Process a block
	for x = 1:1:blocksizex
	for y = 1:1:blocksizey
    im8x8(bx, by, 1) = im8x8(bx, by, 1) + int32(vFrame( (bx - 1) * blocksizex + x, (by - 1) * blocksizey + y, 1 ));
	im8x8(bx, by, 2) = im8x8(bx, by, 2) + int32(vFrame( (bx - 1) * blocksizex + x, (by - 1) * blocksizey + y, 2 ));
	im8x8(bx, by, 3) = im8x8(bx, by, 3) + int32(vFrame( (bx - 1) * blocksizex + x, (by - 1) * blocksizey + y, 3 ));
	end
	end
	im8x8(bx, by, 1) = floor(im8x8(bx, by, 1) / blocksize);
	im8x8(bx, by, 2) = floor(im8x8(bx, by, 2) / blocksize);
	im8x8(bx, by, 3) = floor(im8x8(bx, by, 3) / blocksize);
	%End of block
    end
end

im8x8 = uint8(im8x8);%This needed to convert it to an image
%imshow(im8x8)

%Step 2: YCBCR Transformation
YCBCR = rgb2ycbcr(im8x8);

%Step 3: DCT Transformation and Zigzag scanning
DY = zigzag(dct(YCBCR(:, :, 1)));
DCb = zigzag(dct(YCBCR(:, :, 2)));
DCr = zigzag(dct(YCBCR(:, :, 3)));

result = [DY DCb DCr];
%END OF FEATURE 1