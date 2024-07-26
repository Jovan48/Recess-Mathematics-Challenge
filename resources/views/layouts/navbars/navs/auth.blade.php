<nav class="navbar navbar-expand-lg navbar-light bg-light" color-on-scroll="500">
    <div class="container-fluid">
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-bar burger-lines"></span>
            <span class="navbar-toggler-bar burger-lines"></span>
            <span class="navbar-toggler-bar burger-lines"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navigation">
            <ul class="navbar-nav ml-auto d-flex align-items-center">
                <li class="nav-item">
                    <a class="nav-link" href="{{ route('profile.edit') }}">
                        <span class="no-icon">{{ __('Account') }}</span>
                    </a>
                </li>
                <li class="nav-item">
                    <form id="logout-form" action="{{ route('logout') }}" method="POST" class="m-0 p-0">
                        @csrf
                        <a class="nav-link text-danger" href="{{ route('logout') }}" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                            {{ __('Log out') }}
                        </a>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>
