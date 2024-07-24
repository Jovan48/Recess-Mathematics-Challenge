@extends('layouts.app')

@section('content')
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Add New School</h3>
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

                        <form method="POST" action="{{ route('schools.store') }}">
                            @csrf
                            <div class="form-group">
                                <label for="name">Name</label>
                                <input type="text" name="name" id="name" class="form-control" required value="{{ old('name') }}">
                            </div>
                            <div class="form-group">
                                <label for="district">District</label>
                                <input type="text" name="district" id="district" class="form-control" required value="{{ old('district') }}">
                            </div>
                            <div class="form-group">
                                <label for="school_registration_number">School Registration Number</label>
                                <input type="text" name="school_registration_number" id="school_registration_number" class="form-control" required value="{{ old('school_registration_number') }}">
                                @error('school_registration_number')
                                    <div class="text-danger">{{ $message }}</div>
                                @enderror
                            </div>
                            <div class="form-group">
                                <label for="email_of_representative">Email of Representative</label>
                                <input type="email" name="email_of_representative" id="email_of_representative" class="form-control" required value="{{ old('email_of_representative') }}">
                                @error('email_of_representative')
                                    <div class="text-danger">{{ $message }}</div>
                                @enderror
                            </div>
                            <div class="form-group">
                                <label for="name_of_representative">Name of Representative</label>
                                <input type="text" name="name_of_representative" id="name_of_representative" class="form-control" required value="{{ old('name_of_representative') }}">
                            </div>
                            <button type="submit" class="btn btn-primary">Add School</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
@endsection
