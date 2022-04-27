<template>
    <button @click="recommend()" class="btn" >
        {{title}}
    </button>
</template>

<script>


export default {
    name: 'Button',
    methods: {
        recommend() {
            console.log("Clicked the button")
        }
    },
    props: {
        title: {
            type: String,
            required: true
        }
    },
}
</script>
