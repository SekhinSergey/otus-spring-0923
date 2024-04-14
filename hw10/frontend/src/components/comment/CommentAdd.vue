<template>
    <div class="col-12">
        <div class="form-group">
            <h1>Add comment</h1>
            <div class="row">
                <label for="holder-input">Text:</label>
                <input id="holder-input" name="text" type="text" v-model="text"/>
            </div>
            <div style="display: flex; gap: 12px; justify-content: center">
                <button class="btn btn-primary" v-on:click="addComment()">Save</button>
                <button class="btn btn-primary" v-on:click="getCommentsByBookId()">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '@/services/api-service';

export default {
    name: "CommentAdd",
    props: [
        'bookId'
    ],
    data: function () {
        return {
            text: '',
        }
    },
    methods: {
        addComment: function () {
            let {text, bookId} = this;
            apiService.addComment({text, bookId})
                .then(response => {
                    this.$emit('commentAdded', response.data);
                }).catch(error => {
                    alert("Comment text value should not be blank");
            });
        },
        getCommentsByBookId: function () {
            apiService.getCommentsByBookId(this.bookId)
                .then(response => {
                    this.$emit('commentAdded', response.data);
                });
        }
    }
}
</script>
