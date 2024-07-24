<!-- resources/views/challenges/create.blade.php -->

@extends('layouts.app')

@section('content')
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Set Challenge Parameters</h3>
                    </div>
                    <div class="card-body">
                        @if (session('success'))
                            <div class="alert alert-success">
                                {{ session('success') }}
                            </div>
                        @endif
                        @if (session('error'))
                            <div class="alert alert-danger">
                                {{ session('error') }}
                            </div>
                        @endif

                        <form method="POST" action="{{ route('challenges.store') }}">
                            @csrf
                            <div class="form-group">
                                <label for="start_date">Start Date</label>
                                <input type="date" name="start_date" id="start_date" class="form-control" required value="{{ old('start_date') }}">
                            </div>
                            <div class="form-group">
                                <label for="end_date">End Date</label>
                                <input type="date" name="end_date" id="end_date" class="form-control" required value="{{ old('end_date') }}">
                            </div>
                            <div class="form-group">
                                <label for="duration">Duration (days)</label>
                                <input type="number" name="duration" id="duration" class="form-control" required value="{{ old('duration') }}">
                            </div>
                            <div class="form-group">
                                <label for="number_of_questions">Number of Questions</label>
                                <input type="number" name="number_of_questions" id="number_of_questions" class="form-control" required value="{{ old('number_of_questions') }}">
                            </div>
                            <button type="submit" class="btn btn-primary">Set Challenge</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
@endsection
