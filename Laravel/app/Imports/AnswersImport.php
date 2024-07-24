<?php

namespace App\Imports;

use App\Models\Answer;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Illuminate\Support\Facades\Log;

class AnswersImport implements ToModel, WithHeadingRow
{
    /**
    * @param array $row
    *
    * @return \Illuminate\Database\Eloquent\Model|null
    */
    public function model(array $row)
    {   
        Log::info('Raw row data: ', $row);

        // Check for null or empty values
        if (is_null($row['answer']) || is_null($row['score']) || trim($row['answer']) === '' || trim($row['score']) === '') {
            Log::error('Skipping row due to missing data: ', $row);
            return null;
        }

        return new Answer([
            'answer' => $row['answer'],
            'score' => $row['score']
        ]);
    }
}
