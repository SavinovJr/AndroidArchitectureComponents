package com.example.myfavoritemovies;

import android.content.Intent;
import android.os.Bundle;

import com.example.myfavoritemovies.databinding.ActivityMainBinding;
import com.example.myfavoritemovies.model.Genre;
import com.example.myfavoritemovies.model.Movie;
import com.example.myfavoritemovies.viewmodel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private MainActivityHandlers clickHandlers;
    private Genre selectedGenre;
    private List<Genre> genreList;
    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private int selectedMovieId;

    public static final int ADD_MOVIE_REQUEST_CODE = 5555;
    public static final int EDIT_MOVIE_REQUEST_CODE = 7777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainActivityViewModel =
                new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MainActivityViewModel.class);
        mainActivityViewModel.getGenres().observe(this, new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                genreList = genres;
                for (Genre genre : genres) {
                    Log.d("MY_TAG", genre.getGenreName());
                }
                showInSpinner();
            }
        });

        clickHandlers = new MainActivityHandlers();
        activityMainBinding.setClickHandlers(clickHandlers);

        loadRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MainActivityHandlers {
        public void onFabClicked(View view) {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivityForResult(intent, ADD_MOVIE_REQUEST_CODE);
        }

        public void onSelectedItems(AdapterView<?> parent, View view, int position, long id) {
            selectedGenre = (Genre) parent.getItemAtPosition(position);
            loadGenreMoviesInList(selectedGenre.getId());
        }
    }

    public void showInSpinner() {
        ArrayAdapter<Genre> genreArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genreList);
        genreArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        activityMainBinding.setSpinnerAdapter(genreArrayAdapter);
    }

    private void loadGenreMoviesInList(int genreId) {
        mainActivityViewModel.getGenreMovies(genreId).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieList = movies;
                movieAdapter.setMovieList(movieList);
            }
        });
    }

    private void loadRecyclerView() {
        recyclerView = activityMainBinding.secondaryLayout.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Movie movie) {
                selectedMovieId = movie.getMovieId();
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.MOVIE_ID, selectedMovieId);
                intent.putExtra(AddEditActivity.MOVIE_NAME, movie.getMovieName());
                intent.putExtra(AddEditActivity.MOVIE_DESCRIPTION, movie.getMovieDescription());
                startActivityForResult(intent, EDIT_MOVIE_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Movie movieToDelete = movieList.get(viewHolder.getAdapterPosition());
                mainActivityViewModel.deleteMovie(movieToDelete);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int selectedGenreID = selectedGenre.getId();
        if (requestCode == ADD_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            Movie movie = new Movie();
            movie.setGenreId(selectedGenreID);
            movie.setMovieName(data.getStringExtra(AddEditActivity.MOVIE_NAME));
            movie.setMovieDescription(data.getStringExtra(AddEditActivity.MOVIE_DESCRIPTION));
            mainActivityViewModel.addNewMovie(movie);
        } else if (requestCode == EDIT_MOVIE_REQUEST_CODE && resultCode == RESULT_OK) {
            Movie movie = new Movie();
            movie.setMovieId(selectedMovieId);
            movie.setGenreId(selectedGenreID);
            movie.setMovieName(data.getStringExtra(AddEditActivity.MOVIE_NAME));
            movie.setMovieDescription(data.getStringExtra(AddEditActivity.MOVIE_DESCRIPTION));
            mainActivityViewModel.updateMovie(movie);
        }
    }
}
