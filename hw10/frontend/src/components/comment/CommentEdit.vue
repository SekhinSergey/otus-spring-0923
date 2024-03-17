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
            <button class="btn btn-primary" v-on:click="saveComment()">Save</button>
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
                let commentToSave = Object.assign({}, this.commentModel);
                commentToSave.text = this.commentText;
                apiService.updateComment(this.commentModel.id, commentToSave)
                    .then(response => {
                        this.$emit('commentSaved', response.data);
                    });
            }
        }
    }
</script>
