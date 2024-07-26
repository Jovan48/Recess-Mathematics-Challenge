<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Challenges extends Model
{
    use HasFactory;
    protected $primaryKey = 'challengeID';
    public $timestamps = true; // Enable timestamps

    protected $fillable = [
        'start_date',
        'end_date',
        'duration',
        'number_of_questions',
    ];
}
