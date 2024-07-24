<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Questions extends Model
{
    use HasFactory;
    protected $primaryKey = 'questionID';
    public $timestamps = true; // Enable timestamps

    protected $fillable = [
        'question',
        
    ];
}
