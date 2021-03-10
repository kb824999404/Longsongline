import os

class config:
    HOST = '0.0.0.0'
    PORT = '80'

    ROOT_PATH = r'F:\Projects\SITP\Longsongline_Backend'

    DATABASE_PATH = os.sep.join(ROOT_PATH.split(os.sep) + [ 'Data' ])

    STATIC_PATH = os.sep.join(ROOT_PATH.split(os.sep) + [ 'static'])

    UPLOAD_FOLDER = os.path.join( STATIC_PATH , 'upload' )

    ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

    PoetryGeneration = {

    }

    PoetrySearch = {
        'data': os.sep.join(ROOT_PATH.split(os.sep) + \
            [ 'PoetrySearch' , 'Data' , 'ccpc_test_v1.0.json' ]) ,
        'synonym': os.sep.join(ROOT_PATH.split(os.sep) + \
            [ 'PoetrySearch' , 'Data' , 'synonym.txt' ]) ,
    }

    SingingSynthesis = {
        'voice' : os.sep.join(ROOT_PATH.split(os.sep) + [ 'SingingSynthesis' , 'voices' ]) ,
        'midi' : os.sep.join(ROOT_PATH.split(os.sep) + [ 'SingingSynthesis' , 'inputs' , 'midi' ]) ,
        'bgm' : os.sep.join(ROOT_PATH.split(os.sep) + [ 'SingingSynthesis' , 'inputs' , 'bgm' ]) ,
        'output' : os.sep.join(ROOT_PATH.split(os.sep) + [ 'SingingSynthesis' , 'outputs' , ]) 
    }


