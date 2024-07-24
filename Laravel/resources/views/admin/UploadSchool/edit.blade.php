@extends('layouts.app')

@section('content')
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Edit School</h3>
                    </div>
                    <div class="card-body">
                        <form method="POST" action="{{ route('schools.update', $school->school_registration_number) }}">
                            @csrf
                            @method('PUT')

                            <div class="form-group">
                                <label for="name">Name</label>
                                <input type="text" name="name" id="name" class="form-control" value="{{ old('name', $school->name) }}" required>
                            </div>

                            <div class="form-group">
                                <label for="district">District</label>
                                <input type="text" name="district" id="district" class="form-control" value="{{ old('district', $school->district) }}" required>
                            </div>

                            <div class="form-group">
                                <label for="school_registration_number">School Registration Number</label>
                                <input type="text" name="school_registration_number" id="school_registration_number" class="form-control" value="{{ old('school_registration_number', $school->school_registration_number) }}" required>
                            </div>

                            <div class="form-group">
                                <label for="email_of_representative">Email of Representative</label>
                                <input type="email" name="email_of_representative" id="email_of_representative" class="form-control" value="{{ old('email_of_representative', $school->email_of_representative) }}" required>
                            </div>

                            <div class="form-group">
                                <label for="name_of_representative">Name of Representative</label>
                                <input type="text" name="name_of_representative" id="name_of_representative" class="form-control" value="{{ old('name_of_representative', $school->name_of_representative) }}" required>
                            </div>

                            <button type="submit" class="btn btn-primary">Update School</button>
                            <a href="{{ route('schools.index') }}" class="btn btn-secondary">Cancel</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
@endsection
