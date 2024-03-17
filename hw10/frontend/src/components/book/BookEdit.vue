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
                <input id="holder-input" name="title" type="text" v-model="bookTitle"/>
            </div>
            <div>
                <label for="authorSelect">Choose author full name:</label>
                <select id="authorSelect" class="form-control" v-model="selectedAuthorId">
                    <option v-for="author in authors" :value="author.id">{{ author.fullName }}</option>
                </select>
            </div>
            <div>
                <label for="genreSelect">Choose genre names:</label>
                <select multiple id="genreSelect" class="form-control" v-model="selectedGenreIds">
                    <option v-for="genre in genres" :value="genre.id">{{ genre.name }}</option>
                </select>
            </div>
            <button class="btn btn-primary" v-on:click="saveBook()">Save</button>
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
                bookTitle: '',
                selectedAuthorId: 0,
                selectedGenreIds: []
            }
        },
        mounted: function () {
            this.selectedAuthorId = this.bookModel.authorId;
            this.selectedGenreIds = this.bookModel.genreIds;
            this.bookTitle = this.bookModel.title;
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
                let bookToSave = Object.assign({}, this.bookModel);
                bookToSave.title = this.bookTitle;
                bookToSave.authorId = this.selectedAuthorId;
                bookToSave.genreIds = this.selectedGenreIds;
                apiService.updateBook(this.bookModel.id, bookToSave)
                    .then(response => {
                        this.$emit('bookSaved', response.data);
                    });
            }
        }
    }
</script>
