import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time

URL = "https://shikimori.one/animes/order-by/ranked_random"
browser = webdriver.Chrome(executable_path="C:/Users/79279/Downloads/chromedriver_win32/chromedriver.exe")
browser.get(URL)
time.sleep(5)
page_downs = 10

body = browser.find_element_by_tag_name("body");
for i in range(page_downs):
    body.send_keys(Keys.PAGE_DOWN)
    time.sleep(0.5)

articles = browser.find_elements_by_tag_name("article");

titles = {'Название':[], 'Жанры':[], 'Описание': [], 'Рейтинг':[], 'Изображение':[]}

for article in articles:
    url = article.find_element_by_tag_name("img").get_attribute("src");
    link = article.find_element_by_xpath(".//a[@class='cover anime-tooltip-processed']").get_attribute("href")
    browser2 = webdriver.Chrome(executable_path="C:/Users/79279/Downloads/chromedriver_win32/chromedriver.exe")
    
    browser2.get(link)

    time.sleep(2)
    elem = browser2.find_element_by_tag_name("body")
    name = browser2.find_element_by_tag_name("h1").text
    try:
        rating = browser2.find_element_by_xpath(".//div[contains(@class,'score-value score')]").text
    except:
        rating = None
    web_genres = browser2.find_elements_by_xpath("//span[@class='genre-ru']")
    genres = web_genres[0].text
    for genre in web_genres[1:]:
        genres += ' ' +genre.text 
    try: 
        description = browser2.find_element_by_xpath("//div[@class='b-text_with_paragraphs']").text
    except:
        description = None

    titles['Название'].append(name)
    titles['Жанры'].append(genres)
    titles['Описание'].append(description)
    titles['Рейтинг'].append(rating)
    titles['Изображение'].append(url)

    browser2.close()
    time.sleep(1) 

browser.close()
titles = pd.DataFrame.from_dict(titles)
titles.to_csv('titles.csv')