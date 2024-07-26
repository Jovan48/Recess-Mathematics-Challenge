<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Answer extends Model
{
    use HasFactory;

    protected $primaryKey = 'answerID'; // Specify the primary key if it's not 'id'
    public $timestamps = true; // Ensure timestamps are enabled

    protected $fillable = [
        'answer',
        'score',
    ];
}
