<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use PhpOffice\PhpSpreadsheet\IOFactory;
use App\Models\Question;

class FileUploadController2 extends Controller
{
    public function uploadQuestions(Request $request)
    {
        $file = $request->file('questions');
        $spreadsheet = IOFactory::load($file->getRealPath());
        $sheetData = $spreadsheet->getActiveSheet()->toArray(null, true, true, true);

        foreach ($sheetData as $row) {
            Question::create([
                'text' => $row['A'],
                'answer' => $row['B'],
                'marks' => $row['C'],
                'challenge_id' => $request->input('challenge_id')
            ]);
        }

        return back()->with('success', 'Questions uploaded successfully');
    }
}

