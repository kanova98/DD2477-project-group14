import Scraper as book_scraper
from bs4 import BeautifulSoup
import requests
import json
import urllib.parse

class DatasetGenerator:
    '''
    DatasetGenerator class used to generate dataset from scraped data.
    '''
    def __init__(self, starting_url):
        '''
        Initialize DatasetGenerator class with url. Scrape all hyperlinks that link to books from the url

        '''
        self.data = {"books": []}
        self.url = starting_url
        self.book_links = self.find_all_book_links()
        self.retrieve_data(self.book_links)

        with open("Data/dataset10.json", 'w') as f:
            json.dump(self.data, f, indent=4, ensure_ascii=False)
    
    def get_soup(self):
        '''
        Get soup object from url.
        '''
        response = requests.get(self.url)
        soup = BeautifulSoup(response.text, 'html.parser')
        return soup

    def find_all_book_links(self):
        '''
        Find all hyperlinks that link to books from the url
        '''
        base_url = "https://www.goodreads.com"
        soup = self.get_soup()
        book_links = soup.find_all('a', class_='bookTitle')
        relative_urls = [book_link.get('href') for book_link in book_links]
        full_urls = [urllib.parse.urljoin(base_url, relative_url) for relative_url in relative_urls]
        return full_urls 

    def retrieve_data(self, links):
        '''
        Retrieve data from links
        '''
        
        for index,book_url in enumerate(links):
            print(f"book nr: {index} with url: {book_url}")
            number_of_tries = 0
            book_data = None
            while number_of_tries < 5:
                try:
                    book_data = book_scraper.Scraper(book_url).scrape_to_json()
                    break    
                except:
                    print("Error scraping book")
                    number_of_tries += 1
                
            if book_data is not None:
                self.data["books"].append(book_data)
            


gen = DatasetGenerator("https://www.goodreads.com/list/show/1.Best_Books_Ever?page=10")    
