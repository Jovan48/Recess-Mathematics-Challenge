<?php

namespace App\Http\Controllers;

use App\Models\School;
use Illuminate\Http\Request;


namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class School extends Model
{
    protected $primaryKey = 'school_registration_number';
    public $incrementing = false;
    protected $keyType = 'string';
    protected $fillable = [
        'name',  // Add all other attributes here that you want to allow mass assignment for
        'district',
        'school_registration_number',
        'email_of_representative',
        'name_of_representative',
    ];
    use HasFactory;
}

