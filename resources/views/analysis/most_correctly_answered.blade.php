@extends('layouts.app')

@section('content')
    <h1>Most Correctly Answered Questions</h1>
    <ul>
        @foreach($questions as $question)
            <li>Question ID: {{ $question->question_id }} - Correct Answers: {{ $question->total_correct }}</li>
        @endforeach
    </ul>
@endsection
