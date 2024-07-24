<div class="sidebar" data-image="{{ asset('light-bootstrap/img/sidebar-5.jpg') }}">
    <div class="sidebar-wrapper">
        <div class="logo">
            <a href="" class="simple-text">
            <img src="{{ asset('light-bootstrap/img/neural.png') }}" style="width:25px">
                {{ __("Mathematics Challenge Administrator") }}
            </a>
        </div>
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="{{route('dashboard')}}">
                    <i class="nc-icon nc-chart-pie-35"></i>
                    <p>{{ __("Dashboard") }}</p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="">
                    <i>
                        <img src="{{ asset('light-bootstrap/img/laravel.svg') }}" style="width:25px">
                    </i>
                    <p>
                        {{ __('Admin actions') }}
                        <b class="caret"></b>
                    </p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="{{route('UploadSchool')}}">
                    <i class="nc-icon nc-single-02"></i>
                    <p>Upload School</p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="{{route('questions.import.form')}}">
                    <i class="nc-icon nc-circle-09"></i>
                    <p>{{ __("Upload Questions") }}</p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="{{route('import.form')}}">
                    <i class="nc-icon nc-circle-09"></i>
                    <p>{{ __("Upload Answers") }}</p>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="{{route('challenges.create')}}">
                    <i class="nc-icon nc-circle-09"></i>
                    <p>{{ __("Set Parameters") }}</p>
                </a>
            </li>
        </ul>
    </div>
</div>
