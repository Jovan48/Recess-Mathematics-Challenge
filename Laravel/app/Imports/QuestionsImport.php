<?php
// app/Imports/QuestionsImport.php

namespace App\Imports;

use App\Models\Questions;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Illuminate\Support\Facades\Log;

class QuestionsImport implements ToModel, WithHeadingRow
{
    /**
     * @param array $row
     *
     * @return \Illuminate\Database\Eloquent\Model|null
     */
    public function model(array $row)
    {
        try {
            return new Questions([
                'question' => $row['question'],
            ]);
        } catch (\Exception $e) {
            Log::error('Error importing row: ' . json_encode($row) . ' - ' . $e->getMessage());
            return null;
        }
    }
}
