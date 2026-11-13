const result = document.getElementById("result");

function insertSong(song){
    const div = document.createElement("div");

    const heading = document.createElement("p");
    heading.textContent = song.artist + " - " + song.title;

    const reason = document.createElement("p");
    reason.textContent = song.reason;
    div.appendChild(heading);
    div.appendChild(reason);
    result.appendChild(div);
}

async function getRecommendations(event){
    event.preventDefault();

    const params = new URLSearchParams({
        genre: document.getElementById("genre").value,
        mood: document.getElementById("mood").value,
        artists: document.getElementById("artists").value
    });

    result.textContent = "Finding music...";

    const response = await fetch("/recommend?" + params);

    if (!response.ok) {
        result.textContent = "Something went wrong."
        return;
    }

    const songs = await response.json();

    if (songs.length === 0) {
        result.textContent = "Couldn't find any songs this time. Try again!";
        return;
    }

    result.textContent = "";
    songs.forEach(insertSong);
}

document.getElementById("form").addEventListener("submit", getRecommendations);

