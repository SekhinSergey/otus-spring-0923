<template>
    <div class="col-12">
        <h1>Genres</h1>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(genre, index) in genres" v-bind:key="index">
                <td>{{ genre.id }}</td>
                <td>{{ genre.name }}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import apiService from '../services/api-service'
    export default {
        name: "Genres",
        data: function () {
            return {
                genres: null
            }
        },
        mounted: function () {
            this.getGenres();
        },
        methods: {
            getGenres: function () {
                apiService.getGenres()
                    .then(response => {
                        this.genres = response.data;
                    }).catch(error => {
                        // Not used
                });
            }
        }
    }
</script>
