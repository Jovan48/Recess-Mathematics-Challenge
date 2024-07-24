<?php

namespace App\Http\Controllers;

use App\Models\School;
use Illuminate\Http\Request;

class SchoolsController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth'); // Ensure user is authenticated
    }
    public function index()
    {
        $schools = School::all();
        return view('admin.UploadSchool.index', compact('schools'));
    }

    public function create()
    {
        return view('admin.UploadSchool.create'); // Show the form to create a new school
    }

    public function store(Request $request)
    {
        // Validate incoming request data
        $request->validate([
            'name' => 'required|string|max:255',
            'district' => 'required|string|max:255',
            'school_registration_number' => 'required|string|max:50|unique:schools,school_registration_number',
            'email_of_representative' => 'required|email|max:255|unique:schools,email_of_representative',
            'name_of_representative' => 'required|string|max:255',
        ]);

        // Create a new School instance and store in database
        $school = School::create([
            'name' => $request->name,
            'district' => $request->district,
            'school_registration_number' => $request->school_registration_number,
            'email_of_representative' => $request->email_of_representative,
            'name_of_representative' => $request->name_of_representative,
        ]);

        // Check if school was created successfully
        if ($school) {
            return redirect()->route('schools.create')->with('success', 'School Added Successfully!');
        } else {
            return back()->withInput()->with('error', 'Failed to add school. Please try again.');
        }
    }

    public function edit($school_registration_number)
    {
        $school = School::where('school_registration_number',$school_registration_number)->firstOrFail();

        return view('admin.UploadSchool.edit', compact('school'));
    }

    public function update(Request $request, $school_registration_number)
    {
        // Validate incoming request data
        $request->validate([
            'name' => 'required|string|max:255',
            'district' => 'required|string|max:255',
            'school_registration_number' => 'required|string|max:50|unique:schools,school_registration_number,'.$school_registration_number,
            'email_of_representative' => 'required|email|max:255|unique:schools,email_of_representative,'.$school_registration_number,
            'name_of_representative' => 'required|string|max:255',
        ]);

        // Update the School instance in database
        $school = School::where('school_registration_number', $school_registration_number)->firstOrFail();
        $school->update([
            'name' => $request->name,
            'district' => $request->district,
            'school_registration_number' => $request->school_registration_number,
            'email_of_representative' => $request->email_of_representative,
            'name_of_representative' => $request->name_of_representative,
        ]);

        // Check if school was updated successfully
        if ($school) {
            return redirect()->route('admin.UploadSchool.create')->with('success', 'School Updated Successfully!');
        } else {
            return back()->withInput()->with('error', 'Failed to update school. Please try again.');
        }
    }

    public function destroy($school_registration_number)
    {
        $school = School::where('school_registration_number', $school_registration_number)->firstOrFail();
        $school->delete();
      
        return redirect()->route('schools.create')->with('success', 'School Deleted Successfully!');
        
    }
}
