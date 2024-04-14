<template>
    <div class="col-12">
        <div class="form-group">
            <h1>Book editing</h1>
            <div>
                <label for="id-input">ID:</label>
                <input id="id-input" type="text" readonly="readonly" :value="bookModel.id"/>
            </div>
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
                <button class="btn btn-primary" v-on:click="saveBook()">Save</button>
                <button class="btn btn-primary" v-on:click="getBooks()">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '../../services/api-service'

export default {
    name: "BookEdit",
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
        saveBook: function () {
            let bookToSave = {...this.bookModel};
            bookToSave.title = this.title;
            bookToSave.authorId = this.authorId;
            bookToSave.genreIds = this.genreIds;
            apiService.updateBook(this.bookModel.id, bookToSave)
                .then(response => {
                    this.$emit('bookSaved', response.data);
                }).catch(error => {
                    alert("Book title value should not be blank");
            });
        },
        getBooks: function () {
            apiService.getBooks()
                .then(response => {
                    this.$emit('bookSaved', response.data);
                });
        }
    }
}
</script>
