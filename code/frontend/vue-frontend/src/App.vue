<template>
    <div class ="container">
      <Header title= "Book Recommendation Engine" />
      <Books :books = "books" />
    </div>
</template>

<script>
import Header from './components/Header.vue'
import Books from './components/Books.vue'

export default {
  name: 'App',
  components: {
    Header,
    Books,
  },
  data() {
    return {
      books: []
    }
  },
  created(){
    this.books = [
      {
            "title": "The Hunger Games",
            "authors": [
                "Suzanne Collins"
            ],
            "ranking": 4.32,
            "ranking_count": 7325115,
            "abstract": "Could you survive on your own in the wild, with every one out to make sure you don't live to see the morning?. In the ruins of a place once known as North America lies the nation of Panem, a shining Capitol surrounded by twelve outlying districts. The Capitol is harsh and cruel and keeps the districts in line by forcing them all to send one boy and one girl between the ages of twelve and eighteen to participate in the annual Hunger Games, a fight to the death on live TV.. Sixteen-year-old Katniss Everdeen, who lives alone with her mother and younger sister, regards it as a death sentence when she steps forward to take her sister's place in the Games. But Katniss has been close to dead before—and survival, for her, is second nature. Without really meaning to, she becomes a contender. But if she is to win, she will have to start making choices that weight survival against humanity and life against love.",
            "part_of_series": true,
            "genre_list": [
                "Young Adult",
                "Fiction",
                "Science Fiction",
                "Fantasy",
                "Romance",
                "Adventure",
                "Apocalyptic",
                "Audiobook"
            ]
        },
      {
            "title": "Harry Potter and the Order of the Phoenix",
            "authors": [
                "J.K. Rowling",
                "Mary GrandPré"
            ],
            "ranking": 4.5,
            "ranking_count": 2910837,
            "abstract": "There is a door at the end of a silent corridor. And it’s haunting Harry Pottter’s dreams. Why else would he be waking in the middle of the night, screaming in terror?. Harry has a lot on his mind for this, his fifth year at Hogwarts: a Defense Against the Dark Arts teacher with a personality like poisoned honey; a big surprise on the Gryffindor Quidditch team; and the looming terror of the Ordinary Wizarding Level exams. But all these things pale next to the growing threat of He-Who-Must-Not-Be-Named - a threat that neither the magical government nor the authorities at Hogwarts can stop.. As the grasp of darkness tightens, Harry must discover the true depth and strength of his friends, the importance of boundless loyalty, and the shocking price of unbearable sacrifice.. His fate depends on them all.",
            "part_of_series": true,
            "genre_list": [
                "Fantasy",
                "Young Adult",
                "Fiction",
                "Childrens",
                "Adventure",
                "Audiobook",
                "Classics",
                "Science Fiction Fantasy"
            ]
        },
      {
            "title": "To Kill a Mockingbird",
            "authors": [
                "Harper Lee"
            ],
            "ranking": 4.27,
            "ranking_count": 5229018,
            "abstract": "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it. To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.. Compassionate, dramatic, and deeply moving, To Kill A Mockingbird takes readers to the roots of human behavior - to innocence and experience, kindness and cruelty, love and hatred, humor and pathos. Now with over 18 million copies in print and translated into forty languages, this regional story by a young Alabama woman claims universal appeal. Harper Lee always considered her book to be a simple love story. Today it is regarded as a masterpiece of American literature.",
            "part_of_series": true,
            "genre_list": [
                "Classics",
                "Fiction",
                "Historical",
                "Academic",
                "Literature",
                "Young Adult",
                "Novels"
            ]
        },
    ]     
  }

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


