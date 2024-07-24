@extends('layouts.app')

@section('content')
<section class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Schools List</h3>
                        <a href="{{ route('schools.create') }}" class="btn btn-primary float-right">Add New School</a>
                    </div>
                    <div class="card-body">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>District</th>
                                    <th>School Registration Number</th>
                                    <th>Email of Representative</th>
                                    <th>Name of Representative</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                @foreach ($schools as $school)
                                3
                                <tr>
                                    <td>{{ $school->name }}</td>
                                    <td>{{ $school->district }}</td>
                                    <td>{{ $school->school_registration_number }}</td>
                                    <td>{{ $school->email_of_representative }}</td>
                                    <td>{{ $school->name_of_representative }}</td>
                                    <td>
                                    <a href="{{ route('schools.edit',$school->school_registration_number) }}" class="btn btn-warning">Edit</a>
                                        <form action="{{ route('schools.destroy', $school->school_registration_number) }}" method="POST" style="display: inline-block;">
                                            @csrf
                                            @method('DELETE')
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this school?')">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                                @endforeach
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
@endsection
