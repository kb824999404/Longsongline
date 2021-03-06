import warnings,os
warnings.filterwarnings("ignore")
os.environ["TF_CPP_MIN_LOG_LEVEL"]='2'
import tensorflow as tf
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)

from flask import Flask,request,render_template
from configs import config

from PoetryGeneration.generate import Generator
from PoetryGeneration.plan import Planner

from PoetrySearch.searcher import Searcher

from ImageMatch.ImageAnalyzor import ImageAnalyzor
from ImageMatch.BaiduTranslator import BaiduTranslator
from Database.UserManager import UserManager
from Database.MusicManager import MusicManager

from SingingSynthesis.PoetrySinger import PoetrySinger

import json,os,uuid
from concurrent.futures import ThreadPoolExecutor
import tensorflow as tf

executor = ThreadPoolExecutor(10)

app = Flask(__name__)

app.config['JSON_AS_ASCII'] = False





@app.route('/', methods=['POST','GET'])
def index():
    return '<h1>Hello!</h1>'

# 登录
@app.route('/login', methods=['POST'])
def login():
    response={'status':'false','message':'请求数据错误！'}
    if request.form['username'] and request.form['passwd']:
        username=request.form['username']
        pwd=request.form['passwd']
        response=userManager.login(username,pwd)
    return response

# 注册
@app.route('/register', methods=['POST'])
def register():
    response={'status':'false','message':'请求数据错误！'}
    if request.form['userId'] and request.form['username'] and request.form['passwd']:
        userId=request.form['userId']
        username=request.form['username']
        pwd=request.form['passwd']
        response=userManager.register(userId,username,pwd)
    return response

# 根据关键词生成古诗词
@app.route('/generatePoem', methods=['POST'])
def generatePoem():
    response={'status':'false'}
    if request.form['hints']:
        hints=request.form['hints']
        keywords = planner.plan(hints)
        print(keywords)

        poem = generator.generate(keywords)
        print(poem)
        data={'hints':hints,'keywords':keywords,'poem':poem}
        response={'status':'true','data':data}
    return response


# 根据一句话检索古诗词
@app.route('/searchPoem', methods=['POST'])
def searchPoem():
    response={'status':'false'}
    if request.form['description']:
        description=request.form['description']
        poem=searcher.solve(description)
        print(poem)
        data={'description':description,'poem':poem}
        response={'status':'true','data':data}
    return response


# 根据图片url检索诗词
@app.route('/searchPoemByImageUrl', methods=['POST'])
def searchPoemByImageUrl():
    response={'status':'false'}
    if request.form['image_url']:
        image_url=request.form['image_url']
        analysis=analyzor.analyzeByUrl(image_url)
        if "captions" in analysis["description"]:
            image_caption=analysis["description"]["captions"][0]["text"].capitalize()
            description = translator.translate(image_caption)
            poem=searcher.solve(description)
            print(poem)
            data={'description':description,'poem':poem}
            response={'status':'true','data':data}
    return response

# 古诗词配乐
@app.route('/songSynthesis', methods=['POST'])
def songSynthesis():
    response={'status':'false'}
    if request.form['title'] and request.form['content'] and request.form['voice'] \
        and request.form['midi'] and request.form['bgm'] and request.form['uid']:
        uid = request.form['uid']
        if not userManager._getUserByID(uid)==None:
            title = request.form['title']
            content = request.form['content']
            voice = int(request.form['voice'])
            midi = int(request.form['midi'])
            bgm = int(request.form['bgm'])
            executor.submit(thread_songSynthesis,title,content,voice,midi,bgm,uid)
            response={'status':'true'}
        else:
            response={'status':'false','message':'找不到该用户!'}
    return response

# 古诗词配乐线程
def thread_songSynthesis(title,content,voice,midi,bgm,uid):
    content_connect = ''.join(content.split('|'))
    print('Hello')
    result,id = musicManager.synthesisMusic(uid,title,content)
    print(result,id)
    if result==True and not id==-1:
        fileName , fileName_blend = singer.songSynthesis(title,content_connect,voice,midi,bgm)
        print(fileName,fileName_blend)
        if musicManager.synthesisDone(id,fileName_blend):
            print("Synthesis Done.")

# 获取用户合成的音乐
@app.route('/getAllMusic', methods=['POST'])
def getAllMusic():
    response={'status':'false'}
    if request.form['uid']:
        uid = request.form['uid']
        response = musicManager.getAllMusic(uid)
    return response

# 根据服务器图片路径检索诗词
@app.route('/searchPoemByImagePath', methods=['POST'])
def searchPoemByImagePath():
    response={'status':'false'}
    if request.form['image_path']:
        image_path=os.path.join(config.UPLOAD_FOLDER,request.form['image_path'])
        print(image_path)
        analysis=analyzor.analyzeByLocal(image_path)
        if "captions" in analysis["description"]:
            image_caption=analysis["description"]["captions"][0]["text"].capitalize()
            description = translator.translate(image_caption)
            poem=searcher.solve(description)
            print(poem)
            data={'description':description,'poem':poem}
            response={'status':'true','data':data}
    return response

# 上传图片
@app.route('/uploadImage', methods=['POST'])
def uploadImage():
    response={'status':'false'}
    if 'image' in request.files:
        img = request.files['image']
        if img and allowed_file(img.filename):
            uuid_str = uuid.uuid4().hex
            extension=img.filename.rsplit('.', 1)[1].lower()
            filename = uuid_str+'.'+extension
            img.save(os.path.join(config.UPLOAD_FOLDER, filename))
            response={'status':'true','filename':filename}
            
    return response


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in config.ALLOWED_EXTENSIONS

# 显示服务器图片
@app.route('/img/<imgname>')
def showImg(imgname):
    image_path = '/'.join(config.UPLOAD_FOLDER.split(os.path.sep)[-2:] + [imgname])
    return render_template('showImg.html', img_path=image_path)


#获取一首诗
@app.route('/getPoemDetail', methods=['POST'])
def getPoemDetail():
    response={'status':'false'}
    if request.form['index'] :
        poemIndex = request.form['index']
        poem = searcher.getPoem(int(poemIndex))
        response={'status':'true',"poem":poem}
    return response

#获取诗列表
@app.route('/getPoemList', methods=['POST'])
def getPoemList():
    response={'status':'false'}
    if request.form['start'] and request.form['number']:
        start = int(request.form['start'])
        number = int(request.form['number'])
        poemList = searcher.getPoemList(start,number)
        response={'status':'true','poemList':poemList}
    return response

#获取问题
@app.route('/getQuestonList', methods=['POST'])
def getQuestonList():
    response={'status':'false'}
    if request.form['index']:
        index = int(request.form['index'])
        questionList = searcher.getQuestion(index)
        response={'status':'true','questionList':questionList}
    return response

if __name__ == '__main__':
    planner = Planner()
    generator = Generator()
    searcher = Searcher()
    analyzor = ImageAnalyzor()
    userManager = UserManager()
    musicManager = MusicManager()
    translator = BaiduTranslator()
    singer = PoetrySinger()
    app.run(host=config.HOST,port=config.PORT)