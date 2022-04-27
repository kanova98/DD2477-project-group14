<template>
    <form class="form">
        <div class="form-control">
            <h2 for="search">Find read books:</h2>
            <input type="text" class="form-control" id="search" v-model="search" placeholder="Search for a book">
        </div>
        <button type="submit" class="btn btn-default" @click="searchBooks">Search</button>
    </form>
</template>

<script>
export default {
    name: 'SearchForBooks',
    data() {
        return {
            search: '',
        }
    },
    methods:
    {
        searchBooks(event) {
            event.preventDefault()
            this.$emit('search-books', this.search)
            console.log("Searching for book {this.search}")
        }
    }


}
</script>

<style scoped>
    .form {
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .form-control {
        width: 50%;
    }
</style>