import os,json
from ImageAnalyzor import ImageAnalyzor
from GoogleTranslator import GoogleTranslator


if __name__ == "__main__":
    analyzor=ImageAnalyzor()

    url=input("Please input the url of the image: ")
    analysis=analyzor.analyzeByUrl(url)

#     path=input("Please input the path of the image: ")
#     analysis=analyzor.analyzeByLocal(path)

    print(json.dumps(analysis))
    if "captions" in analysis["description"]:
        image_caption=analysis["description"]["captions"][0]["text"].capitalize()

    print(image_caption)


    t2=GoogleTranslator(src='en',dest='zh-CN')
    result = t2.translate(image_caption)
    print(result)