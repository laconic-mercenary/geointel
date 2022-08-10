#!/bin/python

import base64

with open("b64img.txt","rb") as txt:
    b64text = txt.read()
    with open("result.jpg", "wb") as img:
        img.write(base64.decodestring(b64text))
