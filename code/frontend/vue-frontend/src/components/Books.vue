<template>
    <div :key="book.title" v-for="book in books">
        <Book :book="book" />
    </div>
    
</template>

<script>
import Book from './Book.vue'

export default {
    name: 'Books',
    props: {
        books: {
            type: Array,
        }
    },
    components: {
        Book,
    }
     
}
</script>