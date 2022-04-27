<template>
    <div class="container">
        <h1>Your recommendations</h1>
        <div :key="RecommendedBook.title" v-for="RecommendedBook in books">
            <RecommendedBook :RecommendedBook="RecommendedBook" />
        </div>
    </div>
</template>

<script>
import RecommendedBook from './RecommendedBook.vue'

export default {
    name: 'Books',
    props: {
        books: {
            type: Array,
        }
    },
    components: {
        RecommendedBook,
    },
   
     
}
</script>

<style>
</style>