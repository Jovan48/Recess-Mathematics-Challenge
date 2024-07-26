<?php

use App\Mail\ReportMail;
use Illuminate\Support\Facades\Mail;

 function sendReport($participant)
{
    $report = $this->generateReport($participant);
    Mail::to($participant->email)->send(new ReportMail($report));
}

 function generateReport($participant)
{
    // Generate the report content
    return "Report content for participant " . $participant->name;
}
