@extends('layouts.app')

@section('content')
    <h1>School Rankings</h1>
    <ul>
        @foreach($rankings as $ranking)
            <li>{{ $ranking['school'] }} - Total Score: {{ $ranking['total_score'] }}</li>
        @endforeach
    </ul>
@endsection
