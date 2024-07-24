<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('reports', function (Blueprint $table) {
            $table->increments('reportID');
            $table->string('participantID')->nullable();
            $table->string('username')->nullable(false);
            $table->foreign('username')->references('username')->on('participants');
            $table->unsignedinteger('challengeID');
            $table->foreign('challengeID')->references('challengeID')->on('challenges');
            $table->integer('score')->nullable(false);
            $table->integer('time_taken')->nullable(false);
            $table->timestamps(); // Add timestamps
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('reports');
    }
};
