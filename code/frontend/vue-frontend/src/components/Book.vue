<template>
    <div class = "book">
        <h3>{{book.title}}</h3>
        <p>Authors: {{book.authors.join(", ")}}</p>
        <p>Rating: {{book.ranking}}</p>
        <p>Rating Count {{book.ranking_count}}</p>
        <p>Abstract: {{book.abstract}}</p>
        
    </div>
</template>

<script>
export default {
    name: "Book",
    props: {
        book: {
            type: Object,
        }
    },
}
</script>

<style >

.book {
    border: 1px solid black;
    padding: 10px;
    margin: 10px;
}

</style>