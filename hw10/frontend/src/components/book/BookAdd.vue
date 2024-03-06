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
                    <option v-for="author in associations.authors" :value="author.id">{{ author.fullName }}</option>
                </select>
            </div>
            <div>
                <label for="genreSelect">Choose genre names:</label>
                <select multiple id="genreSelect" class="form-control" v-model="genreIds">
                    <option v-for="genre in associations.genres" :value="genre.id">{{ genre.name }}</option>
                </select>
            </div>
            <button class="btn btn-primary" v-on:click="addBook()">Save</button>
        </div>
    </div>
</template>

<script>
    import apiService from '@/services/api-service';
    export default {
        name: "BookCreate",
        data: function () {
            return {
                associations: {},
                title: '',
                authorId: 0,
                genreIds: []
            }
        },
        mounted: function () {
            this.loadAssociations();
        },
        methods: {
            loadAssociations: function () {
                apiService.getAssociations()
                    .then(response => {
                        this.associations = response.data;
                    });
            },
            addBook: function () {
                let {title, authorId, genreIds} = this;
                apiService.addBook({title, authorId, genreIds})
                    .then(response => {
                        this.$emit('bookAdded', response.data);
                    });
            }
        }
    }
</script>
