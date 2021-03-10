import requests
import json
import random
import hashlib

from requests.api import request

class BaiduTranslator ():
    _host = "https://fanyi-api.baidu.com"

    _headers ={
        'Content-Type': 'application/x-www-form-urlencoded',
        'Cookie': 'BAIDUID=72AEE58E357FF20A89724210C3BC13D2:FG=1'
    }

    _url =  _host + '/api/trans/vip/translate'

    _appid = '20210306000717678'

    _token = 'sObVetvsS6Ug3gFinujL'

    _params = {
        'q': '' ,
        'from': 'auto',
        'to' : 'en',
        'appid' : _appid,
        'salt' : '',
        'sign' : ''
    }

    _maxSalt = 2<<15

    def __init__(self , src = 'auto', dest = 'en'):
        self._params['from'] = src
        self._params['to'] = dest

    def __calculateSign(self , text):
        salt = str(random.randint(0 , self._maxSalt))
        self._params['salt'] = salt
        sign = self._appid + text + salt + self._token
        hl = hashlib.md5()
        hl.update(sign.encode(encoding='utf-8'))
        self._params['sign'] = hl.hexdigest()


    def translate(self , text):
        self._params['q'] = text
        self.__calculateSign(text)
        result = ''
        try:
            res = requests.post(self._url,
                            headers = self._headers,
                            data = self._params)
            res.raise_for_status()
            jsonText = res.text
            if len(jsonText)>0:
                jsonResult = json.loads(jsonText)
                result += jsonResult['trans_result'][0]['dst']
            return result
        except Exception as ex:
            print('ERROR: ' + str(ex))
            return ''

if __name__ == '__main__':
    t2 = BaiduTranslator()
    result = t2.translate('你好')
    print(result)
