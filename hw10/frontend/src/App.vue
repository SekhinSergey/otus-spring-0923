<template>
    <div id="nav">
        <router-link to="/">Main</router-link>
        |
        <router-link to="/books">Books</router-link>
        |
        <router-link to="/authors">Authors</router-link>
        |
        <router-link to="/genres">Genres</router-link>
    </div>
    <div class="container">
        <div class="row">
            <router-view/>
        </div>
    </div>
</template>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
}

#nav {
    padding: 30px;
}

#nav a {
    font-weight: bold;
    color: #2c3e50;
}

#nav a.router-link-exact-active {
    color: #42b983;
}
</style>
