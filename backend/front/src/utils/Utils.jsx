class Utils {
    saveUser(user) {
        localStorage.setItem('user', JSON.stringify(user))
    }

    removeUser() {
        localStorage.removeItem('user')
    }

    getToken() {
        let user = JSON.parse(localStorage.getItem('user'))
        return user && "Bearer " + user.token;
    }

    getUserName() {
        let user = JSON.parse(localStorage.getItem('user'));
        return user && user.login;
    }

    getEmail() {
        let user = JSON.parse(localStorage.getItem('user'));
        return user.email;
    }

}

export default new Utils()
