from SingingSynthesis.SingingSynthesizer import SingingSynthesizer
from SingingSynthesis.utils import txt2pinyin,getSong,blendWithBGM,create_dir_not_exist
import os
import uuid
from configs import config


class PoetrySinger:
    _fs = 44100  #采样率
    _voicesPaths = [
        'geping',
        'google_girl',
        'Trump',
    ]
    
    _midFiles = [
        '杨花落尽子规啼.mid',
        'happysong.mid',
    ]

    _bgmFiles = [
        '民谣1(舒缓).wav',
        'piano.wav',
    ]

    _resultRoot = os.path.join( config.SingingSynthesis['output'] , 'PoetrySongs' )

    _blendFactor = [ 0.3 , 1.0  ]

    def __init__(self):
        self.synthesizer = SingingSynthesizer(self._fs)


    def songSynthesis(self , title , content , voice = 0 , midi = 0 , bgm = 0):

        MID_FILE = os.path.join( config.SingingSynthesis['midi'] , self._midFiles[midi] )
        VOICE_ROOT = os.path.join( config.SingingSynthesis['voice'] , self._voicesPaths[voice] )
        BGM_PATH = os.path.join( config.SingingSynthesis['bgm'] , self._bgmFiles[bgm] )

        lyrics = txt2pinyin(content)
        lyrics = [ p[0] for p in lyrics ]

        song = getSong(MID_FILE)
        
        str_uuid = ''.join(str(uuid.uuid4()).split('-'))
        fileName = title +'_' + str_uuid
        fileName_blend = title + '_blend_' +str_uuid
        RESULT_PATH = os.path.join(self._resultRoot,fileName + '.wav')

        RESULT_BLEND_PATH = os.path.join(self._resultRoot,fileName_blend + '.wav')

        create_dir_not_exist(RESULT_PATH)
        create_dir_not_exist(RESULT_BLEND_PATH)

        self.synthesizer.voiceSynthesis(VOICE_ROOT,lyrics,song)
        self.synthesizer.saveVoice(RESULT_PATH)

        blendWithBGM(RESULT_PATH,BGM_PATH,self._fs,RESULT_BLEND_PATH,self._blendFactor)

        return fileName , fileName_blend



if __name__=='__main__':
    singer = PoetrySinger()
    r = singer.songSynthesis('长亭外','杨花落尽子规啼',0,0,0)
    print(r)