<template>
  <comment-edit v-if="editCommentMode" :comment-model="editedComment" @commentSaved="updateComment"></comment-edit>
  <comment-add v-if="addCommentMode" :book-id="bookId" @commentAdded="updateCommentList"></comment-add>
  <div class="col-12" v-if="commentsActive">
    <h1>Comments</h1>
    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Text</th>
        <th style="text-align: right">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(comment, index) in comments" v-bind:key="index">
        <td>{{ comment.id }}</td>
        <td>{{ comment.text }}</td>
        <td style="display: flex; gap: 12px; justify-content: flex-end">
          <button class="btn btn-primary" @click="editComment(comment)">Edit</button>
          <button class="btn btn-danger" @click="deleteComment(comment)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>
    <button class="btn btn-primary" @click="createComment()">Add</button>
  </div>
</template>

<script>
import apiService from '../services/api-service';
import CommentAdd from '@/components/comment/CommentAdd.vue';
import CommentEdit from '@/components/comment/CommentEdit.vue';
export default {
  name: "Comments",
  props: ['bookId'],
  components: {
    CommentAdd,
    CommentEdit
  },
  data: function () {
    return {
      editCommentMode: false,
      addCommentMode: false,
      loading: false,
      editedComment: {},
      comments: [],
      comment: {}
    }
  },
  computed: {
    commentsActive: function () {
      return !(this.editCommentMode || this.addCommentMode || this.loading);
    }
  },
  mounted: function () {
    this.getCommentsByBookId();
  },
  methods: {
    getCommentsByBookId: function () {
      apiService.getCommentsByBookId(this.$route.params.bookId)
          .then(response => {
            this.comments = response.data;
          }).catch(error => {
        // Not used
      });
    },
    createComment: function () {
      this.addCommentMode = true;
    },
    updateCommentList: function () {
      this.getCommentsByBookId();
      this.addCommentMode = false;
    },
    editComment: function (comment) {
      this.editedComment = comment;
      this.editCommentMode = true;
    },
    updateComment: function (comment) {
      let comments = this.comments;
      Object.keys(comments).forEach(key => {
        if (comments[key].id === comment.id) {
          comments[key].text = comment.text;
        }
      });
      this.editCommentMode = false;
    },
    deleteComment: function (id) {
      apiService.deleteComment(id)
          .then(() => {
            this.updateModelAfterDelete(id);
          })
    },
    updateModelAfterDelete: function (id) {
      let comments = this.comments;
      Object.entries(comments).forEach(([key, value]) => {
        if (value.id === id) {
          comments.splice(key, 1)
        }
      })
    }
  }
}
</script>
