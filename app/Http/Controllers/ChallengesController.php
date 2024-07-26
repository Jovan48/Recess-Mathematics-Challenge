<?php
// app/Http/Controllers/ChallengesController.php

namespace App\Http\Controllers;

use App\Models\Challenge;
use App\Models\Challenges;
use Illuminate\Http\Request;

class ChallengesController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth'); // Ensure user is authenticated
    }

    public function create()
    {
        return view('challenges.create'); // Show the form to create new parameters
    }

    public function store(Request $request)
    {
        // Validate incoming request data
        $request->validate([
            'start_date' => 'required|date',
            'end_date' => 'required|date|after_or_equal:start_date',
            'duration' => 'required|integer|min:1',
            'number_of_questions' => 'required|integer|min:1',
        ]);

        // Create a new Challenge instance and store in database
        $challenge = Challenges::create([
            'start_date' => $request->start_date,
            'end_date' => $request->end_date,
            'duration' => $request->duration,
            'number_of_questions' => $request->number_of_questions,
        ]);

        // Check if challenge was created successfully
        if ($challenge) {
            return redirect()->route('challenges.create')->with('success', 'Parameters successfully set!');
        } else {
            return back()->withInput()->with('error', 'Failed to set parameters. Please try again.');
        }
    }
}
