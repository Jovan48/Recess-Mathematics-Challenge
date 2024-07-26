<form action="/upload-questions" method="POST" enctype="multipart/form-data">
    @csrf
    <input type="file" name="questions" required>
    <input type="hidden" name="challenge_id" value="{{ $challengeId }}">
    <button type="submit">Upload Questions</button>
</form>
