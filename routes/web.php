    <?php
    use Illuminate\Support\Facades\Route;
    use App\Http\Controllers\HomeController;
    use App\Http\Controllers\UserController;
    use App\Http\Controllers\ProfileController;
    use App\Http\Controllers\PageController;
    use App\Http\Controllers\SchoolsController;
    use App\Http\Controllers\ChallengesController;
    use App\Http\Controllers\QuestionsController;
    use App\Http\Controllers\AnswerController;

    /*
    |--------------------------------------------------------------------------
    | Web Routes
    |--------------------------------------------------------------------------
    |
    | Here is where you can register web routes for your application. These
    | routes are loaded by the RouteServiceProvider and all of them will
    | be assigned to the "web" middleware group. Make something great!
    |
    */

    Route::get('/', function () {
        return view('welcome');
    });

    Auth::routes();

    Route::get('/admin', [HomeController::class, 'index'])->name('dashboard');
    Route::get('/home', [HomeController::class, 'index'])->name('home');

    Route::group(['middleware' => 'auth'], function () {
        // User routes
        Route::get('user', [UserController::class, 'index'])->name('user.index');
        Route::get('user/create', [UserController::class, 'create'])->name('user.create');
        Route::post('user', [UserController::class, 'store'])->name('user.store');
        Route::get('user/{user}/edit', [UserController::class, 'edit'])->name('user.edit');
        Route::put('user/{user}', [UserController::class, 'update'])->name('user.update');
        Route::delete('user/{user}', [UserController::class, 'destroy'])->name('user.destroy');

        // Profile routes
        Route::get('profile', [ProfileController::class, 'edit'])->name('profile.edit');
        Route::patch('profile', [ProfileController::class, 'update'])->name('profile.update');
        Route::patch('profile/password', [ProfileController::class, 'password'])->name('profile.password');

        // Page routes
        Route::get('{page}', [PageController::class, 'index'])->name('page.index');

        // Schools routes
        Route::get('/schools', [SchoolsController::class, 'index'])->name('schools.index');
        Route::get('/schools/create', [SchoolsController::class, 'create'])->name('schools.create');
        Route::post('/schools', [SchoolsController::class, 'store'])->name('schools.store');
        Route::get('/schools/{school_registration_number}/edit', [SchoolsController::class, 'edit'])->name('schools.edit');
        Route::put('/schools/{school_registration_number}', [SchoolsController::class, 'update'])->name('schools.update');
        Route::delete('/schools/{school_registration_number}', [SchoolsController::class, 'destroy'])->name('schools.destroy');

        // Upload School route
        Route::get('/admin/schools', [SchoolsController::class, 'index'])->name('UploadSchool');

        // Challenges routes
        Route::get('/challenges/create', [ChallengesController::class, 'create'])->name('challenges.create');
        Route::post('/challenges/store', [ChallengesController::class, 'store'])->name('challenges.store');

        // Questions import routes
        Route::get('/questions/import', [QuestionsController::class, 'showImportForm'])->name('questions.import.form');
        Route::post('/questions/import', [QuestionsController::class, 'import'])->name('questions.import');
        
        
        
        Route::get('/answers/import', [AnswerController::class, 'importForm'])->name('import.form');
        Route::post('/answers/import', [AnswerController::class, 'import'])->name('answers.import');

        // file upload for analysis
        Route::get('/upload-questions', [FileUploadController::class, 'showUploadForm']);
        Route::post('/upload-questions', [FileUploadController::class, 'uploadQuestions']);

        // Routes for analysis
        Route::get('/analysis/most-correctly-answered', [AnalysisController::class, 'mostCorrectlyAnsweredQuestions']);
        Route::get('/analysis/school-rankings', [AnalysisController::class, 'schoolRankings']);


    });


