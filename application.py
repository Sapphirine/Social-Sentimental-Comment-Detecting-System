from flask import Flask
from flask import request
from flask import render_template
import sql as s


application = Flask(__name__)

@application.route('/', methods=['GET'])
@application.route('/hello', methods=['GET', 'POST'])
@application.route('/hello/<name>', methods=['GET', 'POST'])
def hello(name=None):
    return 'hello world!'


@application.route('/index', methods=['GET', 'POST'])
def index(name=None):
    if request.method == 'POST':
        c = s.connect_db()
        res = s.filterLen(c, request.form['product'], request.form['category'])
        res2 = s.filter(c, request.form['product'], request.form['category'])
        tweets = []
        #positive_text=[]
        #neutral_text=[]
        negative_text=[]

        print res
#return render_template('index.html', tweets=res, negative_text=res2[2])
        return render_template('index.html', tweets=res, negative_text=res2)
    else:
        return render_template('index.html', tweets=[], negative_text=[])

if __name__ == '__main__':
    application.debug = True
    application.run(host='0.0.0.0')
