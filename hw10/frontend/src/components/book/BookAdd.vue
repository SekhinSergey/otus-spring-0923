<template>
    <div class="col-12">
        <div class="form-group">
            <h1>Book adding</h1>
            <div>
                <label for="holder-input">Title:</label>
                <input id="holder-input" name="title" type="text" v-model="title"/>
            </div>
            <div>
                <label for="authorSelect">Choose author full name:</label>
                <select id="authorSelect" class="form-control" v-model="authorId">
                    <option v-for="author in authors" :value="author.id">{{ author.fullName }}</option>
                </select>
            </div>
            <div>
                <label for="genreSelect">Choose genre names:</label>
                <select multiple id="genreSelect" class="form-control" v-model="genreIds">
                    <option v-for="genre in genres" :value="genre.id">{{ genre.name }}</option>
                </select>
            </div>
            <div style="display: flex; gap: 12px; justify-content: center">
                <button class="btn btn-primary" v-on:click="addBook()">Save</button>
                <button class="btn btn-primary" v-on:click="$router.go(0)">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '@/services/api-service';

export default {
    name: "BookCreate",
    props: [
        'bookModel'
    ],
    data: function () {
        return {
            authors: {},
            genres: {},
            title: '',
            authorId: '',
            genreIds: []
        }
    },
    mounted: function () {
        this.authorId = this.bookModel.authorId;
        this.genreIds = this.bookModel.genreIds;
        this.title = this.bookModel.title;
        this.loadAuthors();
        this.loadGenres();
    },
    methods: {
        loadAuthors: function () {
            apiService.getAuthors()
                .then(response => {
                    this.authors = response.data;
                });
        },
        loadGenres: function () {
            apiService.getGenres()
                .then(response => {
                    this.genres = response.data;
                });
        },
        addBook: function () {
            let {title, authorId, genreIds} = this;
            apiService.addBook({title, authorId, genreIds})
                .then(response => {
                    this.$emit('bookAdded', response.data);
                }).catch(error => {
                    console.info(error.response.data.errors)
                    let errors = [];
                    error.response.data.errors.forEach(e => errors.push(e.message));
                    alert(errors.join('\r\n'));
            });
        }
    }
}
</script>
