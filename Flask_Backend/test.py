from SingingSynthesis.PoetrySinger import PoetrySinger
from concurrent.futures import ThreadPoolExecutor
# import tensorflow as tf
import time

executor = ThreadPoolExecutor(10)

def thread(txt,txt2):
    time.sleep(3)
    print(txt,txt2)


if __name__ == '__main__':
    # singer = PoetrySinger()
    # r = singer.songSynthesis('啦啦' , '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦' )
    # print(r)
    executor.submit(thread,'Hello','Hi')
    while True:
        pass