document.addEventListener('DOMContentLoaded', function() {
    // Handle the user creation form submission
    document.getElementById('createUserForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const id = document.getElementById('userId').value;
        const email = document.getElementById('email').value;
        createUser(id, email);
    })

    // Handle the get handicap form submission
    document.getElementById('getHandicapForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const id = document.getElementById('handicapUserId').value;
        getHandicap(id);
    })

    // Handle the get latest scores form submission
    document.getElementById('getLatestScoresForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const id = document.getElementById('latestScoresUserId').value;
        getScores(id);
    })

    // Handle the submit score form submission
    document.getElementById('submitScoreForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const id = document.getElementById('submitUserId').value;
        const rawScore = document.getElementById('rawScore').value;
        const courseRating = document.getElementById('courseRating').value;
        const slopeRating = document.getElementById('slopeRating').value;
        const courseName = document.getElementById('courseName').value;
        submitScore(id, rawScore, courseRating, slopeRating, courseName);
    })
})

function createUser(id, email) {
    axios.post('https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/', { id, email })
        .then(response => {
            document.getElementById('createUserResult').textContent = 'User created successfully! Response: ' + JSON.stringify(response.data);
        })
        .catch(error => {
            document.getElementById('createUserResult').textContent = 'Failed to create user. Error: ' + error;
        });
}

function getHandicap(id) {
    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/handicap`)
        .then(response => {
            document.getElementById('handicapResult').textContent = 'Handicap retrieved successfully! Response: ' + JSON.stringify(response.data);
        })
        .catch(error => {
            document.getElementById('handicapResult').textContent = 'Failed to retrieve handicap. Error: ' + error;
        })
}

function getScores(id) {
    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`)
        .then(response => {
            document.getElementById('scoresResult').textContent = 'Scores retrieved successfully! Response: ' + JSON.stringify(response.data);
        })
        .catch(error => {
            document.getElementById('scoresResult').textContent = 'Failed to retrieve scores. Error: ' + error;
        })
}

function submitScore(id, rawScore, courseRating, slopeRating, courseName) {
    axios.post(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`, { rawScore, courseRating, slopeRating, courseName })
        .then(response => {
            document.getElementById('submitScoreResult').textContent = 'Score submitted successfully! Response: ' + JSON.stringify(response.data);
        })
        .catch(error => {
            document.getElementById('submitScoreResult').textContent = 'Failed to submit score. Error: ' + error;
        });
}