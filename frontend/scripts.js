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

//function createUser(userId, email) {
//    axios.post('https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/', { userId, email })
//        .then(response => {
//            document.getElementById('createUserResult').textContent = 'User created successfully! Response: ' + JSON.stringify(response.data);
//        })
//        .catch(error => {
//            document.getElementById('createUserResult').textContent = 'Failed to create user. Error: ' + error;
//        });
//}

function createUser(userId, email) {
    axios.post('https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/', { userId, email })
        .then(response => {
            const user = response.data.userModel; // Access the userModel from the response
            const resultContainer = document.getElementById('createUserResult');
            resultContainer.innerHTML = ''; // clear previous result
            if (user && user.userId && user.email) { // Check if userModel and properties are defined
                resultContainer.innerHTML = `<strong>User ID:</strong> ${user.userId}, <strong>Email:</strong> ${user.email}`;
            } else {
                resultContainer.textContent = 'Provided User ID must be alphanumeric and email must be valid. Otherwise, User ID or Email may be taken.';
            }
        })
        .catch(error => {
            document.getElementById('createUserResult').textContent = 'Failed to create user. Error: ' + error;
            console.error("Error creating user:", error);
        });
}

//function getHandicap(id) {
//    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/handicap`)
//        .then(response => {
//            document.getElementById('handicapResult').textContent = 'Handicap retrieved successfully! Response: ' + JSON.stringify(response.data);
//        })
//        .catch(error => {
//            document.getElementById('handicapResult').textContent = 'Failed to retrieve handicap. Error: ' + error;
//        })
//}

function getHandicap(id) {
    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/handicap`)
        .then(response => {
            const handicap = response.data.handicapIndex;  // Access the handicapIndex from the response
            const resultContainer = document.getElementById('handicapResult');
            resultContainer.innerHTML = ''; // Clear previous result
            if (handicap !== undefined) {  // Check if handicapIndex is defined
                resultContainer.innerHTML = `<strong>Handicap Index:</strong> ${handicap}`;
            } else {
                resultContainer.textContent = 'Incorrect User ID provided or less than 20 games were played.';
            }
        })
        .catch(error => {
            document.getElementById('handicapResult').textContent = 'Failed to retrieve handicap. Error: ' + error;
            console.error("Error retrieving handicap:", error);
        });
}


//function getScores(id) {
//    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`)
//        .then(response => {
//            document.getElementById('scoresResult').textContent = 'Scores retrieved successfully! Response: ' + JSON.stringify(response.data);
//        })
//        .catch(error => {
//            document.getElementById('scoresResult').textContent = 'Failed to retrieve scores. Error: ' + error;
//        })
//}

function getScores(id) {
    axios.get(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`)
        .then(response => {
            console.log('Data received:', response.data);  // Log the full data object
            const scoresContainer = document.getElementById('scoresResult');
            scoresContainer.innerHTML = '';  // Clear previous results
            const scores = response.data.scoreModels;
            if (scores && scores.length > 0) {
                scores.forEach(score => {
                    console.log('Processing score:', score);  // Log each score
                    const scoreDiv = document.createElement('div');
                    scoreDiv.classList.add('score-entry');
                    scoreDiv.innerHTML = `<strong>Date:</strong> ${score.dateTime}, <strong>Score:</strong> ${score.rawScore}, <strong>Differential:</strong> ${score.handicapDifferential}`;
                    scoresContainer.appendChild(scoreDiv);
                });
            } else {
                scoresContainer.textContent = 'Incorrect User ID provided or less than 1 game played.';
            }
        })
        .catch(error => {
            document.getElementById('scoresResult').textContent = 'Failed to retrieve scores. Error: ' + error;
            console.error('Error fetching scores:', error);  // Log the error
        });
}

//function submitScore(id, rawScore, courseRating, slopeRating, courseName) {
//    axios.post(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`, { rawScore, courseRating, slopeRating, courseName })
//        .then(response => {
//            document.getElementById('submitScoreResult').textContent = 'Score submitted successfully! Response: ' + JSON.stringify(response.data);
//        })
//        .catch(error => {
//            document.getElementById('submitScoreResult').textContent = 'Failed to submit score. Error: ' + error;
//        });
//}

function submitScore(id, rawScore, courseRating, slopeRating, courseName) {
    axios.post(`https://fab1jynslk.execute-api.us-west-2.amazonaws.com/prod/golfcalculator/${id}/scores`, { rawScore, courseRating, slopeRating, courseName })
        .then(response => {
            const score = response.data.scoreModel;  // Adjusted to access scoreModel
            const resultContainer = document.getElementById('submitScoreResult');
            resultContainer.innerHTML = ''; // Clear previous result
            if (score) {
                const scoreDiv = document.createElement('div');
                scoreDiv.classList.add('score-entry');
                scoreDiv.innerHTML = `<strong>Date:</strong> ${score.dateTime}, <strong>Score:</strong> ${score.rawScore}, <strong>Differential:</strong> ${score.handicapDifferential}`;
                resultContainer.appendChild(scoreDiv);
            } else {
                resultContainer.textContent = 'Incorrect User ID provided or Raw Score left blank.';
            }
        })
        .catch(error => {
            document.getElementById('submitScoreResult').textContent = 'Failed to submit score. Error: ' + error;
            console.error("Error submitting score:", error);
        });
}

