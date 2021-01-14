#cv2.imread
#cv2.imshow
#cv2.imwrite
import cv2
image = cv2.imread('dog.jpg', flags=1)
cv2.imshow('Example',image)
cv2.waitKey(0)