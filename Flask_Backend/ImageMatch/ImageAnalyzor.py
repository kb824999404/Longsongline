import requests,json,os,sys

class ImageAnalyzor:
    def __init__(self):
        #读取密钥和终结点
#         if 'COMPUTER_VISION_SUBSCRIPTION_KEY' in os.environ:
#             subscription_key = os.environ['COMPUTER_VISION_SUBSCRIPTION_KEY']
#             print('subscription_key: '+subscription_key)
#         else:
#             print("\nSet the COMPUTER_VISION_SUBSCRIPTION_KEY environment variable.\n**Restart your shell or IDE for changes to take effect.**")
#             sys.exit()
#         if 'COMPUTER_VISION_ENDPOINT' in os.environ:
#             endpoint = os.environ['COMPUTER_VISION_ENDPOINT']
#             print('endpoint: '+endpoint)
#         else:
#             print("\nSet the COMPUTER_VISION_ENDPOINT environment variable.\n**Restart your shell or IDE for changes to take effect.**")
#             sys.exit()     
        subscription_key='3760787f72444fc1b629a36c1c374069'
        endpoint='https://eastasia.api.cognitive.microsoft.com/'
        analyze_url=endpoint+"vision/v2.1/analyze"
        self.subscription_key=subscription_key
        self.endpoint=endpoint
        self.analyze_url=analyze_url
    
    def analyzeByUrl(self,image_url):
        headers={'Ocp-Apim-Subscription-Key':self.subscription_key}
        params={'visualFeatures':'Categories,Description,Color'}
        data={'url':image_url}
        respose=requests.post(self.analyze_url,headers=headers,params=params,json=data)
        respose.raise_for_status()

        analysis=respose.json()
#         print(json.dumps(analysis))
        return analysis
    
    def analyzeByLocal(self,image_path):
        image_data=open(image_path,"rb").read()
        headers={'Ocp-Apim-Subscription-Key':self.subscription_key,
            'Content-Type':'application/octet-stream'}
        params={'visualFeatures':'Categories,Description,Color'}
        respose=requests.post(self.analyze_url,headers=headers,params=params,data=image_data)
        respose.raise_for_status()

        analysis=respose.json()
#         print(json.dumps(analysis))
        return analysis


if __name__=='__main__':
    analyzor=ImageAnalyzor()

    # url=input("Please input the url of the image: ")
    path=input("Please input the path of the image: ")


    # analysis=analyzor.analyzeByUrl(url)
    analysis=analyzor.analyzeByLocal(path)
    print(json.dumps(analysis))
    image_caption=analysis["description"]["captions"][0]["text"].capitalize()

    print(image_caption)
