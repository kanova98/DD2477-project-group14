<template>
    <button @click="recommend()" class="btn" >
        Get Recommendation
    </button>
</template>

<script>


export default {
    name: 'Button',
    methods: {
        recommend() {
            console.log("Clicked the button")
        }
    }
}
</script>
