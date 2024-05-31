document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('login-form-submit').addEventListener('submit', function (event) {
        event.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        fetch('http://localhost:8081/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        })
            .then(response => response.json())
            .then(data => {
                if (data.token) {
                    localStorage.setItem('jwtToken', data.token);
                    alert('Login successful!');
                    loadUsers(); // Load users after successful login
                } else {
                    alert('Login failed!');
                }
            })
            .catch(error => console.error('Error:', error));
    });

    function loadUsers() {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('Please log in first');
            return;
        }

        fetch('http://localhost:8081/home/users', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(users => {
                const userList = document.getElementById('user-list');
                userList.innerHTML = '';
                users.forEach(user => {
                    const listItem = document.createElement('li');
                    listItem.textContent = user.name + ' - ' + user.email;
                    userList.appendChild(listItem);
                });
            })
            .catch(error => console.error('Error:', error));
    }

    // Optionally load users on page load if token is present
    if (localStorage.getItem('jwtToken')) {
        loadUsers();
    }
});
