import axios from 'axios'

const apiClient = axios.create({
    baseURL: '/api/library',
    withCredentials: false,
    headers: {'Content-Type': 'application/json'}
});

export default {
    getBooks() {
        return apiClient.get('/books');
    },
    updateBook(data) {
        return apiClient.put('/book', data);
    },
    addBook(data) {
        return apiClient.post('/book', data);
    },
    deleteBook(bookId) {
        return apiClient.delete(`/book?id=${bookId}`);
    },
    getAssociations() {
        return apiClient.get('/associations');
    },
    getCommentsByBookId(bookId) {
        return apiClient.get(`/comments?bookId=${bookId}`);
    },
    updateComment(data) {
        return apiClient.put('/comment', data);
    },
    addComment(data) {
        return apiClient.post('/comment', data);
    },
    deleteComment(commentId) {
        return apiClient.delete(`/comment?id=${commentId}`);
    },
    getAuthors() {
        return apiClient.get('/authors');
    },
    getGenres() {
        return apiClient.get('/genres');
    }
}
