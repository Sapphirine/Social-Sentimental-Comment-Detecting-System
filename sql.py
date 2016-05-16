import MySQLdb
import string

def connect_db():
    conn = MySQLdb.connect("localhost","root","root","tweets")
    return conn

def build(conn):
    c = conn.cursor()
    c.execute('''CREATE TABLE tweets
                     (id int, time text, content text, category text, sentiment text)''')
    conn.commit()

def cclose(conn):
    conn.commit()
    conn.close()

def get(conn, text, category, emotion):
    c = conn.cursor()
    c.execute("SELECT content FROM tweets WHERE content LIKE %s and category = %s and sentiment = %s", ("%" + text + "%",category, emotion))
    result = c.fetchall()
    return result

def getLen(conn, text, category, emotion):
    c = conn.cursor()
    result = []
    c.execute("SELECT * FROM tweets WHERE content LIKE %s and category = %s and sentiment = %s", ("%" + text + "%",category, emotion))
    result = c.fetchall()
    #for row in c.execute('SELECT * FROM tweets'):
    #for row in c:
    #    if((text in row[2]) and
    #      (category in row[3]) and
    #       (emotion in row[4])):
    #        result.append(row)
    #print len(result)
    return len(result)

def filter(conn, text1, text2):
    results= get(conn, text1, text2, 'negative') #negative
    return results

def filterLen(conn, text1, text2):
    results = [0,0,0]
    results[0] = getLen(conn, text1, text2, 'positive') #postive
    results[1] = getLen(conn, text1, text2, 'neutral')
    results[2] = getLen(conn, text1, text2, 'negative') #negative
    return results

