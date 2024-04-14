<template>
    <div class="col-12">
        <div class="form-group">
            <h1>Edit comment</h1>
            <div class="row">
                <label for="id-input">ID:</label>
                <input id="id-input" type="text" readonly="readonly" :value="commentModel.id"/>
            </div>
            <div class="row">
                <label for="holder-input">Text:</label>
                <input id="holder-input" name="text" type="text" v-model="commentText"/>
            </div>
            <div style="display: flex; gap: 12px; justify-content: center">
                <button class="btn btn-primary" v-on:click="saveComment()">Save</button>
                <button class="btn btn-primary" v-on:click="getCommentsByBookId()">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '../../services/api-service'

export default {
    name: "CommentEdit",
    props: [
        'commentModel'
    ],
    data: function () {
        return {
            commentText: '',
        }
    },
    mounted: function () {
        this.commentText = this.commentModel.text;
    },
    methods: {
        saveComment: function () {
            let commentToSave = {...this.commentModel};
            commentToSave.text = this.commentText;
            apiService.updateComment(this.commentModel.id, commentToSave)
                .then(response => {
                    this.$emit('commentSaved', response.data);
                }).catch(error => {
                    alert("Comment text value should not be blank");
            });
        },
        getCommentsByBookId: function () {
            apiService.getCommentsByBookId(this.bookId)
                .then(response => {
                    this.$emit('commentSaved', response.data);
                });
        }
    }
}
</script>
