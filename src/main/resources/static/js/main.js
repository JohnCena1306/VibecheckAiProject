const result = document.getElementById("result");

function insertSong(song){
    const div = document.createElement("div");
    div.classList.add("song");

    const heading = document.createElement("p");
    heading.classList.add("song-title");
    heading.textContent = song.artist + " - " + song.title;

    const reason = document.createElement("p");
    reason.classList.add("song-reason");
    reason.textContent = song.reason;

    // Gør teksten sikker at sætte ind i en URL så links ikke bliver ødelagt
    const search = encodeURIComponent(song.artist + " " + song.title);

    // Link til youtube
    const youtubeLink = document.createElement("a");
    youtubeLink.classList.add("song-link");
    youtubeLink.href = "https://www.youtube.com/results?search_query=" + search;
    youtubeLink.textContent = "YouTube";
    youtubeLink.target = "_blank";

    // link til spotify
    const spotifyLink = document.createElement("a");
    spotifyLink.classList.add("song-link");
    spotifyLink.href = "https://open.spotify.com/search/" + search;
    spotifyLink.textContent = "Spotify";
    spotifyLink.target = "_blank";

    div.appendChild(heading);
    div.appendChild(reason);
    div.appendChild(youtubeLink);
    div.appendChild(spotifyLink);
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

