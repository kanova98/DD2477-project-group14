<template>
    <div v-if="this.books.length > 0" class="container">
        <h1>Which books have you read?</h1>
        <div :key="ReadBook.title" v-for="ReadBook in books">
            <ReadBook @mark-read="$emit('mark-read', ReadBook.title)" :ReadBook="ReadBook" />
        </div>
    </div>
</template>

<script>
import ReadBook from './ReadBook.vue'

export default {
    name: 'Books',
    props: {
        books: {
            type: Array,
        }
    },
    components: {
        ReadBook,
    },
    emits: ['mark-read']
     
}
</script>

<style>
</style>