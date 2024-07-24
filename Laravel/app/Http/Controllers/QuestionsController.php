<?php
// app/Http/Controllers/QuestionsController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Imports\QuestionsImport;
use Maatwebsite\Excel\Facades\Excel;
use Illuminate\Support\Facades\Log;

class QuestionsController extends Controller
{
    public function showImportForm()
    {
        return view('questions.import');
    }

    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls'
        ]);

        try {
            Excel::import(new QuestionsImport, $request->file('file'));
            return redirect()->back()->with('success', 'Questions imported successfully.');
        } catch (\Exception $e) {
            Log::error('Failed to import questions: ' . $e->getMessage());
            return redirect()->back()->with('error', 'Failed to import questions. Please ensure the file is correctly formatted.');
        }
    }
}
