<template>
    <book-edit v-if="isEditingMode && book.id" :book-model="book" @bookSaved="updateBooksAfterEditing"></book-edit>
    <book-add v-if="isAddingMode" :book-model="book" @bookAdded="updateBooksAfterAdding"></book-add>
    <div v-else class="col-12">
        <h1>Books</h1>
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Author ID</th>
                <th>Genre IDs</th>
                <th>Comments</th>
                <th style="text-align: right">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(book, index) in books" v-bind:key="index">
                <td>{{ book.id }}</td>
                <td>{{ book.title }}</td>
                <td>{{ book.authorId }}</td>
                <td>{{ book.genreIds }}</td>
                <td>
                    <router-link :to="{ name: 'Comments', params: { bookId: book.id }}">Info</router-link>
                </td>
                <td style="display: flex; gap: 12px; justify-content: flex-end">
                    <button class="btn btn-primary" @click="editBook(book)">Edit</button>
                    <button class="btn btn-danger" @click="deleteBook(book.id)">Delete</button>
                </td>
            </tr>
            <tr>
                <td colspan="7">
                    <button class="btn btn-primary" @click="addBook()">Add</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import apiService from '@/services/api-service';
import BookEdit from '@/components/book/BookEdit.vue';
import BookAdd from '@/components/book/BookAdd.vue';

export default {
    name: "Books",
    components: {
        BookEdit,
        BookAdd
    },
    data: function () {
        return {
            isAddingMode: false,
            isEditingMode: false,
            books: [],
            book: {}
        }
    },
    mounted: function () {
        this.getBooks();
    },
    methods: {
        getBooks: function () {
            apiService.getBooks()
                .then(response => {
                    this.books = response.data;
                });
        },
        editBook: function (book) {
            this.book = book;
            this.isEditingMode = true;
        },
        updateBooksAfterEditing: function (book) {
            this.book.title = book.title;
            this.book.author = book.author;
            this.book.genre = book.genre;
            this.isEditingMode = false;
            this.getBooks();
        },
        addBook: function () {
            this.isAddingMode = true;
        },
        updateBooksAfterAdding: function (book) {
            this.books.push(book);
            this.isAddingMode = false;
        },
        deleteBook: function (id) {
            apiService.deleteBook(id)
                .then(() => {
                    this.getBooks();
                });
        }
    }
}
</script>
