<template>
    <header>
        <h1> {{title}} </h1>
        
    </header>
</template>

<script>
import Button from './Button.vue'

export default {
    name: 'Header',
    props:  {
        title: {
            type: String,
            required: true
        }
    },
}
</script>

<style scoped>
    header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
    }
    
</style>