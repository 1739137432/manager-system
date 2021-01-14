import numpy as np
import cv2
import random

img = cv2.imread("fuzzy.png",1)

gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
blur = cv2.GaussianBlur(gray,(5,5),5)
thresh = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 435, 17)
contours, n2 = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
filtered = []
for c in contours:
	if cv2.contourArea(c) < 1000:
		continue
	filtered.append(c)
	
objects = np.zeros([img.shape[0],img.shape[1],3], 'uint8')
colors = [[100,100,100],[150,150,150],[200,200,200],[0,0,0]]
j=0
for c in filtered:
	# cv2.drawContours(objects, [c], -1, (random.randint(0, 100),random.randint(0, 100),random.randint(0, 100)),-1)
	cv2.drawContours(objects, [c], -1, colors[j],-1)
	j=j+1
	area = cv2.contourArea(c)
	p = cv2.arcLength(c,True)

cv2.imshow("Contours",objects)
cv2.waitKey(0)
cv2.destroyAllWindows()