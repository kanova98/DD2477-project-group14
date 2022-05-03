<template>
    <div class = "RecommendedBook">
        <h3>{{RecommendedBook.title}}</h3>
        <p>Authors: {{RecommendedBook.authors.join(", ")}}</p>
        <p>Rating: {{RecommendedBook.ranking}}</p>
        <button class="btn" @click="toggleAbstract()" >
            <label>  {{btnText}} </label>
        </button>
        <p v-if="showAbstract">
             {{RecommendedBook.abstractForBook}}
        </p>
        
    </div>
</template>

<script>

export default {
  
    name: "RecommendedBook",
    props: {
        RecommendedBook: {
            type: Object,
        }
    },
    data() {
        return {
            showAbstract: false,
            btnText: 'Show Abstract'
        }
    },
    methods: {
        toggleAbstract() {
            this.showAbstract = !this.showAbstract
            if (this.showAbstract) {
                this.btnText = "Hide Abstract"
            } else {
                this.btnText = "Show Abstract"
            }
            
        }
    }
}
</script>

<style >

.RecommendedBook {
    border: 1px solid black;
    padding: 10px;
    margin: 10px;
}

</style>