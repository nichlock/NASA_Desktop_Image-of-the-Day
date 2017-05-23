#~/bin/bash
#
# fetch image ot the day url from "nasa.gov Image of the day rss feed"
# and save the file as nasa.jpeg

FILE=`curl -s -o - https://www.nasa.gov/rss/dyn/lg_image_of_the_day.rss \
| grep enclosure \
| head -1 \
| sed -e "s/.*url=\"http:/https:/g" -e "s/\" length.*//g"`

curl --output nasa.jpg $FILE
