from bs4 import BeautifulSoup
import json
import requests


class Scraper():
    '''
    Scraper class used to scrape goodreads website and store book abstract
    and data  as JSON serialized objects.
    '''
    
    def __init__(self, url):
        '''
        Initialize Scraper class with url to scrape.
        '''
        self.url = url
    
    class book():
        '''
        Book class used to store book data.
        '''
        def __init__(self, title, authors, ranking, ranking_count, abstract, part_of_series, genre_list):
            '''
            Initialize book class with book data.
            '''
            self.title = title
            self.authors = authors
            self.ranking = ranking
            self.ranking_count = ranking_count
            self.abstract = abstract
            self.part_of_series = part_of_series
            self.genre_list = genre_list


    def scrape_to_json(self):
        '''
        Scrape goodreads website and return book abstract and data as
        JSON serialized objects.
        '''
        soup = self.get_soup() # Get soup object

        if soup is None:
            return None
        
        book_title, part_of_series = self.get_book_title(soup)
        
        book_authors = self.get_book_authors(soup) # Get book authors  
       
        book_ranking, ranking_count = self.get_book_ranking(soup) # Get book ranking
        
       
        book_abstract = self.get_book_abstract(soup) # Get book abstract
        
        genre_list = self.get_genre_list(soup) # Get genre list

        book_obj = self.book(book_title, book_authors, book_ranking, ranking_count, book_abstract, part_of_series, genre_list)
        
        return book_obj.__dict__

    def get_soup(self):
        '''
        Get soup object from url.
        '''
        
        response = requests.get(self.url)
        soup = BeautifulSoup(response.text, 'lxml')
        
        if soup is None:
            tries = 0
            while(soup is None and tries < 5):
                response = requests.get(self.url)
                soup = BeautifulSoup(response.text, 'lxml')
                tries += 1
        
        return soup

    def get_book_authors(self, soup):
        '''
        Get book authors from soup object.
        '''
        author_divs = soup.find("div", {"id":"bookAuthors"})
        author_list = [author.text.strip() for author in author_divs.find_all("a")]
        
        return author_list

    def get_book_ranking(self, soup):
        '''
        Returns the rank of the book as well as how many people have rated it
        '''
        ranking_div = soup.find("div", {"id":"bookMeta"})
        ranking = ranking_div.find("span", {"itemprop":"ratingValue"}).text.strip() # Extract ranking
        ranking_count = ranking_div.find("a", {"class":"gr-hyperlink"}).text.strip().split()[0].strip()  # Extract ranking count
        count_as_int = int(ranking_count.replace(",", ""))
        return (float(ranking), count_as_int)

    def get_book_abstract(self, soup):
        '''
        Get book abstract from soup object. return book abstract as string.
        or the string "no description" if the book abstract could not be found for some reason
        '''
        description_div = soup.find('div', {'class': 'readable stacked', 'id': 'description'})
        
        text_divs = description_div.find_all("span")
        try:
            description_text = ". ".join(list(text_divs[1].stripped_strings))#text_divs[1].prettify()
        except IndexError: # if no description at second index, which is usually the whole abstract

            try:
                description_text = ". ".join(list(text_divs[0].stripped_strings))# then take the first index
            except:
                description_text = "no description"
        return description_text.replace("\"", "")
     

    def get_book_title(self, soup):
        '''
        Get book title from soup object.
        '''
        title_divs = soup.find("div", {'id':'metacol', 'class':'last col'})
        title = title_divs.find("h1").text.strip()
        part_of_series = True if len(title_divs.find("h2").text) > 1 else False # Checks if there is any text indicating that the book is in a series
        
        return (title, part_of_series)

    def get_genre_list(self, soup):
        '''
        Get genre list from soup object. Returns the genre list in the correct order of most relevant genre to least 
        relevant genre.
        '''
        divs = soup.find_all("div", {"class":"elementList"})
        genre_list = [div.find("a").text for div in divs]
        processed_list = filter(lambda x: x != "", genre_list)
        seen = set()
        unique_list_with_order = [x for x in processed_list if x not in seen and not seen.add(x)]   
        return unique_list_with_order


scraper = Scraper("https://www.goodreads.com/book/show/12.The_Hunger_Games")



