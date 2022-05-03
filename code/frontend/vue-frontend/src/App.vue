<template>
    <div class ="container">
      <Header title= "Book Recommendation Engine" />
      <Button @click="showRecommendations" title = "Get Recommendations"/>
      <Button @click="showSearch" title = "Search for read books"/>
      <Search-for-books v-if="inSearchBooks" @search-books="getBooksFromBackend"/>
    </div>
    <Books v-if="inSearchBooks" @mark-read="markAndDelete" :books = "books" />
    <Recommended-books v-if="inRecommendations" :books = "recommendedBooks" />
</template>

<script>
import Header from './components/Header.vue'
import Books from './components/Books.vue'
import Button from './components/Button.vue'
import SearchForBooks from './components/SearchForBooks.vue'
import RecommendedBooks from './components/RecommendedBooks.vue'

export default {
  name: 'App',
  components: {
    Header,
    Books,
    Button,
    SearchForBooks,
    RecommendedBooks
  },
  data() {
    return {
      books: [],
      recommendedBooks: [],
      inSearchBooks: false,
      inRecommendations: false
    }
  },
  methods: {
    async markAndDelete(title){
      const response = await fetch(`http://localhost:9090/read/${title}`, {
        method: 'POST',
  
      })
      
      this.books = this.books.filter(book => book.title !== title)

      //"Want to send data to backend here as well"
    },
    showSearch(){
      this.inRecommendations = false
      this.inSearchBooks = true 
    },
    showRecommendations(){
      this.inSearchBooks = false
      this.inRecommendations = true
      this.getRecommendations()

    },
    /* 
    * Should return a list of books from the backend
    */
    async getRecommendations(){
      const response = await fetch(`http://localhost:9090/books/recommendations`, {
        method: 'GET',
      })
      if(response.body !== null){
        const data = await response.json()
        console.log(data)
        this.recommendedBooks = data
        
      }  
      
      //this.recommendedBooks = data
      
      console.log("hi")
      //return data
    },
    async getBooksFromBackend(search){
      const response = await fetch(`http://localhost:9090/books/${search}`)
      console.log(response)
      if(response.body !== null){
        const data = await response.json()
        console.log(data)
        this.books = data
      }   
    },


  },
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400&display=swap');
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
body {
  font-family: 'Poppins', sans-serif;
}
.container {
  max-width: 800px;
  margin: 30px auto;
  overflow: auto;
  min-height: 300px;
  border: 1px solid rgb(242, 246, 250);
  padding: 30px;
  border-radius: 5px;
}
.btn {
  display: inline-block;
  background: #000;
  color: #fff;
  border: none;
  padding: 10px 20px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  text-decoration: none;
  font-size: 15px;
  font-family: inherit;
}
.btn:focus {
  outline: none;
}
.btn:active {
  transform: scale(0.98);
}
.btn-block {
  display: block;
  width: 100%;
}
#app {
  
  align-items: center;
  text-align: center;
  margin-top: 20px;
}
</style>


