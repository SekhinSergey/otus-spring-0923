<template>
    <div class="col-12">
        <h1>Authors</h1>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(author, index) in authors" v-bind:key="index">
                <td>{{ author.id }}</td>
                <td>{{ author.fullName }}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import apiService from '../services/api-service'
    export default {
        name: "Authors",
        data: function () {
            return {
                authors: null
            }
        },
        mounted: function () {
            this.getAuthors();
        },
        methods: {
            getAuthors: function () {
                apiService.getAuthors()
                    .then(response => {
                        this.authors = response.data;
                    }).catch(error => {
                        // Not used
                });
            }
        }
    }
</script>
