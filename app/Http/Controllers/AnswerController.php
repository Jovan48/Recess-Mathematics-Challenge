<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Imports\AnswersImport;
use Maatwebsite\Excel\Facades\Excel;


class AnswerController extends Controller
{
    // Method to show the import form
    public function importForm()
    {
        return view('answers.import');
    }

    // Method to handle the import
    public function import(Request $request)
    {
        $request->validate([
            'file' => 'required|mimes:xlsx,xls'
        ]);
        try {
            Excel::import(new AnswersImport, $request->file('file'));
            return redirect()->back()->with('success', 'Answers imported successfully.');
        } catch (\Exception $e) {
            return redirect()->back()->with('error', 'Failed to import answers. Please ensure the file is correctly formatted.'. $e->getMessage());
        }
    }
}
