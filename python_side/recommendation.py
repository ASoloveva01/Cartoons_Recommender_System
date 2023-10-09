import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import nltk
import re

def get_recommendations(favorites, titles, doc_sims):
    similar_titles = np.zeros(0)
    
    for favorite in favorites:
        title_similarities = doc_sims.iloc[int(float(favorite))-1].values
        similar_title_idxs = np.argsort(-title_similarities)[1:6]
        similar_titles = np.r_[similar_titles, titles[similar_title_idxs]]
    return np.setdiff1d(np.unique(similar_titles), favorites)

def normalize_document(doc):
    stop_words = nltk.corpus.stopwords.words('russian')
    doc = re.sub(r'[^а-яА-Я0-9\s]', '', doc, re.I|re.A)
    doc = doc.lower()
    doc = doc.strip()
                
    tokens = nltk.word_tokenize(doc)
    filtered_tokens = [token for token in tokens if token not in stop_words]
    doc = ' '.join(filtered_tokens)
    return doc
titles = pd.read_csv('titles.csv')
normalize_corpus = np.vectorize(normalize_document)
descriptions = list(titles['Описание'])
descriptions = [x for x in descriptions if str(x) != 'nan']
norm_corpus = normalize_corpus(descriptions)

tf = TfidfVectorizer(ngram_range=(1, 2), min_df=2)
tfidf_matrix = tf.fit_transform(norm_corpus)

doc_sim = cosine_similarity(tfidf_matrix)
doc_sim_df = pd.DataFrame(doc_sim)
doc_sim_df.to_csv('doc_similarities.csv')