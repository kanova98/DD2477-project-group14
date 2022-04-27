<template>
    <div class = "ReadBook">
        <h3>{{ReadBook.title}}</h3>
        <p>Authors: {{ReadBook.authors.join(", ")}}</p>
        <p>Rating: {{ReadBook.ranking}}</p>
        <p>Rating Count {{ReadBook.ranking_count}}</p>
        <button @click="markRead(ReadBook.title)" >
            Mark as Read
        </button>
    </div>
</template>

<script>

export default {
  
    name: "ReadBook",
    props: {
        ReadBook: {
            type: Object,
        }
    },
    methods: {
        markRead(title) {
            console.log(title)
            this.$emit('mark-read', title)
        }
    }
}
</script>

<style >

.ReadBook {
    border: 1px solid black;
    padding: 10px;
    margin: 10px;
}

</style>