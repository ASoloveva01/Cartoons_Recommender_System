import json
import pandas as pd
import numpy as np
from recommendation import get_recommendations
from flask import Flask, request, jsonify
import sys
from ast import literal_eval

app = Flask(__name__)
@app.route('/get_recommendations', methods=['POST'])
def send_recommendations():
    titles = pd.read_csv('titles.csv')
    doc_sim_df = pd.read_csv('doc_similarities.csv')
    request_data = request.get_json()

    favourites = json.loads(request_data['favorites'])
    recommendations = get_recommendations(favourites, titles['Номер'].values, doc_sim_df)

    return json.dumps({'recommendations':list(recommendations)})
if __name__ == '__main__':
    # run app in debug mode on port 5000
    app.run(debug=True, port=5000)