# NASA Image of the Day, on your Desktop!
This is a short Java program that, when run, writes [NASA's image of the day](https://www.nasa.gov/multimedia/imagegallery/iotd.html) to two image files. You can set those images to a slideshow that changes once a day. For more info, see my [instructable on this](https://www.instructables.com/id/NASA-Image-of-the-Day-As-Your-Desktop/).

# Use it

To use this, I have a shortcut to the Java program in my startup folder on my Windows 10 PC, so it automatially updates the image daily.

If you want the images stored in a location other than the JAR's directory, put the batch files in your startup folder instead, and modify the two paths listed at the top:
```batch
REM The runnable jar file
JAR_LOCATION=[JAR location]
REM The directory to store the images in. Don't use quotes here.
IMAGE_LOCATION=[where to save the image]
```

# What's Inside?

This wokrs by accessing NASA's IoTD RSS feed, getting the image location, then downloading the image to either the directory of the JAR file or the directory specified in the first argument to the file.
